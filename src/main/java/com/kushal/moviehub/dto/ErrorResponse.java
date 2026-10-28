package com.kushal.moviehub.dto;

import java.util.Map;
import java.util.Objects;

public class ErrorResponse {
	
	private int status;
	private String message;
	private Map<String,String> errors;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Map<String, String> getErrors() {
		return errors;
	}
	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}
	@Override
	public int hashCode() {
		return Objects.hash(errors, message, status);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ErrorResponse other = (ErrorResponse) obj;
		return Objects.equals(errors, other.errors) && Objects.equals(message, other.message) && status == other.status;
	}
	@Override
	public String toString() {
		return "ErrorResponse [status=" + status + ", message=" + message + ", errors=" + errors + "]";
	}
	public ErrorResponse(int status, String message, Map<String, String> errors) {
		super();
		this.status = status;
		this.message = message;
		this.errors = errors;
	}
	public ErrorResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ErrorResponse(int status,String message) {
		this.status = status;
		this.message = message;
	}
	
	
	
	
}

