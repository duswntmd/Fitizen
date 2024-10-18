package com.sku.fitizen.domain.challenge;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChallComment {

        private int commentId ;
        private int challengeId;
        private String userId;
        private String challComment;
        private LocalDate commentDate;
        private Integer parentCommentId;

        @Override
        public String toString() {
                return "ChallComment [commentId=" + commentId + ", challengeId=" + challengeId
                        + ", userId=" + userId + ", challComment=" + challComment + ", commentDate="
                        + commentDate + ", parentCommentId=" + parentCommentId + "]";
        }
}
