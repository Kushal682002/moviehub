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

import com.kushal.moviehub.dto.ReviewRequest;
import com.kushal.moviehub.dto.ReviewResponse;
import com.kushal.moviehub.exception.GlobalExceptionHandler;
import com.kushal.moviehub.service.ReviewService;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(reviewController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void addReview_shouldReturnCreatedReview()
            throws Exception {

        // Arrange
        ReviewResponse response =
                createReviewResponse();

        when(reviewService.addReview(
                eq(1L),
                any(ReviewRequest.class)
        )).thenReturn(response);

        // Act & Assert
        mockMvc.perform(
                post("/api/movies/1/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "userId": 1,
                                    "rating": 9,
                                    "comment": "Excellent movie!"
                                }
                                """)
        )
        .andExpect(status().isCreated())
        .andExpect(
                jsonPath("$.id")
                        .value(1)
        )
        .andExpect(
                jsonPath("$.userId")
                        .value(1)
        )
        .andExpect(
                jsonPath("$.userName")
                        .value("Kushal")
        )
        .andExpect(
                jsonPath("$.movieId")
                        .value(1)
        )
        .andExpect(
                jsonPath("$.rating")
                        .value(9)
        )
        .andExpect(
                jsonPath("$.comment")
                        .value("Excellent movie!")
        );

        verify(reviewService)
                .addReview(
                        eq(1L),
                        any(ReviewRequest.class)
                );
    }

    @Test
    void getReviewsByMovie_shouldReturnReviews()
            throws Exception {

        // Arrange
        ReviewResponse review1 =
                createReviewResponse();

        ReviewResponse review2 =
                new ReviewResponse();

        review2.setId(2L);
        review2.setUserId(2L);
        review2.setUserName("Rahul");
        review2.setMovieId(1L);
        review2.setRating(8);
        review2.setComment("Very good movie.");

        when(reviewService.getReviewsByMovie(1L))
                .thenReturn(
                        List.of(review1, review2)
                );

        // Act & Assert
        mockMvc.perform(
                get("/api/movies/1/reviews")
        )
        .andExpect(status().isOk())
        .andExpect(
                jsonPath("$")
                        .isArray()
        )
        .andExpect(
                jsonPath("$.length()")
                        .value(2)
        )
        .andExpect(
                jsonPath("$[0].rating")
                        .value(9)
        )
        .andExpect(
                jsonPath("$[0].comment")
                        .value("Excellent movie!")
        )
        .andExpect(
                jsonPath("$[1].rating")
                        .value(8)
        )
        .andExpect(
                jsonPath("$[1].comment")
                        .value("Very good movie.")
        );

        verify(reviewService)
                .getReviewsByMovie(1L);
    }

    @Test
    void getReviewById_shouldReturnReview()
            throws Exception {

        // Arrange
        ReviewResponse response =
                createReviewResponse();

        when(reviewService.getReviewById(1L))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(
                get("/api/reviews/1")
        )
        .andExpect(status().isOk())
        .andExpect(
                jsonPath("$.id")
                        .value(1)
        )
        .andExpect(
                jsonPath("$.userId")
                        .value(1)
        )
        .andExpect(
                jsonPath("$.userName")
                        .value("Kushal")
        )
        .andExpect(
                jsonPath("$.movieId")
                        .value(1)
        )
        .andExpect(
                jsonPath("$.rating")
                        .value(9)
        )
        .andExpect(
                jsonPath("$.comment")
                        .value("Excellent movie!")
        );

        verify(reviewService)
                .getReviewById(1L);
    }

    @Test
    void updateReview_shouldReturnUpdatedReview()
            throws Exception {

        // Arrange
        ReviewResponse response =
                createReviewResponse();

        response.setRating(10);
        response.setComment(
                "Absolutely excellent movie!"
        );

        when(reviewService.updateReview(
                eq(1L),
                any(ReviewRequest.class)
        )).thenReturn(response);

        // Act & Assert
        mockMvc.perform(
                put("/api/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "userId": 1,
                                    "rating": 10,
                                    "comment": "Absolutely excellent movie!"
                                }
                                """)
        )
        .andExpect(status().isOk())
        .andExpect(
                jsonPath("$.id")
                        .value(1)
        )
        .andExpect(
                jsonPath("$.rating")
                        .value(10)
        )
        .andExpect(
                jsonPath("$.comment")
                        .value(
                                "Absolutely excellent movie!"
                        )
        );

        verify(reviewService)
                .updateReview(
                        eq(1L),
                        any(ReviewRequest.class)
                );
    }

    @Test
    void deleteReview_shouldReturnNoContent()
            throws Exception {

        // Arrange
        doNothing()
                .when(reviewService)
                .deleteReview(1L);

        // Act & Assert
        mockMvc.perform(
                delete("/api/reviews/1")
        )
        .andExpect(status().isNoContent());

        verify(reviewService)
                .deleteReview(1L);
    }

    private ReviewResponse createReviewResponse() {

        ReviewResponse response =
                new ReviewResponse();

        response.setId(1L);
        response.setUserId(1L);
        response.setUserName("Kushal");
        response.setMovieId(1L);
        response.setRating(9);
        response.setComment("Excellent movie!");

        return response;
    }
}