package com.kushal.moviehub.service;

import com.kushal.moviehub.dto.LoginRequest;
import com.kushal.moviehub.dto.LoginResponse;

public interface AuthService {
	
	LoginResponse login(LoginRequest loginRequest);
	
}
