package com.kushal.moviehub.exception;

public class AccessDeniedException extends RuntimeException{
	
	public AccessDeniedException(String message) {
		super(message);
	}
}
