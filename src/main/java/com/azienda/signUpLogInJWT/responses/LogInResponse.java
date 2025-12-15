package com.azienda.signUpLogInJWT.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogInResponse {
	private String token;
	private long expiresIn;
	public LogInResponse(String token, long expiresIn) {
		super();
		this.token = token;
		this.expiresIn = expiresIn;
	}
	
	
}
