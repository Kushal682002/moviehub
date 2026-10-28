package com.kushal.moviehub.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kushal.moviehub.dto.ReviewRequest;
import com.kushal.moviehub.dto.ReviewResponse;
import com.kushal.moviehub.service.ReviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(
            summary = "Add a review",
            description = "Adds a review for a movie. USER or ADMIN access required."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/movies/{movieId}/reviews")
    public ResponseEntity<ReviewResponse> addReview(
            @PathVariable Long movieId,
            @Valid @RequestBody ReviewRequest reviewRequest) {

        ReviewResponse response =
                reviewService.addReview(
                        movieId,
                        reviewRequest
                );

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "Get reviews for a movie",
            description = "Returns all reviews associated with a movie."
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/movies/{movieId}/reviews")
    public ResponseEntity<List<ReviewResponse>>
            getReviewsByMovie(
                    @PathVariable Long movieId) {

        return ResponseEntity.ok(
                reviewService.getReviewsByMovie(movieId)
        );
    }

    @Operation(
            summary = "Get review by ID",
            description = "Returns a specific review using its ID."
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/reviews/{id}")
    public ResponseEntity<ReviewResponse>
            getReviewById(
                    @PathVariable Long id) {

        return ResponseEntity.ok(
                reviewService.getReviewById(id)
        );
    }

    @Operation(
            summary = "Update a review",
            description = "Updates an existing review. The review owner or ADMIN can update it."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/reviews/{id}")
    public ResponseEntity<ReviewResponse>
            updateReview(
                    @PathVariable Long id,
                    @Valid @RequestBody
                    ReviewRequest reviewRequest) {

        return ResponseEntity.ok(
                reviewService.updateReview(
                        id,
                        reviewRequest
                )
        );
    }

    @Operation(
            summary = "Delete a review",
            description = "Deletes an existing review. The review owner or ADMIN can delete it."
    )
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Void>
            deleteReview(
                    @PathVariable Long id) {

        reviewService.deleteReview(id);

        return ResponseEntity.noContent()
                .build();
    }
}