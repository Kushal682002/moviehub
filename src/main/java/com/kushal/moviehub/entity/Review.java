package com.kushal.moviehub.entity;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reviews")
public class Review {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Integer rating;
	
	private String comment;
	
	@ManyToOne
	@JoinColumn(name = "user_id",nullable = false)
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "movie_id",nullable = false)
	private Movie movie;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public Review(Long id, Integer rating, String comment, User user, Movie movie) {
		super();
		this.id = id;
		this.rating = rating;
		this.comment = comment;
		this.user = user;
		this.movie = movie;
	}

	public Review() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
}
