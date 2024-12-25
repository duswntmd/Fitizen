package com.sku.fitizen.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoAnalysis {
    private int vnum;
    private String userid;
    private String realvideoname;
    private String uuidvideoname;
    private String aivideourl;
    private String aianswer;
    private String videoresult;
    private Date REGDATE;
}