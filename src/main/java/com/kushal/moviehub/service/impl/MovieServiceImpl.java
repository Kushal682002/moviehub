package com.kushal.moviehub.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kushal.moviehub.dto.MovieRequest;
import com.kushal.moviehub.dto.MovieResponse;
import com.kushal.moviehub.entity.Movie;
import com.kushal.moviehub.exception.MovieNotFoundException;
import com.kushal.moviehub.repository.MovieRepository;
import com.kushal.moviehub.service.MovieService;

@Service
public class MovieServiceImpl implements MovieService {

    private static final Logger logger =
            LoggerFactory.getLogger(MovieServiceImpl.class);

    private final MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    @Transactional
    public MovieResponse addMovie(
            MovieRequest movieRequest) {

        logger.info(
                "Adding movie with title: {}",
                movieRequest.getTitle()
        );

        Movie movie = new Movie();

        movie.setTitle(movieRequest.getTitle());
        movie.setDescription(movieRequest.getDescription());
        movie.setGenre(movieRequest.getGenre());
        movie.setReleaseYear(movieRequest.getReleaseYear());
        movie.setDuration(movieRequest.getDuration());
        movie.setLanguage(movieRequest.getLanguage());
        movie.setRating(movieRequest.getRating());

        Movie savedMovie =
                movieRepository.save(movie);

        logger.info(
                "Movie created successfully with id: {}",
                savedMovie.getId()
        );

        return convertToResponse(savedMovie);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieResponse> getAllMovies() {

        logger.info("Fetching all movies");

        return movieRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MovieResponse> getMoviesWithPagination(
            int page,
            int size) {

        logger.info(
                "Fetching movies with pagination - page: {}, size: {}",
                page,
                size
        );

        PageRequest pageRequest =
                PageRequest.of(page, size);

        return movieRepository
                .findAll(pageRequest)
                .map(this::convertToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieResponse> searchByTitle(
            String title) {

        logger.info(
                "Searching movies by title: {}",
                title
        );

        return movieRepository
                .findByTitleContainingIgnoreCase(title)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieResponse> filterByGenre(
            String genre) {

        logger.info(
                "Filtering movies by genre: {}",
                genre
        );

        return movieRepository
                .findByGenreIgnoreCase(genre)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieResponse> filterByMinimumRating(
            Double rating) {

        logger.info(
                "Filtering movies with minimum rating: {}",
                rating
        );

        return movieRepository
                .findByRatingGreaterThanEqual(rating)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MovieResponse getMovieById(Long id) {

        logger.info(
                "Fetching movie with id: {}",
                id
        );

        Movie movie =
                movieRepository.findById(id)
                        .orElseThrow(() -> {

                            logger.warn(
                                    "Movie with id {} not found",
                                    id
                            );

                            return new MovieNotFoundException(
                                    "Movie with id "
                                            + id
                                            + " not found"
                            );
                        });

        return convertToResponse(movie);
    }

    @Override
    @Transactional
    public MovieResponse updateMovie(
            Long id,
            MovieRequest movieRequest) {

        logger.info(
                "Updating movie with id: {}",
                id
        );

        Movie existingMovie =
                movieRepository.findById(id)
                        .orElseThrow(() -> {

                            logger.warn(
                                    "Movie with id {} not found for update",
                                    id
                            );

                            return new MovieNotFoundException(
                                    "Movie with id "
                                            + id
                                            + " not found"
                            );
                        });

        existingMovie.setTitle(
                movieRequest.getTitle()
        );

        existingMovie.setDescription(
                movieRequest.getDescription()
        );

        existingMovie.setGenre(
                movieRequest.getGenre()
        );

        existingMovie.setReleaseYear(
                movieRequest.getReleaseYear()
        );

        existingMovie.setDuration(
                movieRequest.getDuration()
        );

        existingMovie.setLanguage(
                movieRequest.getLanguage()
        );

        existingMovie.setRating(
                movieRequest.getRating()
        );

        Movie updatedMovie =
                movieRepository.save(existingMovie);

        logger.info(
                "Movie with id {} updated successfully",
                id
        );

        return convertToResponse(updatedMovie);
    }

    @Override
    @Transactional
    public void deleteMovie(Long id) {

        logger.info(
                "Deleting movie with id: {}",
                id
        );

        if (!movieRepository.existsById(id)) {

            logger.warn(
                    "Movie with id {} not found for deletion",
                    id
            );

            throw new MovieNotFoundException(
                    "Movie with id "
                            + id
                            + " not found"
            );
        }

        movieRepository.deleteById(id);

        logger.info(
                "Movie with id {} deleted successfully",
                id
        );
    }

    private MovieResponse convertToResponse(
            Movie movie) {

        MovieResponse response =
                new MovieResponse();

        response.setId(movie.getId());

        response.setTitle(
                movie.getTitle()
        );

        response.setDescription(
                movie.getDescription()
        );

        response.setGenre(
                movie.getGenre()
        );

        response.setReleaseYear(
                movie.getReleaseYear()
        );

        response.setDuration(
                movie.getDuration()
        );

        response.setLanguage(
                movie.getLanguage()
        );

        response.setRating(
                movie.getRating()
        );

        return response;
    }
}