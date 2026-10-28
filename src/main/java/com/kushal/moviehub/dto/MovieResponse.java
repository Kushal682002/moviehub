package com.kushal.moviehub.dto;

import java.util.Objects;

public class MovieResponse {
	
	private Long id;
	private String title;
	private String description;
	private String genre;
	private Integer releaseYear;
	private Integer duration;
	private String language;
	private Double rating;
	public MovieResponse(Long id, String title, String description, String genre, Integer releaseYear, Integer duration,
			String language, Double rating) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.genre = genre;
		this.releaseYear = releaseYear;
		this.duration = duration;
		this.language = language;
		this.rating = rating;
	}
	public MovieResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public Double getRating() {
		return rating;
	}
	public void setRating(Double rating) {
		this.rating = rating;
	}
	@Override
	public int hashCode() {
		return Objects.hash(description, duration, genre, id, language, rating, releaseYear, title);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MovieResponse other = (MovieResponse) obj;
		return Objects.equals(description, other.description) && Objects.equals(duration, other.duration)
				&& Objects.equals(genre, other.genre) && Objects.equals(id, other.id)
				&& Objects.equals(language, other.language) && Objects.equals(rating, other.rating)
				&& Objects.equals(releaseYear, other.releaseYear) && Objects.equals(title, other.title);
	}
	@Override
	public String toString() {
		return "MovieResponse [id=" + id + ", title=" + title + ", description=" + description + ", genre=" + genre
				+ ", releaseYear=" + releaseYear + ", duration=" + duration + ", language=" + language + ", rating="
				+ rating + "]";
	}
	
	
	
}
