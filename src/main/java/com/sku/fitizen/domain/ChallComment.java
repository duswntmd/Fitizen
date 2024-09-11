package com.sku.fitizen.domain;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChallComment {

        private int commentId ;
        private int challengeId;
        private String userId;
        private String challComment;
        private LocalDate commentDate;

}
