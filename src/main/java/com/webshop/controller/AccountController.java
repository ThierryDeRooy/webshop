package com.webshop.controller;

import com.webshop.exception.InvalidTOTPVerificationCode;
import com.webshop.service.TOTPService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final TOTPService totpService;

    @GetMapping("/account")
    public String getAccount(Model model, Principal principal) {
        boolean userHasTotpEnabled = totpService.isTotpEnabled(principal.getName());
        model.addAttribute("totpEnabled",userHasTotpEnabled);
        return "account";
    }

    @GetMapping("/setup-totp")
    public String getGoogleAuthenticatorQRUrl(Model model, Principal principal) {
        boolean userHasTotpEnabled = totpService.isTotpEnabled(principal.getName());
        if(!userHasTotpEnabled) {
            model.addAttribute("qrUrl",totpService.generateNewGoogleAuthQrUrl(principal.getName()));
        }
        model.addAttribute("totpEnabled",userHasTotpEnabled);
        return "account";
    }

    @PostMapping("/confirm-totp")
    public String confirmGoogleAuthenticatorSetup(Model model, Principal principal, @RequestParam("code") String codeDto) {
        boolean userHasTotpEnabled = totpService.isTotpEnabled(principal.getName());
        if(!userHasTotpEnabled) {
            totpService.enableTOTPForUser(principal.getName(), Integer.valueOf(codeDto));
            model.addAttribute("totpEnabled",true);
        }
        return "account";
    }

    @ExceptionHandler(InvalidTOTPVerificationCode.class)
    public String handleInvalidTOTPVerificationCode(Model model, Principal principal) {
        boolean userHasTotpEnabled = totpService.isTotpEnabled(principal.getName());
        model.addAttribute("totpEnabled",userHasTotpEnabled);
        model.addAttribute("confirmError",true);
        return "account";
    }

}