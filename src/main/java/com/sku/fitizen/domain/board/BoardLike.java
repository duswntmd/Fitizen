package com.sku.fitizen.domain.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardLike {
    private Long id;
    private Long bno;
    private String userId;
}