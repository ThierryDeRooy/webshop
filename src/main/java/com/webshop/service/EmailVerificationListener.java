package com.webshop.service;

import com.webshop.event.UserRegistrationEvent;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@PropertySource("classpath:application.properties")
@RequiredArgsConstructor
public class EmailVerificationListener implements ApplicationListener<UserRegistrationEvent> {

    private final JavaMailSender mailSender;
    private final VerificationService verificationService;
    private final MessageSource messageSource;

    @Value("${number.chars.verification.id}")
    private int numberCharsVerificationId;

    @Override
    public void onApplicationEvent(UserRegistrationEvent event) {
        String username = event.getUser().getUsername();
        String action = event.getAction();

        String url = event.getRequest().getRequestURL().toString();
        String uri = event.getRequest().getRequestURI();

        String baseUrl = url.substring(0,url.indexOf(uri)+1);
        boolean useLetters = true;
        boolean useNumbers = false;
        String id = RandomStringUtils.random(numberCharsVerificationId, useLetters, useNumbers);
        String verificationId = verificationService.createVerification(username, id);
        String email = event.getUser().getEmail();
        SimpleMailMessage message = new SimpleMailMessage();
        Locale loc = LocaleContextHolder.getLocale();
        switch(action) {
            case "chgpw" :
                message.setSubject(messageSource.getMessage("webshop.password.reset",null, loc));
                message.setText(messageSource.getMessage("password.reset.link",null, loc) +": " + baseUrl +action+"email?id="+verificationId);
                break;
            case "verify" :
                message.setSubject(messageSource.getMessage("webshop.account.verification",null, loc));
                message.setText(messageSource.getMessage("activation.link",null, loc) +": "+ baseUrl + action+"/email?id="+verificationId);
                break;
            case "newAdminVerification" :
                message.setSubject("Confirm new Account");
                message.setText("Activation link: "+ baseUrl + action+"email?id="+verificationId);
                break;

        }
        message.setTo(email);
        mailSender.send(message);

    }
}
