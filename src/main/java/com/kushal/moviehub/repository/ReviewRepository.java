package com.kushal.moviehub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kushal.moviehub.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>{
	
	List<Review> findByMovieId(Long movieId);
	
	List<Review> findByUserId(Long userId);
	
}
