package com.sku.fitizen.mapper.placereview;

import com.sku.fitizen.domain.placereview.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReviewMapper {

    void insertReview(Review review);

    List<Review> findReviewsByPlaceId(@Param("placeId") Long placeId);

    Review findReviewById(@Param("reviewId") Long reviewId);

    void updateReview(Review review);

    void deleteReview(Long reviewId);

    Double getAverageRating(Long placeId);

    List<Map<String, Object>> getRatingCounts(Long placeId);


}
