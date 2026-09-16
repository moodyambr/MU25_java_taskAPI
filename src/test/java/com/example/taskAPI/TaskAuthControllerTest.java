package com.example.taskAPI;

import com.example.taskAPI.controller.AuthController;
import com.example.taskAPI.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
// Enhetstest för AuthController där vi mockar säkerhets- och JWT-komponenten
public class TaskAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private JwtService jwtService;

    @Test
    // Arrange: skapa giltiga inloggningsuppgifter och mocka autentisering samt JWT-generering.
    // Act: gör POST /login.
    // Assert: status 200 OK och token returneras i JSON.
    void login_shouldReturnToken_forValidCredentials() throws Exception {
        String loginRequest = "{\"username\":\"david\",\"password\":\"123\"}";

        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(jwtService.generateToken("david")).thenReturn("test-token");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-token"));
    }

    @Test
    // Arrange: mocka autentisering att kasta BadCredentialsException.
    // Act: gör POST /login med fel lösenord.
    // Assert: status 401 Unauthorized.
    void login_shouldReturnUnauthorized_forInvalidCredentials() throws Exception {
        String loginRequest = "{\"username\":\"david\",\"password\":\"wrong\"}";

        doThrow(new BadCredentialsException("Bad credentials")).when(authenticationManager).authenticate(any());

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isUnauthorized());
    }
}
