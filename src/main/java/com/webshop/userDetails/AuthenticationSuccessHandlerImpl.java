package com.webshop.userDetails;

import com.webshop.model.Authorities;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    public static final String USERNAME = "username";
    public static final String ROLE = "role";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            request.getSession().setAttribute(USERNAME, userDetails.getUsername());
        }
        request.getSession().setAttribute(ROLE, SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());
        if(requiresTotpAuthentication(authentication)) {
            redirectStrategy.sendRedirect(request, response, "/totp-login");
        } else {
            redirectStrategy.sendRedirect(request, response, "/home");
        }
    }

    private boolean requiresTotpAuthentication(Authentication authentication) {
        Set<String> authorities = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        return authorities.contains(Authorities.TOTP_AUTH_AUTHORITY);
    }

}