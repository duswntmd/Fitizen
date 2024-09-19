package com.sku.fitizen.domain;

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
