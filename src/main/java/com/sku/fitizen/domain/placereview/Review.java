package com.sku.fitizen.domain.placereview;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
public class Review {
    private Long id;
    private String content;
    private int rating;
    private String userId; // Foreign Key to USERS
    private Long placeId;  // Foreign Key to PLACES
    private Date createdDate;
    private String userName; // 사용자 이름 (JOIN으로 조회할 때 사용)
}