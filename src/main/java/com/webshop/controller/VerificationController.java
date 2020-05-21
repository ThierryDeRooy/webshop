package com.webshop.controller;

import com.webshop.entity.WebUser;
import com.webshop.model.PasswordChange;
import com.webshop.repository.WebUserRepository;
import com.webshop.repository.CustomerRepository;
import com.webshop.service.VerificationService;
import com.webshop.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationService verificationService;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder encoder;
    private final WebUserRepository webUserRepository;

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
                Utils.destroyAllUserSessions(webUser.getUsername());
                model.addAttribute("success", "password.successfully.changed");
                return "changePassword";
            } else {
                model.addAttribute("error", "wrong.password");
                return "changePassword";
            }

        }
    }



//    private Model checkPassword(Model model, String password, String confirmPassword) {
//        PasswordPolicyValidator passwordPolicyValidator = new PasswordPolicyValidator();
//        List<String> errorList = passwordPolicyValidator.getErrors(password);
//        if (!errorList.isEmpty()) {
//            List<String> tempList = new ArrayList<>();
//            for (String item : errorList) {
//                if (item.startsWith("{") && item.endsWith("}"))
//                    tempList.add(item.substring(1,item.length()-1));
//                else
//                    tempList.add(item);
//            }
//            model.addAttribute("error", tempList);
//            return model;
//        } else if (!password.equals(confirmPassword)) {
//            errorList.add("passwords.not.match");
//            model.addAttribute("error", errorList);
//            return model;
//        }
//        return model;
//    }

}
