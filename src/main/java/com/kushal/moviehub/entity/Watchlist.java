package com.kushal.moviehub.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "watchlists",uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id","movie_id"})})
public class Watchlist {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id",nullable = false)
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "movie_id",nullable = false)
	private Movie movie;
	
	public Watchlist() {
		
	}

	public Watchlist(Long id, User user, Movie movie) {
		super();
		this.id = id;
		this.user = user;
		this.movie = movie;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	
	
}
