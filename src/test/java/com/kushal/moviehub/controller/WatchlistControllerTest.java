package com.kushal.moviehub.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import com.kushal.moviehub.dto.WatchlistResponse;
import com.kushal.moviehub.service.WatchlistService;

@ExtendWith(MockitoExtension.class)
class WatchlistControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WatchlistService watchlistService;

    @InjectMocks
    private WatchlistController watchlistController;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(watchlistController)
                .build();
    }

    @Test
    void addToWatchlist_shouldReturnCreatedWatchlist()
            throws Exception {

        // Arrange
        WatchlistResponse response =
                createWatchlistResponse();

        when(watchlistService.addToWatchlist(1L, 1L))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(
                post("/api/users/1/watchlist/1")
                        .contentType(MediaType.APPLICATION_JSON)
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
                jsonPath("$.movieId")
                        .value(1)
        )
        .andExpect(
                jsonPath("$.movieTitle")
                        .value("Inception")
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
                jsonPath("$.rating")
                        .value(8.8)
        );

        verify(watchlistService)
                .addToWatchlist(1L, 1L);
    }

    @Test
    void getUserWatchlist_shouldReturnWatchlist()
            throws Exception {

        // Arrange
        WatchlistResponse watchlist1 =
                createWatchlistResponse();

        WatchlistResponse watchlist2 =
                new WatchlistResponse();

        watchlist2.setId(2L);
        watchlist2.setUserId(1L);
        watchlist2.setMovieId(2L);
        watchlist2.setMovieTitle("Interstellar");
        watchlist2.setGenre("Sci-Fi");
        watchlist2.setReleaseYear(2014);
        watchlist2.setRating(8.7);

        when(watchlistService.getUserWatchlist(1L))
                .thenReturn(
                        List.of(watchlist1, watchlist2)
                );

        // Act & Assert
        mockMvc.perform(
                get("/api/users/1/watchlist")
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
                jsonPath("$[0].movieTitle")
                        .value("Inception")
        )
        .andExpect(
                jsonPath("$[0].rating")
                        .value(8.8)
        )
        .andExpect(
                jsonPath("$[1].movieTitle")
                        .value("Interstellar")
        )
        .andExpect(
                jsonPath("$[1].rating")
                        .value(8.7)
        );

        verify(watchlistService)
                .getUserWatchlist(1L);
    }

    @Test
    void removeFromWatchlist_shouldReturnNoContent()
            throws Exception {

        // Arrange
        doNothing()
                .when(watchlistService)
                .removeFromWatchlist(1L, 1L);

        // Act & Assert
        mockMvc.perform(
                delete("/api/users/1/watchlist/1")
        )
        .andExpect(status().isNoContent());

        verify(watchlistService)
                .removeFromWatchlist(1L, 1L);
    }

    private WatchlistResponse createWatchlistResponse() {

        WatchlistResponse response =
                new WatchlistResponse();

        response.setId(1L);
        response.setUserId(1L);
        response.setMovieId(1L);
        response.setMovieTitle("Inception");
        response.setGenre("Sci-Fi");
        response.setReleaseYear(2010);
        response.setRating(8.8);

        return response;
    }
}