package com.example.taskAPI.security;

import io.jsonwebtoken.Jwt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
// Läser JWT från Authorization-headern och sätter användaren i Spring Security.
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    @Lazy
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Hämtar token från headern, om den finns.
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Tar bort "Bearer " och får bara själva token.
            String token = authHeader.substring(7);

            // Validerar token och hämtar användarnamnet.
            String userName = jwtService.validateTokenAndGetUserName(token);

            if (userName != null) {
                // Laddar användaren från användardatat och sätter autentisering.
                UserDetails user = userDetailsService.loadUserByUsername(userName);
                var authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Fortsätter requesten till nästa filter/endpoint.
        filterChain.doFilter(request, response);
    }
}
