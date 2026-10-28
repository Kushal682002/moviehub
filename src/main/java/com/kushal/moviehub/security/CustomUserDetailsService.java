package com.kushal.moviehub.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kushal.moviehub.entity.User;
import com.kushal.moviehub.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	
	private final UserRepository userRepository;
	
	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
		
		User user = userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User with email " + email + " not found"));
		
		return org.springframework.security.core.userdetails.User.withUsername(user.getEmail()).password(user.getPassword()).roles(user.getRole().name()).build();
		
	}
	
}

