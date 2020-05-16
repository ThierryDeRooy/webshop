package com.webshop.service;

import com.webshop.model.RecaptchaDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@PropertySource("classpath:application.properties")
public class GoogleRecaptchaService {

	private static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify"
			+ "?secret={secret}&remoteip={remoteip}&response={response}";
	private final RestTemplate restTemplate;

	@Value("${capcha.secret.key}")
	private String secretKey;

//	private final String secretKey = "6Ldj9s0UAAAAAFxw22K_SOsLV0InoLc0h21FBp2h";
	
	public GoogleRecaptchaService(RestTemplate restTemplate) {
		this.restTemplate=restTemplate;
	}
	
	public RecaptchaDto verify(String ip, String recaptchaResponse) {
		Map<String, String> request = new HashMap<>();
		request.put("remoteip", ip);
		request.put("secret", secretKey);
		request.put("response", recaptchaResponse);
		ResponseEntity<Map> response = restTemplate.getForEntity(VERIFY_URL, Map.class, request);
		Map<String, Object> body = response.getBody();
		boolean success = (Boolean)body.get("success");
		RecaptchaDto recaptchaDto = new RecaptchaDto();
		recaptchaDto.setSuccess(success);
		if(!success) {
		      recaptchaDto.setErrors((List)body.get("error-codes"));
		}
		return recaptchaDto;
	}
	
}
