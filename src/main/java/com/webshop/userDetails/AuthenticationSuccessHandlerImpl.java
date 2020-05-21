package com.webshop.userDetails;

import com.webshop.model.Authorities;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        request.getSession().setAttribute(USERNAME, request.getParameter(USERNAME));
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