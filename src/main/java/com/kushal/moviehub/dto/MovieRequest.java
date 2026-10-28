package com.kushal.moviehub.dto;

import java.util.Objects;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MovieRequest {
	
	@NotBlank(message = "Title is required")
	private String title;
	
	@NotBlank(message = "Description is required")
	private String description;
	
	@NotBlank(message = "Genre is required")
	private String genre;
	
	@NotNull(message = "Release year is required")
	@Min(value=1888, message = "Release year must be valid")
	private Integer releaseYear;
	
	@NotNull(message = "Duration is required")
	@Min(value = 1,message = "Duration must be greater than 0")
	private Integer duration;
	
	@NotBlank(message = "Language is required")
	private String language;
	
	@NotNull(message = "Rating is required")
	@DecimalMin(value = "0.0",message = "Rating cannot be less than 0")
	@DecimalMax(value = "10.0",message = "Rating cannot be greater than 10")
	private Double rating;
	
	
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
		return Objects.hash(description, duration, genre, language, rating, releaseYear, title);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MovieRequest other = (MovieRequest) obj;
		return Objects.equals(description, other.description) && Objects.equals(duration, other.duration)
				&& Objects.equals(genre, other.genre) && Objects.equals(language, other.language)
				&& Objects.equals(rating, other.rating) && Objects.equals(releaseYear, other.releaseYear)
				&& Objects.equals(title, other.title);
	}
	@Override
	public String toString() {
		return "MovieRequest [title=" + title + ", description=" + description + ", genre=" + genre + ", releaseYear="
				+ releaseYear + ", duration=" + duration + ", language=" + language + ", rating=" + rating + "]";
	}
	public MovieRequest(String title, String description, String genre, Integer releaseYear, Integer duration,
			String language, Double rating) {
		super();
		this.title = title;
		this.description = description;
		this.genre = genre;
		this.releaseYear = releaseYear;
		this.duration = duration;
		this.language = language;
		this.rating = rating;
	}
	public MovieRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
