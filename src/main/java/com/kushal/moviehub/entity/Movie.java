package com.kushal.moviehub.entity;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "movies")
public class Movie {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String description;
	private String genre;
	private Integer releaseYear;
	private Integer duration;
	private String language;
	private Double rating;
	
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
		Movie other = (Movie) obj;
		return Objects.equals(description, other.description) && Objects.equals(duration, other.duration)
				&& Objects.equals(genre, other.genre) && Objects.equals(id, other.id)
				&& Objects.equals(language, other.language) && Objects.equals(rating, other.rating)
				&& Objects.equals(releaseYear, other.releaseYear) && Objects.equals(title, other.title);
	}
	public Movie(Long id, String title, String description, String genre, Integer releaseYear, Integer duration,
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
	@Override
	public String toString() {
		return "Movie [id=" + id + ", title=" + title + ", description=" + description + ", genre=" + genre
				+ ", releaseYear=" + releaseYear + ", duration=" + duration + ", language=" + language + ", rating="
				+ rating + "]";
	}
	public Movie() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
