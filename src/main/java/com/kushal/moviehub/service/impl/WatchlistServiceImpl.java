package com.kushal.moviehub.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kushal.moviehub.dto.WatchlistResponse;
import com.kushal.moviehub.entity.Movie;
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
import com.kushal.moviehub.service.WatchlistService;

@Service
public class WatchlistServiceImpl implements WatchlistService {

    private static final Logger logger =
            LoggerFactory.getLogger(WatchlistServiceImpl.class);

    private final WatchlistRepository watchlistRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public WatchlistServiceImpl(
            WatchlistRepository watchlistRepository,
            UserRepository userRepository,
            MovieRepository movieRepository) {

        this.watchlistRepository = watchlistRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    @Transactional
    public WatchlistResponse addToWatchlist(
            Long userId,
            Long movieId) {

        logger.info(
                "Adding movie id {} to watchlist of user id {}",
                movieId,
                userId
        );

        User currentUser = getCurrentUser();

        checkOwnership(userId, currentUser);

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> {

                    logger.warn(
                            "Movie with id {} not found while adding to watchlist",
                            movieId
                    );

                    return new MovieNotFoundException(
                            "Movie with id "
                                    + movieId
                                    + " not found"
                    );
                });

        if (watchlistRepository
                .existsByUserIdAndMovieId(
                        userId,
                        movieId)) {

            logger.warn(
                    "Movie id {} is already in watchlist of user id {}",
                    movieId,
                    userId
            );

            throw new WatchlistAlreadyExistsException(
                    "Movie with id "
                            + movieId
                            + " is already in the watchlist"
            );
        }

        Watchlist watchlist = new Watchlist();

        watchlist.setUser(currentUser);
        watchlist.setMovie(movie);

        Watchlist savedWatchlist =
                watchlistRepository.save(watchlist);

        logger.info(
                "Movie id {} added to watchlist of user id {} successfully",
                movieId,
                userId
        );

        return convertToResponse(savedWatchlist);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WatchlistResponse> getUserWatchlist(
            Long userId) {

        logger.info(
                "Fetching watchlist for user id: {}",
                userId
        );

        User currentUser = getCurrentUser();

        checkOwnership(userId, currentUser);

        return watchlistRepository
                .findByUserId(userId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    @Transactional
    public void removeFromWatchlist(
            Long userId,
            Long movieId) {

        logger.info(
                "Removing movie id {} from watchlist of user id {}",
                movieId,
                userId
        );

        User currentUser = getCurrentUser();

        checkOwnership(userId, currentUser);

        if (!movieRepository.existsById(movieId)) {

            logger.warn(
                    "Movie with id {} not found while removing from watchlist",
                    movieId
            );

            throw new MovieNotFoundException(
                    "Movie with id "
                            + movieId
                            + " not found"
            );
        }

        if (!watchlistRepository
                .existsByUserIdAndMovieId(
                        userId,
                        movieId)) {

            logger.warn(
                    "Movie id {} is not in watchlist of user id {}",
                    movieId,
                    userId
            );

            throw new WatchlistNotFoundException(
                    "Movie with id "
                            + movieId
                            + " is not in the watchlist"
            );
        }

        watchlistRepository.deleteByUserIdAndMovieId(
                userId,
                movieId
        );

        logger.info(
                "Movie id {} removed from watchlist of user id {} successfully",
                movieId,
                userId
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
                    "Attempt to access watchlist without authentication"
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

    private void checkOwnership(
            Long requestedUserId,
            User currentUser) {

        if (!currentUser.getId()
                .equals(requestedUserId)) {

            logger.warn(
                    "User {} attempted to access watchlist of user {}",
                    currentUser.getId(),
                    requestedUserId
            );

            throw new AccessDeniedException(
                    "You can only access your own watchlist"
            );
        }
    }

    private WatchlistResponse convertToResponse(
            Watchlist watchlist) {

        WatchlistResponse response =
                new WatchlistResponse();

        response.setId(watchlist.getId());

        if (watchlist.getUser() != null) {

            response.setUserId(
                    watchlist.getUser().getId()
            );
        }

        if (watchlist.getMovie() != null) {

            Movie movie = watchlist.getMovie();

            response.setMovieId(
                    movie.getId()
            );

            response.setMovieTitle(
                    movie.getTitle()
            );

            response.setGenre(
                    movie.getGenre()
            );

            response.setReleaseYear(
                    movie.getReleaseYear()
            );

            response.setRating(
                    movie.getRating()
            );
        }

        return response;
    }
}