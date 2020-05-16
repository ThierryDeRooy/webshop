package com.webshop.validator;


import com.webshop.model.RecaptchaDto;
import com.webshop.service.GoogleRecaptchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class RecaptchaValidator implements ConstraintValidator<Recaptcha, Object>{
	
	private final HttpServletRequest httpRequest;
	private final GoogleRecaptchaService recaptchaService;
	
	@Override
	public void initialize(Recaptcha constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		String recaptchaResponse = httpRequest.getParameter("g-recaptcha-response");
		if(recaptchaResponse == null || recaptchaResponse.isEmpty()) {
			return false;
		}
		String ip = httpRequest.getRemoteAddr();
		RecaptchaDto recaptchaDto = recaptchaService.verify(ip, recaptchaResponse);
		return recaptchaDto.isSuccess();
	}

}
