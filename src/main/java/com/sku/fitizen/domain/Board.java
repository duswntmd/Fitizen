package com.sku.fitizen.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    private Long bno;
    private String title;
    private String content;
    private String author;
    private Date regDate;
    private int hits;
    private Date upDate;
    private int likes;
    private int commentCnt;
}
