package com.example.taskAPI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
// Testklass som verifierar att login-endpointet fungerar för både giltiga och ogiltiga inloggningar.
public class AuthConrollerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    // Arrange: användaren skickar giltiga inloggningsuppgifter.
    // Act: gör POST /login.
    // Assert: status 200 OK och att JWT-token finns i responsen.
    void login_shouldReturnToken_forValidCredentials() throws Exception {
        String loginRequest = "{\"username\":\"david\",\"password\":\"123\"}";

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    // Arrange: användaren skickar fel lösenord.
    // Act: gör POST /login.
    // Assert: status 401 Unauthorized.
    void login_shouldReturnUnauthorized_forInvalidCredentials() throws Exception {
        String loginRequest = "{\"username\":\"david\",\"password\":\"wrong-password\"}";

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isUnauthorized());
    }
}
