package com.kushal.moviehub.service;

import java.util.List;

import com.kushal.moviehub.dto.ReviewRequest;
import com.kushal.moviehub.dto.ReviewResponse;

public interface ReviewService {
	
	ReviewResponse addReview(Long movieId,ReviewRequest reviewRequest);
	
	List<ReviewResponse> getReviewsByMovie(Long movieId);
	
	ReviewResponse getReviewById(Long id);
	
	ReviewResponse updateReview(Long id,ReviewRequest reviewRequest);
	
	void deleteReview(Long id);
	
}
