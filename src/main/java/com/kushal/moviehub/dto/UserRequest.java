package com.kushal.moviehub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserRequest {
	
	@NotBlank(message = "Name is required")
	private String name;
	
	@NotBlank(message = "Email is required")
	@Email(message = "Email must be valid")
	private String email;
	
	@NotBlank(message = "Password is required")
	private String password;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserRequest(@NotBlank(message = "Name is required") String name,
			@NotBlank(message = "Email is required") @Email(message = "Email must be valid") String email,
			@NotBlank(message = "Password is required") String password) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public UserRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
