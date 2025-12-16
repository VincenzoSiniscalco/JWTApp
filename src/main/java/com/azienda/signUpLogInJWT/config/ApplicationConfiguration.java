package com.azienda.signUpLogInJWT.config;


import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.azienda.signUpLogInJWT.model.User;
import com.azienda.signUpLogInJWT.repository.UserRepository;

@Configuration
public class ApplicationConfiguration {
	private final UserRepository userRepository;
	
	public ApplicationConfiguration(UserRepository userRepository) {
	this.userRepository=userRepository;
	}
	

    @Bean
    public UserDetailsService userDetailsService() {
        return usernameOrEmail -> {
            // prova a trovare l'utente per username
            Optional<User> userOpt = userRepository.findByUsername(usernameOrEmail);

            // se non trovato, prova con l'email
            if (userOpt.isEmpty()) {
                userOpt = userRepository.findByEmail(usernameOrEmail);
            }

            User user = userOpt.orElseThrow(() ->
                    new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail));

            return user; // assuming User implements UserDetails
        };
    }

	
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
		return config.getAuthenticationManager();
	}
	
	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());

        authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
}
