package com.azienda.signUpLogInJWT.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.azienda.signUpLogInJWT.dto.LoginUserDto;
import com.azienda.signUpLogInJWT.dto.RegisterUserDto;
import com.azienda.signUpLogInJWT.dto.VerifyUserDto;
import com.azienda.signUpLogInJWT.model.User;
import com.azienda.signUpLogInJWT.responses.LogInResponse;
import com.azienda.signUpLogInJWT.service.AuthenticationService;
import com.azienda.signUpLogInJWT.service.JwtService;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
	private final JwtService jwtService;
	private final AuthenticationService authenticationService;
	
	public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
		this.jwtService= jwtService;
		this.authenticationService= authenticationService;
	}
	
	@PostMapping("/signup")
	public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto){
		User registeredUser= authenticationService.signUp(registerUserDto);
		return ResponseEntity.ok(registeredUser);
	}
	@PostMapping("/login")
	public ResponseEntity<LogInResponse> authenticate(@RequestBody LoginUserDto loginUserDto){
		User authenticatedUser = authenticationService.authenticate(loginUserDto);
		String jwtToken = jwtService.generateToken(authenticatedUser);
		LogInResponse loginResponse= new LogInResponse(jwtToken, jwtService.getExpirationTime());
		return ResponseEntity.ok(loginResponse);
	}
	@PostMapping("/verify")
	public ResponseEntity<?> verifyUser(@RequestBody VerifyUserDto verifyUserDto){
		try {
			authenticationService.verifyUser(verifyUserDto);
			return ResponseEntity.ok("Account verified successfully!");
			
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	@PostMapping("/resend")
	public ResponseEntity<?> resendVerificationCode(@RequestParam String email){
		try {
			authenticationService.resendVerificationCode(email);
			return ResponseEntity.ok("Verification code sent.");
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
