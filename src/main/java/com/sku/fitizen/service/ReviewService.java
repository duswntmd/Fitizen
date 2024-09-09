package com.sku.fitizen.service;

import com.sku.fitizen.domain.Review;
import com.sku.fitizen.mapper.ReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;

    @Transactional
    public void saveReview(Review review) {
        reviewMapper.insertReview(review);
    }

    public List<Review> getReviewsByPlaceId(Long placeId) {
        return reviewMapper.findReviewsByPlaceId(placeId);
    }

    public Review getReviewById(Long reviewId) {
        return reviewMapper.findReviewById(reviewId);
    }

    public void updateReview(Review review) {
        reviewMapper.updateReview(review);
    }

    public void deleteReview(Long reviewId) {
        reviewMapper.deleteReview(reviewId);
    }

}
