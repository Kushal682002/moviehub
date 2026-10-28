package com.kushal.moviehub.service;

import java.util.List;

import com.kushal.moviehub.dto.UserRequest;
import com.kushal.moviehub.dto.UserResponse;

public interface UserService {
	
	UserResponse createUser(UserRequest userRequest);
	
	List<UserResponse> getAllUsers();
	
	UserResponse getUserById(Long id);
	
	UserResponse updateUser(Long id,UserRequest userRequest);
	
	void deleteUser(Long id);
	
}
