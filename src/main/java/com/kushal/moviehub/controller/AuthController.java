package com.kushal.moviehub.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kushal.moviehub.dto.LoginRequest;
import com.kushal.moviehub.dto.LoginResponse;
import com.kushal.moviehub.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "User login",
            description = "Authenticates a user and returns a JWT token."
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest loginRequest) {

        return ResponseEntity.ok(
                authService.login(loginRequest)
        );
    }
}