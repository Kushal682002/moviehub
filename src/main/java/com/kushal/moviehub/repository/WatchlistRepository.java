package com.kushal.moviehub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kushal.moviehub.entity.Watchlist;

public interface WatchlistRepository extends JpaRepository<Watchlist,Long>{
	
	List<Watchlist> findByUserId(Long userId);
	
	Optional<Watchlist> findByUserIdAndMovieId(Long userId,Long movieId);
	
	boolean existsByUserIdAndMovieId(Long userId,Long movieId);
	
	void deleteByUserIdAndMovieId(Long userId,Long movieId);
	
}
