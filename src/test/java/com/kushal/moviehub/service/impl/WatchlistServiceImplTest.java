package com.kushal.moviehub.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.kushal.moviehub.dto.WatchlistResponse;
import com.kushal.moviehub.entity.Movie;
import com.kushal.moviehub.entity.Role;
import com.kushal.moviehub.entity.User;
import com.kushal.moviehub.entity.Watchlist;
import com.kushal.moviehub.exception.AccessDeniedException;
import com.kushal.moviehub.exception.MovieNotFoundException;
import com.kushal.moviehub.exception.UserNotFoundException;
import com.kushal.moviehub.exception.WatchlistAlreadyExistsException;
import com.kushal.moviehub.exception.WatchlistNotFoundException;
import com.kushal.moviehub.repository.MovieRepository;
import com.kushal.moviehub.repository.UserRepository;
import com.kushal.moviehub.repository.WatchlistRepository;

@ExtendWith(MockitoExtension.class)
class WatchlistServiceImplTest {

    @Mock
    private WatchlistRepository watchlistRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private WatchlistServiceImpl watchlistService;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void addToWatchlist_shouldAddMovie_whenUserOwnsWatchlist() {

        // Arrange
        User currentUser = createUser(
                1L,
                "Kushal",
                "kushal@example.com"
        );

        Movie movie = createMovie(
                10L,
                "Inception"
        );

        mockAuthenticatedUser(
                "kushal@example.com"
        );

        when(userRepository.findByEmail(
                "kushal@example.com"
        )).thenReturn(Optional.of(currentUser));

        when(movieRepository.findById(10L))
                .thenReturn(Optional.of(movie));

        when(watchlistRepository
                .existsByUserIdAndMovieId(1L, 10L))
                .thenReturn(false);

        Watchlist savedWatchlist =
                new Watchlist();

        savedWatchlist.setId(100L);
        savedWatchlist.setUser(currentUser);
        savedWatchlist.setMovie(movie);

        when(watchlistRepository.save(any(Watchlist.class)))
                .thenReturn(savedWatchlist);

        // Act
        WatchlistResponse response =
                watchlistService.addToWatchlist(
                        1L,
                        10L
                );

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
                10L,
                response.getMovieId()
        );

        assertEquals(
                "Inception",
                response.getMovieTitle()
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
                8.8,
                response.getRating()
        );

        verify(watchlistRepository)
                .save(any(Watchlist.class));
    }

    @Test
    void addToWatchlist_shouldThrowException_whenUserAccessesAnotherUsersWatchlist() {

        // Arrange
        User currentUser = createUser(
                1L,
                "Kushal",
                "kushal@example.com"
        );

        mockAuthenticatedUser(
                "kushal@example.com"
        );

        when(userRepository.findByEmail(
                "kushal@example.com"
        )).thenReturn(Optional.of(currentUser));

        // Act & Assert
        assertThrows(
                AccessDeniedException.class,
                () -> watchlistService.addToWatchlist(
                        2L,
                        10L
                )
        );

        verify(
                watchlistRepository,
                org.mockito.Mockito.never()
        ).save(any(Watchlist.class));
    }

    @Test
    void addToWatchlist_shouldThrowException_whenMovieDoesNotExist() {

        // Arrange
        User currentUser = createUser(
                1L,
                "Kushal",
                "kushal@example.com"
        );

        mockAuthenticatedUser(
                "kushal@example.com"
        );

        when(userRepository.findByEmail(
                "kushal@example.com"
        )).thenReturn(Optional.of(currentUser));

        when(movieRepository.findById(10L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                MovieNotFoundException.class,
                () -> watchlistService.addToWatchlist(
                        1L,
                        10L
                )
        );

        verify(movieRepository)
                .findById(10L);
    }

    @Test
    void addToWatchlist_shouldThrowException_whenMovieAlreadyExists() {

        // Arrange
        User currentUser = createUser(
                1L,
                "Kushal",
                "kushal@example.com"
        );

        Movie movie = createMovie(
                10L,
                "Inception"
        );

        mockAuthenticatedUser(
                "kushal@example.com"
        );

        when(userRepository.findByEmail(
                "kushal@example.com"
        )).thenReturn(Optional.of(currentUser));

        when(movieRepository.findById(10L))
                .thenReturn(Optional.of(movie));

        when(watchlistRepository
                .existsByUserIdAndMovieId(1L, 10L))
                .thenReturn(true);

        // Act & Assert
        assertThrows(
                WatchlistAlreadyExistsException.class,
                () -> watchlistService.addToWatchlist(
                        1L,
                        10L
                )
        );

        verify(
                watchlistRepository,
                org.mockito.Mockito.never()
        ).save(any(Watchlist.class));
    }

    @Test
    void getUserWatchlist_shouldReturnMovies_whenUserOwnsWatchlist() {

        // Arrange
        User currentUser = createUser(
                1L,
                "Kushal",
                "kushal@example.com"
        );

        Movie movie1 = createMovie(
                10L,
                "Inception"
        );

        Movie movie2 = createMovie(
                20L,
                "Interstellar"
        );

        Watchlist watchlist1 =
                new Watchlist();

        watchlist1.setId(100L);
        watchlist1.setUser(currentUser);
        watchlist1.setMovie(movie1);

        Watchlist watchlist2 =
                new Watchlist();

        watchlist2.setId(101L);
        watchlist2.setUser(currentUser);
        watchlist2.setMovie(movie2);

        mockAuthenticatedUser(
                "kushal@example.com"
        );

        when(userRepository.findByEmail(
                "kushal@example.com"
        )).thenReturn(Optional.of(currentUser));

        when(watchlistRepository.findByUserId(1L))
                .thenReturn(List.of(
                        watchlist1,
                        watchlist2
                ));

        // Act
        List<WatchlistResponse> responses =
                watchlistService.getUserWatchlist(1L);

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
                "Inception",
                responses.get(0).getMovieTitle()
        );

        assertEquals(
                101L,
                responses.get(1).getId()
        );

        assertEquals(
                "Interstellar",
                responses.get(1).getMovieTitle()
        );

        verify(watchlistRepository)
                .findByUserId(1L);
    }

    @Test
    void getUserWatchlist_shouldThrowException_whenUserAccessesAnotherUsersWatchlist() {

        // Arrange
        User currentUser = createUser(
                1L,
                "Kushal",
                "kushal@example.com"
        );

        mockAuthenticatedUser(
                "kushal@example.com"
        );

        when(userRepository.findByEmail(
                "kushal@example.com"
        )).thenReturn(Optional.of(currentUser));

        // Act & Assert
        assertThrows(
                AccessDeniedException.class,
                () -> watchlistService.getUserWatchlist(2L)
        );

        verify(
                watchlistRepository,
                org.mockito.Mockito.never()
        ).findByUserId(any());
    }

    @Test
    void removeFromWatchlist_shouldRemoveMovie_whenUserOwnsWatchlist() {

        // Arrange
        User currentUser = createUser(
                1L,
                "Kushal",
                "kushal@example.com"
        );

        mockAuthenticatedUser(
                "kushal@example.com"
        );

        when(userRepository.findByEmail(
                "kushal@example.com"
        )).thenReturn(Optional.of(currentUser));

        when(movieRepository.existsById(10L))
                .thenReturn(true);

        when(watchlistRepository
                .existsByUserIdAndMovieId(1L, 10L))
                .thenReturn(true);

        // Act
        watchlistService.removeFromWatchlist(
                1L,
                10L
        );

        // Assert
        verify(movieRepository)
                .existsById(10L);

        verify(watchlistRepository)
                .existsByUserIdAndMovieId(1L, 10L);

        verify(watchlistRepository)
                .deleteByUserIdAndMovieId(1L, 10L);
    }

    @Test
    void removeFromWatchlist_shouldThrowException_whenUserAccessesAnotherUsersWatchlist() {

        // Arrange
        User currentUser = createUser(
                1L,
                "Kushal",
                "kushal@example.com"
        );

        mockAuthenticatedUser(
                "kushal@example.com"
        );

        when(userRepository.findByEmail(
                "kushal@example.com"
        )).thenReturn(Optional.of(currentUser));

        // Act & Assert
        assertThrows(
                AccessDeniedException.class,
                () -> watchlistService.removeFromWatchlist(
                        2L,
                        10L
                )
        );

        verify(
                watchlistRepository,
                org.mockito.Mockito.never()
        ).deleteByUserIdAndMovieId(
                any(),
                any()
        );
    }

    @Test
    void removeFromWatchlist_shouldThrowException_whenMovieDoesNotExist() {

        // Arrange
        User currentUser = createUser(
                1L,
                "Kushal",
                "kushal@example.com"
        );

        mockAuthenticatedUser(
                "kushal@example.com"
        );

        when(userRepository.findByEmail(
                "kushal@example.com"
        )).thenReturn(Optional.of(currentUser));

        when(movieRepository.existsById(10L))
                .thenReturn(false);

        // Act & Assert
        assertThrows(
                MovieNotFoundException.class,
                () -> watchlistService.removeFromWatchlist(
                        1L,
                        10L
                )
        );

        verify(movieRepository)
                .existsById(10L);

        verify(
                watchlistRepository,
                org.mockito.Mockito.never()
        ).deleteByUserIdAndMovieId(
                any(),
                any()
        );
    }

    @Test
    void removeFromWatchlist_shouldThrowException_whenMovieIsNotInWatchlist() {

        // Arrange
        User currentUser = createUser(
                1L,
                "Kushal",
                "kushal@example.com"
        );

        mockAuthenticatedUser(
                "kushal@example.com"
        );

        when(userRepository.findByEmail(
                "kushal@example.com"
        )).thenReturn(Optional.of(currentUser));

        when(movieRepository.existsById(10L))
                .thenReturn(true);

        when(watchlistRepository
                .existsByUserIdAndMovieId(1L, 10L))
                .thenReturn(false);

        // Act & Assert
        assertThrows(
                WatchlistNotFoundException.class,
                () -> watchlistService.removeFromWatchlist(
                        1L,
                        10L
                )
        );

        verify(
                watchlistRepository,
                org.mockito.Mockito.never()
        ).deleteByUserIdAndMovieId(
                any(),
                any()
        );
    }

    @Test
    void addToWatchlist_shouldThrowException_whenAuthenticatedUserDoesNotExist() {

        // Arrange
        mockAuthenticatedUser(
                "unknown@example.com"
        );

        when(userRepository.findByEmail(
                "unknown@example.com"
        )).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                UserNotFoundException.class,
                () -> watchlistService.addToWatchlist(
                        1L,
                        10L
                )
        );

        verify(
                watchlistRepository,
                org.mockito.Mockito.never()
        ).save(any(Watchlist.class));
    }

    private void mockAuthenticatedUser(
            String email) {

        Authentication authentication =
                mock(Authentication.class);

        SecurityContext securityContext =
                mock(SecurityContext.class);

        when(authentication.isAuthenticated())
                .thenReturn(true);

        when(authentication.getName())
                .thenReturn(email);

        /*
         * WatchlistServiceImpl only uses:
         *
         * authentication.isAuthenticated()
         * authentication.getName()
         *
         * It does NOT use getAuthorities().
         *
         * Therefore we must NOT stub getAuthorities()
         * here. Mockito strict mode would report it as
         * an unnecessary stubbing.
         */

        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        SecurityContextHolder.setContext(
                securityContext
        );
    }

    private User createUser(
            Long id,
            String name,
            String email) {

        User user = new User();

        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setPassword("encodedPassword");
        user.setRole(Role.USER);

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