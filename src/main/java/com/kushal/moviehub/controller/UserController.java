package com.kushal.moviehub.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kushal.moviehub.dto.UserRequest;
import com.kushal.moviehub.dto.UserResponse;
import com.kushal.moviehub.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account. This endpoint is public."
    )
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserRequest userRequest) {

        UserResponse response =
                userService.createUser(userRequest);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "Get all users",
            description = "Returns all registered users. ADMIN access required."
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        return ResponseEntity.ok(
                userService.getAllUsers()
        );
    }

    @Operation(
            summary = "Get user by ID",
            description = "Returns a specific user. ADMIN access required."
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                userService.getUserById(id)
        );
    }

    @Operation(
            summary = "Update user",
            description = "Updates an existing user. ADMIN access required."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest userRequest) {

        return ResponseEntity.ok(
                userService.updateUser(
                        id,
                        userRequest
                )
        );
    }

    @Operation(
            summary = "Delete user",
            description = "Deletes an existing user. ADMIN access required."
    )
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}