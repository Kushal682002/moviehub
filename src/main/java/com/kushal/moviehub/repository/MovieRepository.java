package com.kushal.moviehub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kushal.moviehub.entity.Movie;

public interface MovieRepository
        extends JpaRepository<Movie, Long> {

    List<Movie> findByTitleContainingIgnoreCase(
            String title
    );

    List<Movie> findByGenreIgnoreCase(
            String genre
    );

    List<Movie> findByRatingGreaterThanEqual(
            Double rating
    );
}