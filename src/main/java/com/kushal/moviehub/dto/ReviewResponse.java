package com.kushal.moviehub.dto;

public class ReviewResponse {
	
	private Long id;
	private Long userId;
	private String userName;
	private Long movieId;
	private Integer rating;
	private String comment;
	public ReviewResponse(Long id, Long userId, String userName, Long movieId, Integer rating, String comment) {
		super();
		this.id = id;
		this.userId = userId;
		this.userName = userName;
		this.movieId = movieId;
		this.rating = rating;
		this.comment = comment;
	}
	public ReviewResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Long getMovieId() {
		return movieId;
	}
	public void setMovieId(Long movieId) {
		this.movieId = movieId;
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
