package com.example.taskAPI.controller;

import com.example.taskAPI.model.LoginRequest;
import com.example.taskAPI.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
// Hanterar inloggning och genererar JWT-token för användaren.
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    // Kontrollerar användarnamn/lösenord och returnerar en JWT om det är korrekt.
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            // Verifierar användarens uppgifter mot Spring Security.
            authManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));

            // Skapar en JWT för den inloggade användaren.
            String token = jwtService.generateToken(req.getUsername());

            // Returnerar token till klienten i JSON-format.
            return ResponseEntity.ok(Map.of("token", token));
        } catch (Exception e) {
            // Om autentiseringen misslyckas skickas 401 Unauthorized.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

}
