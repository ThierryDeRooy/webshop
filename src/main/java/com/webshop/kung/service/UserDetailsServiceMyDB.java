package com.webshop.kung.service;

import com.webshop.kung.entity.Admin;
import com.webshop.kung.entity.User;
import com.webshop.kung.repository.AdminRepository;
import com.webshop.kung.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceMyDB implements UserDetailsService  {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            Admin admin = adminRepository.findByUsername(username);
            if (admin == null)
                throw new UsernameNotFoundException(username);
            return org.springframework.security.core.userdetails.User.withUsername(admin.getUsername()).password(admin.getPassword()).roles(admin.getRole()).build();
        }
        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername()).password(user.getPassword()).roles("USER").build();
    }
}
