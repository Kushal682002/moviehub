package com.kushal.moviehub.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.kushal.moviehub.dto.LoginRequest;
import com.kushal.moviehub.dto.LoginResponse;
import com.kushal.moviehub.entity.User;
import com.kushal.moviehub.exception.UserNotFoundException;
import com.kushal.moviehub.repository.UserRepository;
import com.kushal.moviehub.security.JwtService;
import com.kushal.moviehub.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            JwtService jwtService) {

        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getEmail(),
                                loginRequest.getPassword()
                        )
                );

        User user = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User with email "
                                        + loginRequest.getEmail()
                                        + " not found"
                        ));

        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();

        String token =
                jwtService.generateToken(userDetails);

        return new LoginResponse(
                "Login successful",
                token,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}