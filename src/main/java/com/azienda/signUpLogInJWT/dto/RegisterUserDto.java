package com.azienda.signUpLogInJWT.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDto {
	private String email;
	private String password;
	private String username;
}
