package com.kushal.moviehub.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kushal.moviehub.dto.MovieRequest;
import com.kushal.moviehub.dto.MovieResponse;
import com.kushal.moviehub.service.MovieService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @Operation(
            summary = "Add a new movie",
            description = "Creates a new movie. ADMIN access required."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<MovieResponse> addMovie(
            @Valid @RequestBody MovieRequest movieRequest) {

        MovieResponse savedMovie =
                movieService.addMovie(movieRequest);

        return new ResponseEntity<>(
                savedMovie,
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "Get all movies",
            description = "Returns all movies."
    )
    @GetMapping
    public ResponseEntity<List<MovieResponse>>
            getAllMovies() {

        return ResponseEntity.ok(
                movieService.getAllMovies()
        );
    }

    @Operation(
            summary = "Get movies with pagination",
            description = "Returns movies using page number and page size."
    )
    @GetMapping("/page")
    public ResponseEntity<Page<MovieResponse>>
            getMoviesWithPagination(

                    @RequestParam(
                            defaultValue = "0"
                    )
                    int page,

                    @RequestParam(
                            defaultValue = "10"
                    )
                    int size) {

        return ResponseEntity.ok(
                movieService.getMoviesWithPagination(
                        page,
                        size
                )
        );
    }

    @Operation(
            summary = "Search movies by title",
            description = "Searches movies whose title contains the given text."
    )
    @GetMapping("/search")
    public ResponseEntity<List<MovieResponse>>
            searchByTitle(
                    @RequestParam String title) {

        return ResponseEntity.ok(
                movieService.searchByTitle(title)
        );
    }

    @Operation(
            summary = "Filter movies by genre",
            description = "Returns movies belonging to the specified genre."
    )
    @GetMapping("/genre")
    public ResponseEntity<List<MovieResponse>>
            filterByGenre(
                    @RequestParam String genre) {

        return ResponseEntity.ok(
                movieService.filterByGenre(genre)
        );
    }

    @Operation(
            summary = "Filter movies by minimum rating",
            description = "Returns movies with a rating greater than or equal to the given rating."
    )
    @GetMapping("/rating")
    public ResponseEntity<List<MovieResponse>>
            filterByMinimumRating(
                    @RequestParam Double rating) {

        return ResponseEntity.ok(
                movieService.filterByMinimumRating(
                        rating
                )
        );
    }

    @Operation(
            summary = "Get movie by ID",
            description = "Returns a movie using its ID."
    )
    @GetMapping("/{id}")
    public ResponseEntity<MovieResponse>
            getMovieById(
                    @PathVariable Long id) {

        return ResponseEntity.ok(
                movieService.getMovieById(id)
        );
    }

    @Operation(
            summary = "Update a movie",
            description = "Updates an existing movie. ADMIN access required."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<MovieResponse>
            updateMovie(
                    @PathVariable Long id,
                    @Valid @RequestBody
                    MovieRequest movieRequest) {

        MovieResponse updatedMovie =
                movieService.updateMovie(
                        id,
                        movieRequest
                );

        return ResponseEntity.ok(updatedMovie);
    }

    @Operation(
            summary = "Delete a movie",
            description = "Deletes an existing movie. ADMIN access required."
    )
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
            deleteMovie(
                    @PathVariable Long id) {

        movieService.deleteMovie(id);

        return ResponseEntity.noContent()
                .build();
    }
}