package com.kushal.moviehub.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReviewRequest {
	
	@NotNull(message = "User Id is required")
	private Long userId;
	
	@NotNull(message = "Rating is required")
	@Min(value = 1,message = "Rating must be at least 1")
	@Max(value = 10,message = "Rating cannot be greater than 10")
	private Integer rating;
	
	@NotBlank(message = "Comment is required")
	private String comment;

	public ReviewRequest(@NotNull(message = "User Id is required") Long userId,
			@NotNull(message = "Rating is required") @Min(value = 1, message = "Rating must be at least 1") @Max(value = 10, message = "Rating cannot be greater than 10") Integer rating,
			@NotBlank(message = "Comment is required") String comment) {
		super();
		this.userId = userId;
		this.rating = rating;
		this.comment = comment;
	}

	public ReviewRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
	
}
