package com.kushal.moviehub.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kushal.moviehub.dto.ReviewRequest;
import com.kushal.moviehub.dto.ReviewResponse;
import com.kushal.moviehub.entity.Movie;
import com.kushal.moviehub.entity.Review;
import com.kushal.moviehub.entity.User;
import com.kushal.moviehub.exception.AccessDeniedException;
import com.kushal.moviehub.exception.MovieNotFoundException;
import com.kushal.moviehub.exception.ReviewNotFoundException;
import com.kushal.moviehub.exception.UserNotFoundException;
import com.kushal.moviehub.repository.MovieRepository;
import com.kushal.moviehub.repository.ReviewRepository;
import com.kushal.moviehub.repository.UserRepository;
import com.kushal.moviehub.service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {

    private static final Logger logger =
            LoggerFactory.getLogger(ReviewServiceImpl.class);

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public ReviewServiceImpl(
            ReviewRepository reviewRepository,
            UserRepository userRepository,
            MovieRepository movieRepository) {

        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    @Transactional
    public ReviewResponse addReview(
            Long movieId,
            ReviewRequest reviewRequest) {

        logger.info(
                "Adding review for movie id: {} by user id: {}",
                movieId,
                reviewRequest.getUserId()
        );

        User currentUser = getCurrentUser();

        if (!isAdmin()
                && !currentUser.getId()
                        .equals(reviewRequest.getUserId())) {

            logger.warn(
                    "User {} attempted to create a review for user {}",
                    currentUser.getId(),
                    reviewRequest.getUserId()
            );

            throw new AccessDeniedException(
                    "You can only create a review for yourself"
            );
        }

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> {

                    logger.warn(
                            "Movie with id {} not found while adding review",
                            movieId
                    );

                    return new MovieNotFoundException(
                            "Movie with id "
                                    + movieId
                                    + " not found"
                    );
                });

        User user = userRepository.findById(
                reviewRequest.getUserId()
        ).orElseThrow(() -> {

            logger.warn(
                    "User with id {} not found while adding review",
                    reviewRequest.getUserId()
            );

            return new UserNotFoundException(
                    "User with id "
                            + reviewRequest.getUserId()
                            + " not found"
            );
        });

        Review review = new Review();

        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());
        review.setUser(user);
        review.setMovie(movie);

        Review savedReview =
                reviewRepository.save(review);

        logger.info(
                "Review created successfully with id: {}",
                savedReview.getId()
        );

        return convertToResponse(savedReview);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByMovie(
            Long movieId) {

        logger.info(
                "Fetching reviews for movie id: {}",
                movieId
        );

        if (!movieRepository.existsById(movieId)) {

            logger.warn(
                    "Movie with id {} not found while fetching reviews",
                    movieId
            );

            throw new MovieNotFoundException(
                    "Movie with id "
                            + movieId
                            + " not found"
            );
        }

        return reviewRepository.findByMovieId(movieId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewResponse getReviewById(Long id) {

        logger.info(
                "Fetching review with id: {}",
                id
        );

        Review review =
                reviewRepository.findById(id)
                        .orElseThrow(() -> {

                            logger.warn(
                                    "Review with id {} not found",
                                    id
                            );

                            return new ReviewNotFoundException(
                                    "Review with id "
                                            + id
                                            + " not found"
                            );
                        });

        return convertToResponse(review);
    }

    @Override
    @Transactional
    public ReviewResponse updateReview(
            Long id,
            ReviewRequest reviewRequest) {

        logger.info(
                "Updating review with id: {}",
                id
        );

        Review existingReview =
                reviewRepository.findById(id)
                        .orElseThrow(() -> {

                            logger.warn(
                                    "Review with id {} not found for update",
                                    id
                            );

                            return new ReviewNotFoundException(
                                    "Review with id "
                                            + id
                                            + " not found"
                            );
                        });

        User currentUser = getCurrentUser();

        if (!isAdmin()
                && !existingReview.getUser()
                        .getId()
                        .equals(currentUser.getId())) {

            logger.warn(
                    "User {} attempted to update review {} owned by user {}",
                    currentUser.getId(),
                    id,
                    existingReview.getUser().getId()
            );

            throw new AccessDeniedException(
                    "You can only update your own review"
            );
        }

        existingReview.setRating(
                reviewRequest.getRating()
        );

        existingReview.setComment(
                reviewRequest.getComment()
        );

        Review updatedReview =
                reviewRepository.save(existingReview);

        logger.info(
                "Review with id {} updated successfully",
                id
        );

        return convertToResponse(updatedReview);
    }

    @Override
    @Transactional
    public void deleteReview(Long id) {

        logger.info(
                "Deleting review with id: {}",
                id
        );

        Review existingReview =
                reviewRepository.findById(id)
                        .orElseThrow(() -> {

                            logger.warn(
                                    "Review with id {} not found for deletion",
                                    id
                            );

                            return new ReviewNotFoundException(
                                    "Review with id "
                                            + id
                                            + " not found"
                            );
                        });

        User currentUser = getCurrentUser();

        if (!isAdmin()
                && !existingReview.getUser()
                        .getId()
                        .equals(currentUser.getId())) {

            logger.warn(
                    "User {} attempted to delete review {} owned by user {}",
                    currentUser.getId(),
                    id,
                    existingReview.getUser().getId()
            );

            throw new AccessDeniedException(
                    "You can only delete your own review"
            );
        }

        reviewRepository.delete(existingReview);

        logger.info(
                "Review with id {} deleted successfully",
                id
        );
    }

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()) {

            logger.warn(
                    "Attempt to access review operation without authentication"
            );

            throw new AccessDeniedException(
                    "User is not authenticated"
            );
        }

        String email;

        Object principal =
                authentication.getPrincipal();

        if (principal instanceof UserDetails userDetails) {

            email = userDetails.getUsername();

        } else {

            email = authentication.getName();
        }

        return userRepository.findByEmail(email)
                .orElseThrow(() -> {

                    logger.warn(
                            "Authenticated user with email {} not found",
                            email
                    );

                    return new UserNotFoundException(
                            "Authenticated user not found"
                    );
                });
    }

    private boolean isAdmin() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        return authentication != null
                && authentication.getAuthorities()
                        .stream()
                        .anyMatch(authority ->
                                authority.getAuthority()
                                        .equals("ROLE_ADMIN"));
    }

    private ReviewResponse convertToResponse(
            Review review) {

        ReviewResponse response =
                new ReviewResponse();

        response.setId(review.getId());
        response.setRating(review.getRating());
        response.setComment(review.getComment());

        if (review.getUser() != null) {

            response.setUserId(
                    review.getUser().getId()
            );

            response.setUserName(
                    review.getUser().getName()
            );
        }

        if (review.getMovie() != null) {

            response.setMovieId(
                    review.getMovie().getId()
            );
        }

        return response;
    }
}