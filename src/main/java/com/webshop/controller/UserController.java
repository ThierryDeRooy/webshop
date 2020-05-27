package com.webshop.controller;

import com.webshop.entity.Customer;
import com.webshop.entity.WebUser;
import com.webshop.event.UserRegistrationEvent;
import com.webshop.model.TotpCode;
import com.webshop.model.UserRegistration;
import com.webshop.repository.WebUserRepository;
import com.webshop.service.CustomerService;
import com.webshop.service.EmailService;
import com.webshop.service.TOTPService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;

@Controller
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);


    private final CustomerService customerService;
    private final PasswordEncoder encoder;
    private final ApplicationEventPublisher eventPublisher;
    private final TOTPService totpService;
    private final WebUserRepository webUserRepository;
    private final EmailService emailService;


    @Value("${capcha.site.key}")
    private String siteKey;


    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("newUser", new UserRegistration());
        model.addAttribute("siteKey", siteKey);
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("newUser") UserRegistration newUser,
                           BindingResult result,
                           Model model,
                           HttpServletRequest request) {
        if(result.hasErrors()) {
            model.addAttribute("siteKey", siteKey);
            return "register";
        }
        WebUser webUser = new WebUser(newUser.getUsername(),
                newUser.getEmail().toLowerCase(),
                encoder.encode(newUser.getPassword()),
                "CUSTOMER",
                false,
                new Date(),
                0);
        Customer customer = new Customer(webUser,
                newUser.getLastname(),
                newUser.getFirstname(),
                newUser.getEmail().toLowerCase());

        webUserRepository.save(webUser);
        customerService.saveUser(customer);
        eventPublisher.publishEvent(new UserRegistrationEvent(webUser, "verify", request));
        return "redirect:register?success";

    }

    @GetMapping("/pwReset")
    public String passwordReset(Model model) {
        return "passwordReset";
    }


    @PostMapping("/pwReset")
    public String passwordResetEmail(@RequestParam("myEmail") String email,
                                     Model model,
                                     HttpServletRequest request) {
        WebUser webUser = null;
        if (email != null) {
            email = email.toLowerCase();
            webUser = webUserRepository.findByEmail(email);
        }
        if (webUser == null) {
            model.addAttribute("email", email);
            return "passwordReset";
        }
        eventPublisher.publishEvent(new UserRegistrationEvent(webUser, "chgpw", request));
        model.addAttribute("success", "mail sent");
        return "passwordReset";
    }
    @GetMapping("/sendUsername")
    public String sendUsername(Model model) {
        return "sendUserName";
    }


    @PostMapping("/sendUsername")
    public String sendUsername(@RequestParam("myEmail") String email,
                                     Model model,
                                     HttpServletRequest request) {
        WebUser webUser = null;
        if (email != null) {
            email = email.toLowerCase();
            webUser = webUserRepository.findByEmail(email);
        }
        if (webUser == null) {
            model.addAttribute("email", email);
            return "sendUserName";
        }
        //send email to customer
        try {
            emailService.sendUsername(webUser);
            model.addAttribute("success", "mail sent");
        } catch (Exception ex) {
            log.error("No email to Customer sent: " + ex.getMessage());
            model.addAttribute("emailNotSent", "mail not sent");
        }


        return "sendUserName";
    }





    @RequestMapping("/login-verified")
    public String loginVerified(Model model) {
        model.addAttribute("verified", true);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }


    @GetMapping("/authenticate-url")
    public String getGoogleAuthenticatorQRUrl(Model model, Principal principal) {
        boolean userHasTotpEnabled = totpService.isTotpEnabled(principal.getName());
        if(!userHasTotpEnabled) {
            model.addAttribute("qrUrl",totpService.generateNewGoogleAuthQrUrl(principal.getName()));
            model.addAttribute("codeDto", new TotpCode());
        }
        model.addAttribute("totpEnabled",userHasTotpEnabled);
        return "account";
    }

//    @GetMapping("/logout")
//    public String logout(HttpServletRequest request, HttpServletResponse response) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if(authentication != null) {
//            new SecurityContextLogoutHandler().logout(request, response, authentication);
//        }
//        return "redirect:/login";
//    }



}
