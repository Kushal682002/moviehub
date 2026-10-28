package com.kushal.moviehub.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kushal.moviehub.dto.WatchlistResponse;
import com.kushal.moviehub.service.WatchlistService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/users/{userId}/watchlist")
public class WatchlistController {

    private final WatchlistService watchlistService;

    public WatchlistController(
            WatchlistService watchlistService) {

        this.watchlistService = watchlistService;
    }

    @Operation(
            summary = "Add movie to watchlist",
            description = "Adds a movie to a user's watchlist. USER or ADMIN access required."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/{movieId}")
    public ResponseEntity<WatchlistResponse>
            addToWatchlist(
                    @PathVariable Long userId,
                    @PathVariable Long movieId) {

        WatchlistResponse response =
                watchlistService.addToWatchlist(
                        userId,
                        movieId
                );

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "Get user's watchlist",
            description = "Returns all movies in a user's watchlist."
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<List<WatchlistResponse>>
            getUserWatchlist(
                    @PathVariable Long userId) {

        return ResponseEntity.ok(
                watchlistService.getUserWatchlist(
                        userId
                )
        );
    }

    @Operation(
            summary = "Remove movie from watchlist",
            description = "Removes a movie from a user's watchlist."
    )
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{movieId}")
    public ResponseEntity<Void>
            removeFromWatchlist(
                    @PathVariable Long userId,
                    @PathVariable Long movieId) {

        watchlistService.removeFromWatchlist(
                userId,
                movieId
        );

        return ResponseEntity.noContent()
                .build();
    }
}