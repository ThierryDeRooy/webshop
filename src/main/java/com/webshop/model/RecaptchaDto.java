package com.webshop.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class RecaptchaDto {

	private boolean success;
	private List<String> errors;
	
}
