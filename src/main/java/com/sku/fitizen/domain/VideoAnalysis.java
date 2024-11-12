package com.sku.fitizen.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoAnalysis {
    private Long vnum;
    private String userid;
    private String realvideoname;
    private String uuidvideoname;
    private String videourl;
    private String aivideourl;
    private String videoresult;
}
