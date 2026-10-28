package com.kushal.moviehub.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.kushal.moviehub.dto.UserRequest;
import com.kushal.moviehub.dto.UserResponse;
import com.kushal.moviehub.entity.Role;
import com.kushal.moviehub.entity.User;
import com.kushal.moviehub.exception.UserAlreadyExistsException;
import com.kushal.moviehub.exception.UserNotFoundException;
import com.kushal.moviehub.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_shouldCreateUser_whenEmailDoesNotExist() {

        // Arrange
        UserRequest request = createUserRequest(
                "Kushal",
                "kushal@example.com",
                "password123"
        );

        User savedUser = createUser(
                1L,
                "Kushal",
                "kushal@example.com",
                "encodedPassword",
                Role.USER
        );

        when(userRepository.existsByEmail(
                "kushal@example.com"
        )).thenReturn(false);

        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        // Act
        UserResponse response =
                userService.createUser(request);

        // Assert
        assertEquals(
                1L,
                response.getId()
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

        verify(userRepository)
                .existsByEmail("kushal@example.com");

        verify(passwordEncoder)
                .encode("password123");

        verify(userRepository)
                .save(any(User.class));
    }

    @Test
    void createUser_shouldThrowException_whenEmailAlreadyExists() {

        // Arrange
        UserRequest request = createUserRequest(
                "Kushal",
                "kushal@example.com",
                "password123"
        );

        when(userRepository.existsByEmail(
                "kushal@example.com"
        )).thenReturn(true);

        // Act & Assert
        assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.createUser(request)
        );

        verify(passwordEncoder, never())
                .encode(any(String.class));

        verify(userRepository, never())
                .save(any(User.class));
    }

    @Test
    void getAllUsers_shouldReturnAllUsers() {

        // Arrange
        User user1 = createUser(
                1L,
                "Kushal",
                "kushal@example.com",
                "encodedPassword",
                Role.USER
        );

        User user2 = createUser(
                2L,
                "Admin",
                "admin@example.com",
                "encodedPassword",
                Role.ADMIN
        );

        when(userRepository.findAll())
                .thenReturn(List.of(user1, user2));

        // Act
        List<UserResponse> responses =
                userService.getAllUsers();

        // Assert
        assertEquals(
                2,
                responses.size()
        );

        assertEquals(
                1L,
                responses.get(0).getId()
        );

        assertEquals(
                "Kushal",
                responses.get(0).getName()
        );

        assertEquals(
                Role.USER,
                responses.get(0).getRole()
        );

        assertEquals(
                2L,
                responses.get(1).getId()
        );

        assertEquals(
                "Admin",
                responses.get(1).getName()
        );

        assertEquals(
                Role.ADMIN,
                responses.get(1).getRole()
        );

        verify(userRepository)
                .findAll();
    }

    @Test
    void getMovieById_shouldReturnUser_whenUserExists() {

        // Arrange
        User user = createUser(
                1L,
                "Kushal",
                "kushal@example.com",
                "encodedPassword",
                Role.USER
        );

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        // Act
        UserResponse response =
                userService.getUserById(1L);

        // Assert
        assertEquals(
                1L,
                response.getId()
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

        verify(userRepository)
                .findById(1L);
    }

    @Test
    void getUserById_shouldThrowException_whenUserDoesNotExist() {

        // Arrange
        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserById(99L)
        );

        verify(userRepository)
                .findById(99L);
    }

    @Test
    void updateUser_shouldUpdateUser_whenUserExists() {

        // Arrange
        User existingUser = createUser(
                1L,
                "Old Name",
                "old@example.com",
                "oldEncodedPassword",
                Role.USER
        );

        UserRequest request = createUserRequest(
                "New Name",
                "new@example.com",
                "newPassword"
        );

        User updatedUser = createUser(
                1L,
                "New Name",
                "new@example.com",
                "newEncodedPassword",
                Role.USER
        );

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(existingUser));

        when(userRepository.existsByEmail(
                "new@example.com"
        )).thenReturn(false);

        when(passwordEncoder.encode("newPassword"))
                .thenReturn("newEncodedPassword");

        when(userRepository.save(existingUser))
                .thenReturn(updatedUser);

        // Act
        UserResponse response =
                userService.updateUser(
                        1L,
                        request
                );

        // Assert
        assertEquals(
                1L,
                response.getId()
        );

        assertEquals(
                "New Name",
                response.getName()
        );

        assertEquals(
                "new@example.com",
                response.getEmail()
        );

        assertEquals(
                Role.USER,
                response.getRole()
        );

        verify(userRepository)
                .findById(1L);

        verify(passwordEncoder)
                .encode("newPassword");

        verify(userRepository)
                .save(existingUser);
    }

    @Test
    void updateUser_shouldThrowException_whenUserDoesNotExist() {

        // Arrange
        UserRequest request = createUserRequest(
                "Kushal",
                "kushal@example.com",
                "password123"
        );

        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                UserNotFoundException.class,
                () -> userService.updateUser(
                        99L,
                        request
                )
        );

        verify(passwordEncoder, never())
                .encode(any(String.class));

        verify(userRepository, never())
                .save(any(User.class));
    }

    @Test
    void updateUser_shouldThrowException_whenNewEmailAlreadyExists() {

        // Arrange
        User existingUser = createUser(
                1L,
                "Kushal",
                "old@example.com",
                "oldEncodedPassword",
                Role.USER
        );

        UserRequest request = createUserRequest(
                "Kushal",
                "existing@example.com",
                "password123"
        );

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(existingUser));

        when(userRepository.existsByEmail(
                "existing@example.com"
        )).thenReturn(true);

        // Act & Assert
        assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.updateUser(
                        1L,
                        request
                )
        );

        verify(passwordEncoder, never())
                .encode(any(String.class));

        verify(userRepository, never())
                .save(any(User.class));
    }

    @Test
    void deleteUser_shouldDeleteUser_whenUserExists() {

        // Arrange
        when(userRepository.existsById(1L))
                .thenReturn(true);

        // Act
        userService.deleteUser(1L);

        // Assert
        verify(userRepository)
                .existsById(1L);

        verify(userRepository)
                .deleteById(1L);
    }

    @Test
    void deleteUser_shouldThrowException_whenUserDoesNotExist() {

        // Arrange
        when(userRepository.existsById(99L))
                .thenReturn(false);

        // Act & Assert
        assertThrows(
                UserNotFoundException.class,
                () -> userService.deleteUser(99L)
        );

        verify(userRepository)
                .existsById(99L);

        verify(
                userRepository,
                never()
        ).deleteById(99L);
    }

    @Test
    void updateUser_shouldNotCheckDuplicateEmail_whenEmailRemainsSame() {

        // Arrange
        User existingUser = createUser(
                1L,
                "Kushal",
                "kushal@example.com",
                "oldEncodedPassword",
                Role.USER
        );

        UserRequest request = createUserRequest(
                "Updated Kushal",
                "kushal@example.com",
                "newPassword"
        );

        User updatedUser = createUser(
                1L,
                "Updated Kushal",
                "kushal@example.com",
                "newEncodedPassword",
                Role.USER
        );

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(existingUser));

        when(passwordEncoder.encode("newPassword"))
                .thenReturn("newEncodedPassword");

        when(userRepository.save(existingUser))
                .thenReturn(updatedUser);

        // Act
        UserResponse response =
                userService.updateUser(
                        1L,
                        request
                );

        // Assert
        assertEquals(
                "Updated Kushal",
                response.getName()
        );

        assertEquals(
                "kushal@example.com",
                response.getEmail()
        );

        verify(userRepository)
                .findById(1L);

        verify(passwordEncoder)
                .encode("newPassword");

        verify(userRepository)
                .save(existingUser);

        verify(
                userRepository,
                never()
        ).existsByEmail("kushal@example.com");
    }

    private UserRequest createUserRequest(
            String name,
            String email,
            String password) {

        UserRequest request =
                new UserRequest();

        request.setName(name);
        request.setEmail(email);
        request.setPassword(password);

        return request;
    }

    private User createUser(
            Long id,
            String name,
            String email,
            String password,
            Role role) {

        User user =
                new User();

        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);

        return user;
    }
}