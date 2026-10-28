package com.kushal.moviehub.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.kushal.moviehub.dto.UserRequest;
import com.kushal.moviehub.dto.UserResponse;
import com.kushal.moviehub.entity.Role;
import com.kushal.moviehub.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

    @Test
    void createUser_shouldReturnCreatedUser()
            throws Exception {

        // Arrange
        UserResponse response =
                createUserResponse();

        when(userService.createUser(
                any(UserRequest.class)
        )).thenReturn(response);

        // Act & Assert
        mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Kushal",
                                    "email": "kushal@example.com",
                                    "password": "password123"
                                }
                                """)
        )
        .andExpect(status().isCreated())
        .andExpect(
                jsonPath("$.id")
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

        verify(userService)
                .createUser(
                        any(UserRequest.class)
                );
    }

    @Test
    void getAllUsers_shouldReturnAllUsers()
            throws Exception {

        // Arrange
        UserResponse user1 =
                createUserResponse();

        UserResponse user2 =
                new UserResponse();

        user2.setId(2L);
        user2.setName("Rahul");
        user2.setEmail("rahul@example.com");
        user2.setRole(Role.USER);

        when(userService.getAllUsers())
                .thenReturn(
                        List.of(user1, user2)
                );

        // Act & Assert
        mockMvc.perform(
                get("/api/users")
        )
        .andExpect(status().isOk())
        .andExpect(
                jsonPath("$")
                        .isArray()
        )
        .andExpect(
                jsonPath("$.length()")
                        .value(2)
        )
        .andExpect(
                jsonPath("$[0].name")
                        .value("Kushal")
        )
        .andExpect(
                jsonPath("$[0].role")
                        .value("USER")
        )
        .andExpect(
                jsonPath("$[1].name")
                        .value("Rahul")
        )
        .andExpect(
                jsonPath("$[1].role")
                        .value("USER")
        );

        verify(userService)
                .getAllUsers();
    }

    @Test
    void getUserById_shouldReturnUser()
            throws Exception {

        // Arrange
        UserResponse response =
                createUserResponse();

        when(userService.getUserById(1L))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(
                get("/api/users/1")
        )
        .andExpect(status().isOk())
        .andExpect(
                jsonPath("$.id")
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

        verify(userService)
                .getUserById(1L);
    }

    @Test
    void updateUser_shouldReturnUpdatedUser()
            throws Exception {

        // Arrange
        UserResponse response =
                createUserResponse();

        response.setName("Kushal Updated");
        response.setEmail("updated@example.com");

        when(userService.updateUser(
                eq(1L),
                any(UserRequest.class)
        )).thenReturn(response);

        // Act & Assert
        mockMvc.perform(
                put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Kushal Updated",
                                    "email": "updated@example.com",
                                    "password": "newPassword123"
                                }
                                """)
        )
        .andExpect(status().isOk())
        .andExpect(
                jsonPath("$.id")
                        .value(1)
        )
        .andExpect(
                jsonPath("$.name")
                        .value("Kushal Updated")
        )
        .andExpect(
                jsonPath("$.email")
                        .value("updated@example.com")
        )
        .andExpect(
                jsonPath("$.role")
                        .value("USER")
        );

        verify(userService)
                .updateUser(
                        eq(1L),
                        any(UserRequest.class)
                );
    }

    @Test
    void deleteUser_shouldReturnNoContent()
            throws Exception {

        // Arrange
        doNothing()
                .when(userService)
                .deleteUser(1L);

        // Act & Assert
        mockMvc.perform(
                delete("/api/users/1")
        )
        .andExpect(status().isNoContent());

        verify(userService)
                .deleteUser(1L);
    }

    private UserResponse createUserResponse() {

        UserResponse response =
                new UserResponse();

        response.setId(1L);
        response.setName("Kushal");
        response.setEmail("kushal@example.com");
        response.setRole(Role.USER);

        return response;
    }
}