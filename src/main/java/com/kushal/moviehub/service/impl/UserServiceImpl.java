package com.kushal.moviehub.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kushal.moviehub.dto.UserRequest;
import com.kushal.moviehub.dto.UserResponse;
import com.kushal.moviehub.entity.Role;
import com.kushal.moviehub.entity.User;
import com.kushal.moviehub.exception.UserAlreadyExistsException;
import com.kushal.moviehub.exception.UserNotFoundException;
import com.kushal.moviehub.repository.UserRepository;
import com.kushal.moviehub.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) {

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new UserAlreadyExistsException(
                    "User with email "
                            + userRequest.getEmail()
                            + " already exists"
            );
        }

        User user = new User();

        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());

        user.setPassword(
                passwordEncoder.encode(userRequest.getPassword())
        );

        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);

        return convertToResponse(savedUser);
    }

    @Override
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User with id "
                                        + id
                                        + " not found"
                        ));

        return convertToResponse(user);
    }

    @Override
    public UserResponse updateUser(
            Long id,
            UserRequest userRequest) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User with id "
                                        + id
                                        + " not found"
                        ));

        if (!existingUser.getEmail().equals(userRequest.getEmail())
                && userRepository.existsByEmail(
                        userRequest.getEmail())) {

            throw new UserAlreadyExistsException(
                    "User with email "
                            + userRequest.getEmail()
                            + " already exists"
            );
        }

        existingUser.setName(userRequest.getName());
        existingUser.setEmail(userRequest.getEmail());

        existingUser.setPassword(
                passwordEncoder.encode(
                        userRequest.getPassword()
                )
        );

        User updatedUser = userRepository.save(existingUser);

        return convertToResponse(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {

        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(
                    "User with id "
                            + id
                            + " not found"
            );
        }

        userRepository.deleteById(id);
    }

    private UserResponse convertToResponse(User user) {

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        return response;
    }
}