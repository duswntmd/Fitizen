package com.sku.fitizen.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardFilesVO {
    private Long fnum;
    private Long bno;
    private String realName;
    private String uuidName;
    private Long fsize;
    private String ftype;
    private String youtubeUrl;
}
