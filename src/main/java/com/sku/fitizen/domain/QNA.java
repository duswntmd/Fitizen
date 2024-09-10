package com.sku.fitizen.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
public class QNA {
    private int qid;
    private String title;
    private String author;
    private String contents;
    private String comments;
    private Date qdate;

        // Getters and Setters
    }

