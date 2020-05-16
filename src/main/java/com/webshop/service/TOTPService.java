package com.webshop.service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import com.webshop.entity.TOTPDetails;
import com.webshop.entity.WebUser;
import com.webshop.exception.InvalidTOTPVerificationCode;
import com.webshop.repository.TOTPRepository;
import com.webshop.repository.WebUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:application.properties")
public class TOTPService {

    private final GoogleAuthenticator googleAuth = new GoogleAuthenticator();
    private final TOTPRepository totpRepository;
    private final WebUserRepository webUserRepository;

    @Value("${google.authenticator.issuer}")
    private String issuer;


    public TOTPService(TOTPRepository totpRepository, WebUserRepository  webUserRepository) {
        this.totpRepository=totpRepository;
        this. webUserRepository= webUserRepository;
    }

    public String generateNewGoogleAuthQrUrl(String username) {
        GoogleAuthenticatorKey authKey = googleAuth.createCredentials();
        String secret = authKey.getKey();
        totpRepository.deleteByUsername(username);
        totpRepository.save(new TOTPDetails(username, secret));
        return GoogleAuthenticatorQRGenerator.getOtpAuthURL(issuer, username, authKey);
    }

    public boolean isTotpEnabled(String username) {
        return  webUserRepository.findByUsername(username).isTotpEnabled();
    }

    public void enableTOTPForUser(String username, int code) {
        if(!verifyCode(username, code)) {
            throw new InvalidTOTPVerificationCode("Verification code is Invalid");
        }
        WebUser user =  webUserRepository.findByUsername(username);
        user.setTotpEnabled(true);
        webUserRepository.save(user);
    }

    public boolean verifyCode(String username, int code) {
        TOTPDetails totpDetails = totpRepository.findByUsername(username);
        return googleAuth.authorize(totpDetails.getSecret(), code);
    }

}
