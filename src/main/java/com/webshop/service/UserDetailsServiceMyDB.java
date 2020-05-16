package com.webshop.service;

import com.webshop.entity.WebUser;
import com.webshop.model.Authorities;
import com.webshop.repository.WebUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceMyDB implements UserDetailsService  {

    private final WebUserRepository webUserRepository;
    private final LoginAttemptService loginAttemptService;
    private final HttpServletRequest request;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String ip = getClientIP();
        if (loginAttemptService.isBlocked(ip)) {
            throw new RuntimeException("blocked");
        }


        WebUser webUser = webUserRepository.findByUsername(username);
        if (webUser == null)
            throw new UsernameNotFoundException(username);
        List<String> authorities = new ArrayList<>();
        if(webUser.isTotpEnabled()) {
            authorities.add(Authorities.TOTP_AUTH_AUTHORITY);
            authorities.add(Authorities.ROLE_TEMP + webUser.getRole());
        } else {
            return org.springframework.security.core.userdetails.User
                    .withUsername(webUser.getUsername())
                    .password(webUser.getPassword())
                    .roles(webUser.getRole())
                    .disabled(!webUser.isVerified())
                    .build();

        }

        return org.springframework.security.core.userdetails.User
                .withUsername(webUser.getUsername())
                .password(webUser.getPassword())
                .roles(webUser.getRole())
                .disabled(!webUser.isVerified())
                .authorities(buildAuthorities(authorities))
                .build();

//        Customer customer = customerRepository.findByUsername(username);
//        if (customer == null) {
//            WebUser webUser = adminRepository.findByUsername(username);
//            if (webUser == null)
//                throw new UsernameNotFoundException(username);
//            return org.springframework.security.core.userdetails.User.withUsername(webUser.getUsername()).password(webUser.getPassword()).roles(webUser.getRole()).build();
//        }
//        return org.springframework.security.core.userdetails.User.withUsername(customer.getUsername()).password(customer.getPassword()).roles("CUSTOMER").disabled(!customer.isVerified()).build();
    }


    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    private List<GrantedAuthority> buildAuthorities(List<String> authorities) {
        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>(1);
        for(String authority : authorities) {
            authList.add(new SimpleGrantedAuthority(authority));
        }
        return authList;
    }

}
