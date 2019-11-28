package com.webshop.kung.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

//    private final DataSource datasource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/addNewCategory/**", "/addNewProduct/**", "/showOrders/**").hasRole("ADMIN")
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated().and().formLogin();
 //               .anyRequest().authenticated().and().formLogin();

        // for H2 console
        http.csrf().disable();
        http.headers().frameOptions().disable();

        // CSP headers
//        http.headers()
//                .addHeaderWriter(new StaticHeadersWriter("Content-Security-Policy",
//                        "script-src 'self' https://ajax.googleapis.com https://maxcdn.bootstrapcdn.com"));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**");
    }

//  use database (tables users and authorities for roles)
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication()
//                .dataSource(this.datasource).;
//    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        DelegatingPasswordEncoder encoder =  (DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder();
        encoder.setDefaultPasswordEncoderForMatches(new BCryptPasswordEncoder());
        return encoder;
    }


 //   public DataSource getDatasource() {
//        return datasource;
//    }
}
