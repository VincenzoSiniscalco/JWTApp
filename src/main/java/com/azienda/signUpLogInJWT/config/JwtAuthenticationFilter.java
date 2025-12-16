package com.azienda.signUpLogInJWT.config;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.azienda.signUpLogInJWT.repository.UserRepository;
import com.azienda.signUpLogInJWT.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository; 

    public JwtAuthenticationFilter(JwtService jwtService,
                                   UserDetailsService userDetailsService,
                                   UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.info("Nessun header Authorization valido trovato, salto filtro.");
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(jwt);
        logger.info("Token ricevuto: " + jwt);
        logger.info("Username/email estratto dal token: " + userEmail);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Authentication corrente nel SecurityContext: " + authentication);

        try {
            // LOG degli utenti presenti nel DB
            logger.info("Utenti presenti nel DB:");
            userRepository.findAll().forEach(u -> logger.info(" - " + u.getEmail())); // o getUsername()

            if (userEmail != null && authentication == null) {
                logger.info("Caricamento UserDetails per username/email: " + userEmail);

                UserDetails userDetails;
                try {
                    userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                } catch (Exception e) {
                    logger.warn("UserDetails non trovato per: " + userEmail);
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Utente non trovato");
                    return;
                }

                logger.info("UserDetails caricati: " + userDetails);
                boolean valid = jwtService.isTokenValid(jwt, userDetails);
                logger.info("Token valido? " + valid);

                if (!valid) {
                    logger.warn("Token non valido per l'utente: " + userEmail);
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token non valido");
                    return;
                }

                if (!userDetails.isEnabled()) {
                    logger.warn("Utente non abilitato: " + userEmail);
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Utente non abilitato");
                    return;
                }

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null,
                        userDetails.getAuthorities().isEmpty() 
                                ? List.of(new SimpleGrantedAuthority("ROLE_USER"))
                                : userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.info("Authentication impostata con successo per: " + userEmail);
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            logger.error("Errore durante l'autenticazione JWT: ", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token JWT non valido");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/auth/");
    }
}
