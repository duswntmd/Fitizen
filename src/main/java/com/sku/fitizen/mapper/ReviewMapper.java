package com.sku.fitizen.mapper;

import com.sku.fitizen.domain.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReviewMapper {

    void insertReview(Review review);

    List<Review> findReviewsByPlaceId(@Param("placeId") Long placeId);

    Review findReviewById(@Param("reviewId") Long reviewId);

    void updateReview(Review review);

    void deleteReview(Long reviewId);

}
