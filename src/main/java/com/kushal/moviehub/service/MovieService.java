package com.kushal.moviehub.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.kushal.moviehub.dto.MovieRequest;
import com.kushal.moviehub.dto.MovieResponse;

public interface MovieService {

    MovieResponse addMovie(MovieRequest movieRequest);

    List<MovieResponse> getAllMovies();

    Page<MovieResponse> getMoviesWithPagination(
            int page,
            int size
    );

    List<MovieResponse> searchByTitle(
            String title
    );

    List<MovieResponse> filterByGenre(
            String genre
    );

    List<MovieResponse> filterByMinimumRating(
            Double rating
    );

    MovieResponse getMovieById(Long id);

    MovieResponse updateMovie(
            Long id,
            MovieRequest movieRequest
    );

    void deleteMovie(Long id);
}