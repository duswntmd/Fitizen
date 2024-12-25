package com.sku.fitizen.domain.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardComment {
    private Long cno;        // 댓글 번호
    private Long pcno;       // 대댓글일 경우 상위 댓글 번호
    private Long bno;        // 게시글 번호 (외래키)
    private String comments; // 댓글 내용
    private String commenter; // 댓글 작성자 (외래키, users의 id)
    private Date regDate;    // 댓글 작성일
    private Date upDate;     // 댓글 수정일
    private int depth;
    private boolean isDeleted;
}