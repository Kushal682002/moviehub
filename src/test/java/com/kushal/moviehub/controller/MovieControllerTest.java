package com.kushal.moviehub.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.kushal.moviehub.dto.MovieRequest;
import com.kushal.moviehub.dto.MovieResponse;
import com.kushal.moviehub.exception.GlobalExceptionHandler;
import com.kushal.moviehub.service.MovieService;

@ExtendWith(MockitoExtension.class)
class MovieControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MovieService movieService;

    @InjectMocks
    private MovieController movieController;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(movieController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void addMovie_shouldReturnCreatedMovie() throws Exception {

        // Arrange
        MovieResponse response = createMovieResponse();

        when(movieService.addMovie(any(MovieRequest.class)))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(
                post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Inception",
                                    "description": "A skilled thief enters dreams.",
                                    "genre": "Sci-Fi",
                                    "releaseYear": 2010,
                                    "duration": 148,
                                    "language": "English",
                                    "rating": 8.8
                                }
                                """)
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.title").value("Inception"))
        .andExpect(jsonPath("$.genre").value("Sci-Fi"))
        .andExpect(jsonPath("$.releaseYear").value(2010))
        .andExpect(jsonPath("$.duration").value(148))
        .andExpect(jsonPath("$.language").value("English"))
        .andExpect(jsonPath("$.rating").value(8.8));

        verify(movieService)
                .addMovie(any(MovieRequest.class));
    }

    @Test
    void getAllMovies_shouldReturnAllMovies() throws Exception {

        // Arrange
        MovieResponse movie1 = createMovieResponse();

        MovieResponse movie2 = new MovieResponse();

        movie2.setId(2L);
        movie2.setTitle("Interstellar");
        movie2.setDescription(
                "Explorers travel through a wormhole."
        );
        movie2.setGenre("Sci-Fi");
        movie2.setReleaseYear(2014);
        movie2.setDuration(169);
        movie2.setLanguage("English");
        movie2.setRating(8.7);

        when(movieService.getAllMovies())
                .thenReturn(List.of(movie1, movie2));

        // Act & Assert
        mockMvc.perform(
                get("/api/movies")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(2))
        .andExpect(
                jsonPath("$[0].title")
                        .value("Inception")
        )
        .andExpect(
                jsonPath("$[1].title")
                        .value("Interstellar")
        );

        verify(movieService)
                .getAllMovies();
    }

    @Test
    void getMovieById_shouldReturnMovie_whenMovieExists()
            throws Exception {

        // Arrange
        MovieResponse response =
                createMovieResponse();

        when(movieService.getMovieById(1L))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(
                get("/api/movies/1")
        )
        .andExpect(status().isOk())
        .andExpect(
                jsonPath("$.id")
                        .value(1)
        )
        .andExpect(
                jsonPath("$.title")
                        .value("Inception")
        )
        .andExpect(
                jsonPath("$.description")
                        .value(
                                "A skilled thief enters dreams."
                        )
        )
        .andExpect(
                jsonPath("$.genre")
                        .value("Sci-Fi")
        )
        .andExpect(
                jsonPath("$.releaseYear")
                        .value(2010)
        )
        .andExpect(
                jsonPath("$.duration")
                        .value(148)
        )
        .andExpect(
                jsonPath("$.language")
                        .value("English")
        )
        .andExpect(
                jsonPath("$.rating")
                        .value(8.8)
        );

        verify(movieService)
                .getMovieById(1L);
    }

    @Test
    void updateMovie_shouldReturnUpdatedMovie()
            throws Exception {

        // Arrange
        MovieResponse response =
                createMovieResponse();

        response.setTitle("Inception Updated");
        response.setRating(9.0);

        when(movieService.updateMovie(
                eq(1L),
                any(MovieRequest.class)
        )).thenReturn(response);

        // Act & Assert
        mockMvc.perform(
                put("/api/movies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Inception Updated",
                                    "description": "Updated description.",
                                    "genre": "Sci-Fi",
                                    "releaseYear": 2010,
                                    "duration": 148,
                                    "language": "English",
                                    "rating": 9.0
                                }
                                """)
        )
        .andExpect(status().isOk())
        .andExpect(
                jsonPath("$.id")
                        .value(1)
        )
        .andExpect(
                jsonPath("$.title")
                        .value("Inception Updated")
        )
        .andExpect(
                jsonPath("$.rating")
                        .value(9.0)
        );

        verify(movieService)
                .updateMovie(
                        eq(1L),
                        any(MovieRequest.class)
                );
    }

    @Test
    void deleteMovie_shouldReturnNoContent()
            throws Exception {

        // Arrange
        doNothing()
                .when(movieService)
                .deleteMovie(1L);

        // Act & Assert
        mockMvc.perform(
                delete("/api/movies/1")
        )
        .andExpect(status().isNoContent());

        verify(movieService)
                .deleteMovie(1L);
    }

    private MovieResponse createMovieResponse() {

        MovieResponse response =
                new MovieResponse();

        response.setId(1L);
        response.setTitle("Inception");
        response.setDescription(
                "A skilled thief enters dreams."
        );
        response.setGenre("Sci-Fi");
        response.setReleaseYear(2010);
        response.setDuration(148);
        response.setLanguage("English");
        response.setRating(8.8);

        return response;
    }
}