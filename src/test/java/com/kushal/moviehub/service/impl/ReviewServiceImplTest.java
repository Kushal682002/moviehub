package com.kushal.moviehub.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.kushal.moviehub.dto.ReviewRequest;
import com.kushal.moviehub.dto.ReviewResponse;
import com.kushal.moviehub.entity.Movie;
import com.kushal.moviehub.entity.Review;
import com.kushal.moviehub.entity.Role;
import com.kushal.moviehub.entity.User;
import com.kushal.moviehub.exception.AccessDeniedException;
import com.kushal.moviehub.exception.MovieNotFoundException;
import com.kushal.moviehub.exception.ReviewNotFoundException;
import com.kushal.moviehub.repository.MovieRepository;
import com.kushal.moviehub.repository.ReviewRepository;
import com.kushal.moviehub.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void addReview_shouldCreateReview_whenUserIsAddingOwnReview() {

        // Arrange
        User currentUser = createUser(
                1L,
                "Kushal",
                "kushal@example.com",
                Role.USER
        );

        Movie movie = createMovie(
                10L,
                "Inception"
        );

        ReviewRequest request = new ReviewRequest();

        request.setUserId(1L);
        request.setRating(9);
        request.setComment("Excellent movie");

        mockAuthenticatedUser(
                "kushal@example.com",
                false
        );

        when(userRepository.findByEmail(
                "kushal@example.com"
        )).thenReturn(Optional.of(currentUser));

        when(movieRepository.findById(10L))
                .thenReturn(Optional.of(movie));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(currentUser));

        Review savedReview = new Review();

        savedReview.setId(100L);
        savedReview.setUser(currentUser);
        savedReview.setMovie(movie);
        savedReview.setRating(9);
        savedReview.setComment("Excellent movie");

        when(reviewRepository.save(any(Review.class)))
                .thenReturn(savedReview);

        // Act
        ReviewResponse response =
                reviewService.addReview(10L, request);

        // Assert
        assertEquals(
                100L,
                response.getId()
        );

        assertEquals(
                1L,
                response.getUserId()
        );

        assertEquals(
                "Kushal",
                response.getUserName()
        );

        assertEquals(
                10L,
                response.getMovieId()
        );

        assertEquals(
                9,
                response.getRating()
        );

        assertEquals(
                "Excellent movie",
                response.getComment()
        );

        verify(reviewRepository)
                .save(any(Review.class));
    }

    @Test
    void addReview_shouldThrowException_whenUserAddsReviewForAnotherUser() {

        // Arrange
        User currentUser = createUser(
                1L,
                "Kushal",
                "kushal@example.com",
                Role.USER
        );

        ReviewRequest request = new ReviewRequest();

        request.setUserId(2L);
        request.setRating(8);
        request.setComment("Good movie");

        mockAuthenticatedUser(
                "kushal@example.com",
                false
        );

        when(userRepository.findByEmail(
                "kushal@example.com"
        )).thenReturn(Optional.of(currentUser));

        // Act & Assert
        assertThrows(
                AccessDeniedException.class,
                () -> reviewService.addReview(
                        10L,
                        request
                )
        );

        verify(
                reviewRepository,
                org.mockito.Mockito.never()
        ).save(any(Review.class));
    }

    @Test
    void addReview_shouldThrowException_whenMovieDoesNotExist() {

        // Arrange
        User currentUser = createUser(
                1L,
                "Kushal",
                "kushal@example.com",
                Role.USER
        );

        ReviewRequest request = new ReviewRequest();

        request.setUserId(1L);
        request.setRating(9);
        request.setComment("Excellent movie");

        mockAuthenticatedUser(
                "kushal@example.com",
                false
        );

        when(userRepository.findByEmail(
                "kushal@example.com"
        )).thenReturn(Optional.of(currentUser));

        when(movieRepository.findById(10L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                MovieNotFoundException.class,
                () -> reviewService.addReview(
                        10L,
                        request
                )
        );

        verify(movieRepository)
                .findById(10L);
    }

    @Test
    void getReviewsByMovie_shouldReturnReviews_whenMovieExists() {

        // Arrange
        Movie movie = createMovie(
                10L,
                "Inception"
        );

        User user = createUser(
                1L,
                "Kushal",
                "kushal@example.com",
                Role.USER
        );

        Review review1 = new Review();

        review1.setId(100L);
        review1.setMovie(movie);
        review1.setUser(user);
        review1.setRating(9);
        review1.setComment("Excellent");

        Review review2 = new Review();

        review2.setId(101L);
        review2.setMovie(movie);
        review2.setUser(user);
        review2.setRating(8);
        review2.setComment("Very good");

        when(movieRepository.existsById(10L))
                .thenReturn(true);

        when(reviewRepository.findByMovieId(10L))
                .thenReturn(List.of(
                        review1,
                        review2
                ));

        // Act
        List<ReviewResponse> responses =
                reviewService.getReviewsByMovie(10L);

        // Assert
        assertEquals(
                2,
                responses.size()
        );

        assertEquals(
                100L,
                responses.get(0).getId()
        );

        assertEquals(
                9,
                responses.get(0).getRating()
        );

        assertEquals(
                101L,
                responses.get(1).getId()
        );

        assertEquals(
                8,
                responses.get(1).getRating()
        );

        verify(movieRepository)
                .existsById(10L);

        verify(reviewRepository)
                .findByMovieId(10L);
    }

    @Test
    void getReviewsByMovie_shouldThrowException_whenMovieDoesNotExist() {

        // Arrange
        when(movieRepository.existsById(10L))
                .thenReturn(false);

        // Act & Assert
        assertThrows(
                MovieNotFoundException.class,
                () -> reviewService.getReviewsByMovie(10L)
        );

        verify(movieRepository)
                .existsById(10L);
    }

    @Test
    void getReviewById_shouldReturnReview_whenReviewExists() {

        // Arrange
        Movie movie = createMovie(
                10L,
                "Inception"
        );

        User user = createUser(
                1L,
                "Kushal",
                "kushal@example.com",
                Role.USER
        );

        Review review = new Review();

        review.setId(100L);
        review.setMovie(movie);
        review.setUser(user);
        review.setRating(9);
        review.setComment("Excellent movie");

        when(reviewRepository.findById(100L))
                .thenReturn(Optional.of(review));

        // Act
        ReviewResponse response =
                reviewService.getReviewById(100L);

        // Assert
        assertEquals(
                100L,
                response.getId()
        );

        assertEquals(
                1L,
                response.getUserId()
        );

        assertEquals(
                "Kushal",
                response.getUserName()
        );

        assertEquals(
                10L,
                response.getMovieId()
        );

        assertEquals(
                9,
                response.getRating()
        );

        assertEquals(
                "Excellent movie",
                response.getComment()
        );

        verify(reviewRepository)
                .findById(100L);
    }

    @Test
    void getReviewById_shouldThrowException_whenReviewDoesNotExist() {

        // Arrange
        when(reviewRepository.findById(100L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                ReviewNotFoundException.class,
                () -> reviewService.getReviewById(100L)
        );

        verify(reviewRepository)
                .findById(100L);
    }

    @Test
    void updateReview_shouldUpdateReview_whenCurrentUserIsOwner() {

        // Arrange
        User currentUser = createUser(
                1L,
                "Kushal",
                "kushal@example.com",
                Role.USER
        );

        Movie movie = createMovie(
                10L,
                "Inception"
        );

        Review existingReview = new Review();

        existingReview.setId(100L);
        existingReview.setUser(currentUser);
        existingReview.setMovie(movie);
        existingReview.setRating(7);
        existingReview.setComment("Good");

        ReviewRequest request = new ReviewRequest();

        request.setUserId(1L);
        request.setRating(9);
        request.setComment("Excellent movie");

        mockAuthenticatedUser(
                "kushal@example.com",
                false
        );

        when(reviewRepository.findById(100L))
                .thenReturn(Optional.of(existingReview));

        when(userRepository.findByEmail(
                "kushal@example.com"
        )).thenReturn(Optional.of(currentUser));

        when(reviewRepository.save(existingReview))
                .thenReturn(existingReview);

        // Act
        ReviewResponse response =
                reviewService.updateReview(
                        100L,
                        request
                );

        // Assert
        assertEquals(
                9,
                response.getRating()
        );

        assertEquals(
                "Excellent movie",
                response.getComment()
        );

        assertEquals(
                1L,
                existingReview.getUser().getId()
        );

        verify(reviewRepository)
                .findById(100L);

        verify(reviewRepository)
                .save(existingReview);
    }

    @Test
    void updateReview_shouldThrowException_whenCurrentUserIsNotOwner() {

        // Arrange
        User reviewOwner = createUser(
                1L,
                "Kushal",
                "kushal@example.com",
                Role.USER
        );

        User currentUser = createUser(
                2L,
                "Rahul",
                "rahul@example.com",
                Role.USER
        );

        Review existingReview = new Review();

        existingReview.setId(100L);
        existingReview.setUser(reviewOwner);
        existingReview.setRating(7);
        existingReview.setComment("Good");

        ReviewRequest request = new ReviewRequest();

        request.setUserId(2L);
        request.setRating(10);
        request.setComment("Updated review");

        mockAuthenticatedUser(
                "rahul@example.com",
                false
        );

        when(reviewRepository.findById(100L))
                .thenReturn(Optional.of(existingReview));

        when(userRepository.findByEmail(
                "rahul@example.com"
        )).thenReturn(Optional.of(currentUser));

        // Act & Assert
        assertThrows(
                AccessDeniedException.class,
                () -> reviewService.updateReview(
                        100L,
                        request
                )
        );

        verify(reviewRepository)
                .findById(100L);

        verify(
                reviewRepository,
                org.mockito.Mockito.never()
        ).save(any(Review.class));
    }

    @Test
    void deleteReview_shouldDeleteReview_whenCurrentUserIsOwner() {

        // Arrange
        User currentUser = createUser(
                1L,
                "Kushal",
                "kushal@example.com",
                Role.USER
        );

        Review review = new Review();

        review.setId(100L);
        review.setUser(currentUser);

        mockAuthenticatedUser(
                "kushal@example.com",
                false
        );

        when(reviewRepository.findById(100L))
                .thenReturn(Optional.of(review));

        when(userRepository.findByEmail(
                "kushal@example.com"
        )).thenReturn(Optional.of(currentUser));

        // Act
        reviewService.deleteReview(100L);

        // Assert
        verify(reviewRepository)
                .findById(100L);

        verify(reviewRepository)
                .delete(review);
    }

    @Test
    void deleteReview_shouldThrowException_whenCurrentUserIsNotOwner() {

        // Arrange
        User reviewOwner = createUser(
                1L,
                "Kushal",
                "kushal@example.com",
                Role.USER
        );

        User currentUser = createUser(
                2L,
                "Rahul",
                "rahul@example.com",
                Role.USER
        );

        Review review = new Review();

        review.setId(100L);
        review.setUser(reviewOwner);

        mockAuthenticatedUser(
                "rahul@example.com",
                false
        );

        when(reviewRepository.findById(100L))
                .thenReturn(Optional.of(review));

        when(userRepository.findByEmail(
                "rahul@example.com"
        )).thenReturn(Optional.of(currentUser));

        // Act & Assert
        assertThrows(
                AccessDeniedException.class,
                () -> reviewService.deleteReview(100L)
        );

        verify(reviewRepository)
                .findById(100L);

        verify(
                reviewRepository,
                org.mockito.Mockito.never()
        ).delete(any(Review.class));
    }

    private void mockAuthenticatedUser(
            String email,
            boolean admin) {

        Authentication authentication =
                mock(Authentication.class);

        SecurityContext securityContext =
                mock(SecurityContext.class);

        when(authentication.isAuthenticated())
                .thenReturn(true);

        when(authentication.getName())
                .thenReturn(email);

        Collection<GrantedAuthority> authorities;

        if (admin) {

            authorities = List.of(
                    new SimpleGrantedAuthority(
                            "ROLE_ADMIN"
                    )
            );

        } else {

            authorities = List.of(
                    new SimpleGrantedAuthority(
                            "ROLE_USER"
                    )
            );
        }

        /*
         * Use doReturn() instead of when().thenReturn()
         * because Spring Security declares getAuthorities()
         * using a wildcard generic:
         *
         * Collection<? extends GrantedAuthority>
         */
        doReturn(authorities)
                .when(authentication)
                .getAuthorities();

        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        SecurityContextHolder.setContext(
                securityContext
        );
    }

    private User createUser(
            Long id,
            String name,
            String email,
            Role role) {

        User user = new User();

        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setPassword("encodedPassword");
        user.setRole(role);

        return user;
    }

    private Movie createMovie(
            Long id,
            String title) {

        Movie movie = new Movie();

        movie.setId(id);
        movie.setTitle(title);
        movie.setDescription(
                "Movie description"
        );
        movie.setGenre("Sci-Fi");
        movie.setReleaseYear(2010);
        movie.setDuration(148);
        movie.setLanguage("English");
        movie.setRating(8.8);

        return movie;
    }
}