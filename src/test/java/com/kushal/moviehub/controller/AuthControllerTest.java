package com.kushal.moviehub.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.kushal.moviehub.dto.LoginResponse;
import com.kushal.moviehub.entity.Role;
import com.kushal.moviehub.service.AuthService;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(authController)
                .build();
    }

    @Test
    void login_shouldReturnLoginResponse_whenCredentialsAreValid()
            throws Exception {

        // Arrange
        LoginResponse response =
                new LoginResponse(
                        "Login successful",
                        "mock-jwt-token",
                        1L,
                        "Kushal",
                        "kushal@example.com",
                        Role.USER
                );

        when(authService.login(any()))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email": "kushal@example.com",
                                    "password": "password123"
                                }
                                """)
        )
        .andExpect(status().isOk())
        .andExpect(
                jsonPath("$.message")
                        .value("Login successful")
        )
        .andExpect(
                jsonPath("$.token")
                        .value("mock-jwt-token")
        )
        .andExpect(
                jsonPath("$.userId")
                        .value(1)
        )
        .andExpect(
                jsonPath("$.name")
                        .value("Kushal")
        )
        .andExpect(
                jsonPath("$.email")
                        .value("kushal@example.com")
        )
        .andExpect(
                jsonPath("$.role")
                        .value("USER")
        );

        verify(authService)
                .login(any());
    }
}