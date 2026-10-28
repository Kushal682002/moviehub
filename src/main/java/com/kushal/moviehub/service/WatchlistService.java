package com.kushal.moviehub.service;

import java.util.List;

import com.kushal.moviehub.dto.WatchlistResponse;

public interface WatchlistService {
	
	WatchlistResponse addToWatchlist(Long userId,Long movieId);
	
	List<WatchlistResponse> getUserWatchlist(Long userId);
	
	void removeFromWatchlist(Long userId,Long movieId);
	
}
