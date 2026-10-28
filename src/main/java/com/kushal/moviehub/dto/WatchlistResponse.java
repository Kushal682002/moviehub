package com.kushal.moviehub.dto;

public class WatchlistResponse {
	
	private Long id;
	private Long userId;
	private Long movieId;
	private String movieTitle;
	private String genre;
	private Integer releaseYear;
	private Double rating;
	
	public WatchlistResponse() {
		
	}

	public WatchlistResponse(Long id, Long userId, Long movieId, String movieTitle, String genre, Integer releaseYear,
			Double rating) {
		super();
		this.id = id;
		this.userId = userId;
		this.movieId = movieId;
		this.movieTitle = movieTitle;
		this.genre = genre;
		this.releaseYear = releaseYear;
		this.rating = rating;
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

	public Long getMovieId() {
		return movieId;
	}

	public void setMovieId(Long movieId) {
		this.movieId = movieId;
	}

	public String getMovieTitle() {
		return movieTitle;
	}

	public void setMovieTitle(String movieTitle) {
		this.movieTitle = movieTitle;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public Integer getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(Integer releaseYear) {
		this.releaseYear = releaseYear;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}
	
	
}
