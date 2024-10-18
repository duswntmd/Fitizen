package com.sku.fitizen.service.placereview;

import com.sku.fitizen.domain.placereview.Review;
import com.sku.fitizen.mapper.placereview.ReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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

    public Double getAverageRating(Long placeId) {
        return reviewMapper.getAverageRating(placeId);
    }

    public List<Map<String, Object>> getRatingCounts(Long placeId) {
        return reviewMapper.getRatingCounts(placeId);
    }

}
