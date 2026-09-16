package com.example.taskAPI.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;


@Service
// Skapar och validerar JWT-token för användare.
public class JwtService {
    // Hemlig nyckel som används för att signera och validera tokens.
    private final String secret = "super_hemlig_nyckel_som_ska_vara_lang";
    private final Key key = Keys.hmacShaKeyFor(secret.getBytes());

    public String generateToken(String username) {
        // Skapar ett token med användarnamnet som ämne och 1 timmes giltighet.
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 timme
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String validateTokenAndGetUserName(String token) {
        // Verifierar token och returnerar användarnamnet inuti token.
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
