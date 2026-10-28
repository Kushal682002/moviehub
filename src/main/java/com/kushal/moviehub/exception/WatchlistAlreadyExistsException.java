package com.kushal.moviehub.exception;

public class WatchlistAlreadyExistsException extends RuntimeException{

	public WatchlistAlreadyExistsException(String message) {
		super(message);
	}
}
