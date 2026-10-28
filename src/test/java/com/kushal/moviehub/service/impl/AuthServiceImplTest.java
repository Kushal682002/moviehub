package com.kushal.moviehub.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import com.kushal.moviehub.dto.LoginRequest;
import com.kushal.moviehub.dto.LoginResponse;
import com.kushal.moviehub.entity.Role;
import com.kushal.moviehub.entity.User;
import com.kushal.moviehub.repository.UserRepository;
import com.kushal.moviehub.security.JwtService;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void login_shouldReturnLoginResponse_whenCredentialsAreValid() {

        // Arrange
        LoginRequest loginRequest =
                new LoginRequest();

        loginRequest.setEmail(
                "kushal@example.com"
        );

        loginRequest.setPassword(
                "password123"
        );

        User user = createUser();

        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        )).thenReturn(authentication);

        when(authentication.getName())
                .thenReturn("kushal@example.com");

        when(authentication.getPrincipal())
                .thenReturn(userDetails);

        when(userRepository.findByEmail(
                "kushal@example.com"
        )).thenReturn(
                java.util.Optional.of(user)
        );

        when(jwtService.generateToken(userDetails))
                .thenReturn("mock-jwt-token");

        // Act
        LoginResponse response =
                authService.login(loginRequest);

        // Assert
        assertNotNull(response);

        assertEquals(
                "Login successful",
                response.getMessage()
        );

        assertEquals(
                "mock-jwt-token",
                response.getToken()
        );

        assertEquals(
                1L,
                response.getUserId()
        );

        assertEquals(
                "Kushal",
                response.getName()
        );

        assertEquals(
                "kushal@example.com",
                response.getEmail()
        );

        assertEquals(
                Role.USER,
                response.getRole()
        );

        verify(authenticationManager)
                .authenticate(
                        any(
                                UsernamePasswordAuthenticationToken.class
                        )
                );

        verify(userRepository)
                .findByEmail(
                        "kushal@example.com"
                );

        verify(jwtService)
                .generateToken(userDetails);
    }

    @Test
    void login_shouldThrowException_whenCredentialsAreInvalid() {

        // Arrange
        LoginRequest loginRequest =
                new LoginRequest();

        loginRequest.setEmail(
                "kushal@example.com"
        );

        loginRequest.setPassword(
                "wrongPassword"
        );

        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        )).thenThrow(
                new BadCredentialsException(
                        "Invalid credentials"
                )
        );

        // Act & Assert
        assertThrows(
                BadCredentialsException.class,
                () -> authService.login(loginRequest)
        );

        verify(authenticationManager)
                .authenticate(
                        any(
                                UsernamePasswordAuthenticationToken.class
                        )
                );
    }

    private User createUser() {

        User user =
                new User();

        user.setId(1L);
        user.setName("Kushal");
        user.setEmail("kushal@example.com");
        user.setPassword("encodedPassword");
        user.setRole(Role.USER);

        return user;
    }
}