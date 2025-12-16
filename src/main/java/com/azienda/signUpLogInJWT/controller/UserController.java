package com.azienda.signUpLogInJWT.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azienda.signUpLogInJWT.model.User;
import com.azienda.signUpLogInJWT.repository.UserRepository;
import com.azienda.signUpLogInJWT.service.UserService;

@RequestMapping("/users")
@RestController
public class UserController {
	private final UserService userService;
	private final UserRepository userRepository;
	
	public UserController(UserService userService, UserRepository userRepository) {
		this.userService=userService;
		this.userRepository =userRepository;
	}
	
	@GetMapping("/me")
	public ResponseEntity<User> authenticatedUser(){
		Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		return ResponseEntity.ok(userRepository.findByEmail(email).orElse(null));
	}
	
	@GetMapping("/")
	public ResponseEntity<List<User>> allUsers(){
		List<User> users= userService.allUsers();
		return ResponseEntity.ok(users);
	}
}
