package com.webshop.controller;

import com.webshop.config.HttpSessionConfig;
import com.webshop.entity.WebUser;
import com.webshop.model.PasswordChange;
import com.webshop.repository.WebUserRepository;
import com.webshop.repository.CustomerRepository;
import com.webshop.service.VerificationService;
import com.webshop.userDetails.AuthenticationSuccessHandlerImpl;
import com.webshop.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationService verificationService;
    private final PasswordEncoder encoder;
    private final WebUserRepository webUserRepository;
    private final PersistentTokenRepository persistentTokenRepository;
    private final SessionRegistry sessionRegistry;

    @GetMapping("/verify/email")
    public String verifyEmail(@RequestParam String id) {
        String username = verificationService.getUsernameForId(id);
        if(username != null) {
            WebUser webUser = webUserRepository.findByUsername(username);
            webUser.setVerified(true);
            webUserRepository.save(webUser);
            verificationService.deleteVerification(id);
        }
        return "redirect:/login-verified";
    }
    @GetMapping("/chgpwemail")
    public String verifyEmailPw(@RequestParam String id, Model model) {
        String username = verificationService.getUsernameForId(id);
        if(username != null) {
            model.addAttribute("id", id);
            model.addAttribute("newPassword", new PasswordChange());
            return "changePasswordNoOld";
        }
        return "redirect:/login-verified";
    }

    @GetMapping("/newAdminVerificationemail")
    public String newAdminPw(@RequestParam String id, Model model) {
        String username = verificationService.getUsernameForId(id);
        if(username != null) {
            WebUser webuser = webUserRepository.findByUsername(username);
            if (webuser!=null) {
                webuser.setVerified(true);
                webUserRepository.save(webuser);
                model.addAttribute("id", id);
                model.addAttribute("newPassword", new PasswordChange());
                return "changePasswordNoOld";
            }
         }
        return "redirect:/login-verified";
    }

    @PostMapping("/changePasswordNoOld")
    public String changePasswordByEmail(@RequestParam("id") String id,
                                        @Valid @ModelAttribute("newPassword") PasswordChange pwChange,
                                        BindingResult result,
                                        Model model) {
        model.addAttribute("id", id);
        if(result.hasErrors()) {
            return "changePasswordNoOld";
        }
        String username = verificationService.getUsernameForId(id);
        if(username != null) {
            WebUser webUser = webUserRepository.findByUsername(username);
            webUser.setPassword(encoder.encode(pwChange.getPassword()));
            webUserRepository.save(webUser);
            verificationService.deleteVerification(id);
            model.addAttribute("success", "password.successfully.changed");
            return "changePasswordNoOld";
        }
        return "redirect:/login-verified";

    }

    @GetMapping("/changePassword")
    public  String changePassword(HttpServletRequest request, Model model) {
        model.addAttribute("newPassword", new PasswordChange());
        return "changePassword";
    }

    @PostMapping("/changePassword")
    public String changePassword(@Valid @ModelAttribute("newPassword") PasswordChange newPassword,
                                 BindingResult result,
                                 Model model,
                                 HttpServletRequest request) {
        if(result.hasErrors()) {
            return "changePassword";
        }
        String userName = request.getUserPrincipal().getName();
        WebUser webUser = webUserRepository.findByUsername(userName);
        if (webUser == null) {
            model.addAttribute("error", "wrong.password");
            return "changePassword";
        } else {
//                boolean checkPw = BCrypt.checkpw(oldPassword, adminRepository.findByUsername(userName).getPassword());
            String oldPassword = newPassword.getOldPassword();
            boolean checkPw = oldPassword != null && encoder.matches(oldPassword, webUser.getPassword());
            if (checkPw) {
//                model = checkPassword(model, password, confirmPassword);
//                if (model.containsAttribute("error"))
//                    return "changePassword";
                webUser.setPassword(encoder.encode(newPassword.getPassword()));
                webUserRepository.save(webUser);
//                Utils.destroyAllUserSessions(webUser.getUsername());
                destroyAllUserSessions();
                model.addAttribute("success", "password.successfully.changed");
                SecurityContextHolder.clearContext();
                persistentTokenRepository.removeUserTokens(webUser.getUsername());
                return "changePassword";
            } else {
                model.addAttribute("error", "wrong.password");
                return "changePassword";
            }

        }
    }

    private void destroyAllUserSessions() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
        for (SessionInformation session : sessions) {
            session.expireNow();
        }
        if (principal instanceof UserDetails) {
            String user = ((UserDetails) principal).getUsername();
            for (HttpSession session : HttpSessionConfig.getActiveSessions()) {
                if (user.equals(session.getAttribute(AuthenticationSuccessHandlerImpl.USERNAME)))
                    session.invalidate();
            }
        }
    }

}
