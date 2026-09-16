package com.example.taskAPI.model;

// DTO för inloggningsförfrågan från klienten.
public class LoginRequest {
    // Användarnamn som skickas i JSON.
    private String username;
    // Lösenord som skickas i JSON.
    private String password;

    public LoginRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
