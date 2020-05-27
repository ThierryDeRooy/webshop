package com.webshop.config;

import com.webshop.filter.TotpAuthenticationFilter;
import com.webshop.model.Authorities;
import com.webshop.userDetails.AuthenticationSuccessHandlerImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

//    private final DataSource datasource;
    @Autowired
    private TotpAuthenticationFilter totpAuthFilter;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    DataSource dataSource;
//    private PersistentTokenRepository persistentTokenRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(totpAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                    .loginPage("/login")
                    .successHandler(new AuthenticationSuccessHandlerImpl())
                    .failureUrl("/login-error")
                    .and()
                .rememberMe()
                    .key("remember-me-key")
                    .tokenValiditySeconds(3600)
                    .tokenRepository(createJdbcTokenRepository())
                    .authenticationSuccessHandler(new AuthenticationSuccessHandlerImpl())
                    .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login")
                    .deleteCookies("remember-me")
                    .and()
                .authorizeRequests()
                   .mvcMatchers("/h2").permitAll()
                    .mvcMatchers("/", "/home", "/contact", "/login", "/logout", "/error", "/login-error", "/register", "/pwReset", "/sendUsername", "/chgpwemail", "/verify/email").permitAll()
                    .mvcMatchers("/changePasswordNoOld", "/login-verified", "/newAdminVerificationemail").permitAll()
                    .mvcMatchers( "/productList", "/buyProduct", "/orderProduct", "/shoppingCart", "/shoppingCartRemoveProduct", "/shoppingCartCustomer", "/shoppingCartConfirmation").permitAll()
                    .mvcMatchers("/img/**", "/images/**").permitAll()
                    .mvcMatchers("/favicon.ico").permitAll()
                    .mvcMatchers("/changePassword").authenticated()
                    .mvcMatchers("/showOrderList", "/orderSame", "/orderIdentically").hasRole("CUSTOMER")
                    .mvcMatchers("/totp-login","/totp-login-error").hasAuthority(Authorities.TOTP_AUTH_AUTHORITY)
                    .mvcMatchers("/showCategories", "/saveCategory", "/addNewProduct", "/saveProduct", "/showOrders", "/webusers", "/removeWebUser", "/updateOrder", "/deleteOrder", "/removeProductFromBaskets").hasRole("ADMIN")
                    .mvcMatchers("/showTransportCosts", "/saveTransportCost", "/showCountries", "/saveCountry", "/removeCountry", "/showInvoice", "/resendEmail", "/sessions", "removeSession").hasRole("ADMIN")
                    .mvcMatchers("/account", "/setup-totp", "/confirm-totp").hasRole("ADMIN")
//                    .anyRequest().denyAll()
                    .anyRequest().permitAll()
                    .and()
                .sessionManagement()
                    .sessionFixation().migrateSession()
                    .maximumSessions(10).expiredUrl("/logout").maxSessionsPreventsLogin(false).sessionRegistry(sessionRegistry());

//        http.addFilterBefore(totpAuthFilter, UsernamePasswordAuthenticationFilter.class)
//                .authorizeRequests().antMatchers("/addNewCategory/**", "/addNewProduct/**", "/showOrders/**", "/newAdmin/**").hasRole("ADMIN")
//                .antMatchers("/totp-login","/totp-login-error").hasAuthority(Authorities.TOTP_AUTH_AUTHORITY)
//                .antMatchers("/**").permitAll()
//                .anyRequest().authenticated().and()
//                .formLogin().loginPage("/login").successHandler(new AuthenticationSuccessHandlerImpl()).failureUrl("/login-error")
//                .and().rememberMe().key("remember-me-key").tokenRepository(createJdbcTokenRepository())
//                .authenticationSuccessHandler(new AuthenticationSuccessHandlerImpl())
//                .and().logout().logoutSuccessUrl("/login")
//                .deleteCookies("remember-me");
 //               .anyRequest().authenticated().and().formLogin();
        http.csrf().ignoringAntMatchers("/setup-totp", "/confirm-totp");
        // for H2 console
        http.csrf().disable();
        http.headers().frameOptions().disable();

        // CSP headers
//        http.headers()
//                .addHeaderWriter(new StaticHeadersWriter("Content-Security-Policy",
//                        "script-src 'self' https://localhost:8443 https://ajax.googleapis.com https://maxcdn.bootstrapcdn.com https://cdnjs.cloudflare.com https://www.google.com/recaptcha/ https://www.gstatic.com/recaptcha/"))
//                .addHeaderWriter(new StaticHeadersWriter("Content-Security-Policy",
//                "frame-src 'self' https://www.google.com/recaptcha/"));
        http.headers().cacheControl().disable();


//        http.sessionManagement().maximumSessions(1);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**");
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return userDetailsService;
    }


//  use database (tables users and authorities for roles)
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication()
//                .dataSource(this.datasource).;
//    }

    @Bean
    public PersistentTokenRepository createJdbcTokenRepository() {
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setDataSource(dataSource);
        return repository;
    }


    @Bean
    public PasswordEncoder getPasswordEncoder() {
        DelegatingPasswordEncoder encoder =  (DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder();
        encoder.setDefaultPasswordEncoderForMatches(new BCryptPasswordEncoder());
        return encoder;
    }

    // Work around https://jira.spring.io/browse/SEC-2855
    @Bean
    public SessionRegistry sessionRegistry() {
        SessionRegistry sessionRegistry = new SessionRegistryImpl();
        return sessionRegistry;
    }

    // Register HttpSessionEventPublisher
    @Bean
    public static ServletListenerRegistrationBean httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
    }


//   public DataSource getDatasource() {
//        return datasource;
//    }
}
