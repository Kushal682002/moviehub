package com.kushal.moviehub.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kushal.moviehub.dto.MovieRequest;
import com.kushal.moviehub.dto.MovieResponse;
import com.kushal.moviehub.entity.Movie;
import com.kushal.moviehub.exception.MovieNotFoundException;
import com.kushal.moviehub.repository.MovieRepository;

@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieServiceImpl movieService;

    @Test
    void addMovie_shouldReturnMovieResponse_whenMovieIsSaved() {

        // Arrange
        MovieRequest movieRequest = new MovieRequest();

        movieRequest.setTitle("Inception");
        movieRequest.setDescription(
                "A skilled thief enters dreams."
        );
        movieRequest.setGenre("Sci-Fi");
        movieRequest.setReleaseYear(2010);
        movieRequest.setDuration(148);
        movieRequest.setLanguage("English");
        movieRequest.setRating(8.8);

        Movie savedMovie = new Movie();

        savedMovie.setId(1L);
        savedMovie.setTitle("Inception");
        savedMovie.setDescription(
                "A skilled thief enters dreams."
        );
        savedMovie.setGenre("Sci-Fi");
        savedMovie.setReleaseYear(2010);
        savedMovie.setDuration(148);
        savedMovie.setLanguage("English");
        savedMovie.setRating(8.8);

        when(movieRepository.save(
                org.mockito.ArgumentMatchers.any(Movie.class)
        )).thenReturn(savedMovie);

        // Act
        MovieResponse response =
                movieService.addMovie(movieRequest);

        // Assert
        assertEquals(
                1L,
                response.getId()
        );

        assertEquals(
                "Inception",
                response.getTitle()
        );

        assertEquals(
                "A skilled thief enters dreams.",
                response.getDescription()
        );

        assertEquals(
                "Sci-Fi",
                response.getGenre()
        );

        assertEquals(
                2010,
                response.getReleaseYear()
        );

        assertEquals(
                148,
                response.getDuration()
        );

        assertEquals(
                "English",
                response.getLanguage()
        );

        assertEquals(
                8.8,
                response.getRating()
        );

        verify(movieRepository)
                .save(
                        org.mockito.ArgumentMatchers.any(Movie.class)
                );
    }

    @Test
    void getAllMovies_shouldReturnAllMovies() {

        // Arrange
        Movie movie1 = new Movie();

        movie1.setId(1L);
        movie1.setTitle("Inception");
        movie1.setDescription(
                "A skilled thief enters dreams."
        );
        movie1.setGenre("Sci-Fi");
        movie1.setReleaseYear(2010);
        movie1.setDuration(148);
        movie1.setLanguage("English");
        movie1.setRating(8.8);

        Movie movie2 = new Movie();

        movie2.setId(2L);
        movie2.setTitle("Interstellar");
        movie2.setDescription(
                "Explorers travel through space."
        );
        movie2.setGenre("Sci-Fi");
        movie2.setReleaseYear(2014);
        movie2.setDuration(169);
        movie2.setLanguage("English");
        movie2.setRating(8.7);

        when(movieRepository.findAll())
                .thenReturn(List.of(movie1, movie2));

        // Act
        List<MovieResponse> responses =
                movieService.getAllMovies();

        // Assert
        assertEquals(
                2,
                responses.size()
        );

        assertEquals(
                1L,
                responses.get(0).getId()
        );

        assertEquals(
                "Inception",
                responses.get(0).getTitle()
        );

        assertEquals(
                2L,
                responses.get(1).getId()
        );

        assertEquals(
                "Interstellar",
                responses.get(1).getTitle()
        );

        verify(movieRepository)
                .findAll();
    }

    @Test
    void getMovieById_shouldReturnMovie_whenMovieExists() {

        // Arrange
        Movie movie = new Movie();

        movie.setId(1L);
        movie.setTitle("Inception");
        movie.setDescription(
                "A skilled thief enters dreams."
        );
        movie.setGenre("Sci-Fi");
        movie.setReleaseYear(2010);
        movie.setDuration(148);
        movie.setLanguage("English");
        movie.setRating(8.8);

        when(movieRepository.findById(1L))
                .thenReturn(Optional.of(movie));

        // Act
        MovieResponse response =
                movieService.getMovieById(1L);

        // Assert
        assertEquals(
                1L,
                response.getId()
        );

        assertEquals(
                "Inception",
                response.getTitle()
        );

        assertEquals(
                "A skilled thief enters dreams.",
                response.getDescription()
        );

        assertEquals(
                "Sci-Fi",
                response.getGenre()
        );

        assertEquals(
                2010,
                response.getReleaseYear()
        );

        assertEquals(
                148,
                response.getDuration()
        );

        assertEquals(
                "English",
                response.getLanguage()
        );

        assertEquals(
                8.8,
                response.getRating()
        );

        verify(movieRepository)
                .findById(1L);
    }

    @Test
    void getMovieById_shouldThrowException_whenMovieDoesNotExist() {

        // Arrange
        when(movieRepository.findById(1L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                MovieNotFoundException.class,
                () -> movieService.getMovieById(1L)
        );

        verify(movieRepository)
                .findById(1L);
    }

    @Test
    void updateMovie_shouldReturnUpdatedMovie_whenMovieExists() {

        // Arrange
        MovieRequest movieRequest = new MovieRequest();

        movieRequest.setTitle("Inception Updated");
        movieRequest.setDescription(
                "Updated movie description."
        );
        movieRequest.setGenre("Science Fiction");
        movieRequest.setReleaseYear(2010);
        movieRequest.setDuration(150);
        movieRequest.setLanguage("English");
        movieRequest.setRating(9.0);

        Movie existingMovie = new Movie();

        existingMovie.setId(1L);
        existingMovie.setTitle("Inception");
        existingMovie.setDescription(
                "A skilled thief enters dreams."
        );
        existingMovie.setGenre("Sci-Fi");
        existingMovie.setReleaseYear(2010);
        existingMovie.setDuration(148);
        existingMovie.setLanguage("English");
        existingMovie.setRating(8.8);

        when(movieRepository.findById(1L))
                .thenReturn(Optional.of(existingMovie));

        when(movieRepository.save(existingMovie))
                .thenReturn(existingMovie);

        // Act
        MovieResponse response =
                movieService.updateMovie(
                        1L,
                        movieRequest
                );

        // Assert
        assertEquals(
                1L,
                response.getId()
        );

        assertEquals(
                "Inception Updated",
                response.getTitle()
        );

        assertEquals(
                "Updated movie description.",
                response.getDescription()
        );

        assertEquals(
                "Science Fiction",
                response.getGenre()
        );

        assertEquals(
                2010,
                response.getReleaseYear()
        );

        assertEquals(
                150,
                response.getDuration()
        );

        assertEquals(
                "English",
                response.getLanguage()
        );

        assertEquals(
                9.0,
                response.getRating()
        );

        verify(movieRepository)
                .findById(1L);

        verify(movieRepository)
                .save(existingMovie);
    }

    @Test
    void updateMovie_shouldThrowException_whenMovieDoesNotExist() {

        // Arrange
        MovieRequest movieRequest = new MovieRequest();

        movieRequest.setTitle("Inception Updated");
        movieRequest.setDescription(
                "Updated movie description."
        );
        movieRequest.setGenre("Sci-Fi");
        movieRequest.setReleaseYear(2010);
        movieRequest.setDuration(150);
        movieRequest.setLanguage("English");
        movieRequest.setRating(9.0);

        when(movieRepository.findById(1L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                MovieNotFoundException.class,
                () -> movieService.updateMovie(
                        1L,
                        movieRequest
                )
        );

        verify(movieRepository)
                .findById(1L);
    }

    @Test
    void deleteMovie_shouldDeleteMovie_whenMovieExists() {

        // Arrange
        when(movieRepository.existsById(1L))
                .thenReturn(true);

        // Act
        movieService.deleteMovie(1L);

        // Assert
        verify(movieRepository)
                .existsById(1L);

        verify(movieRepository)
                .deleteById(1L);
    }

    @Test
    void deleteMovie_shouldThrowException_whenMovieDoesNotExist() {

        // Arrange
        when(movieRepository.existsById(1L))
                .thenReturn(false);

        // Act & Assert
        assertThrows(
                MovieNotFoundException.class,
                () -> movieService.deleteMovie(1L)
        );

        verify(movieRepository)
                .existsById(1L);
    }
}