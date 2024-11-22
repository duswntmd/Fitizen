package com.sku.fitizen.service;

import com.sku.fitizen.domain.VideoAnalysis;
import com.sku.fitizen.mapper.VideoAnalysisMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VideoAnalysisService {

    private final VideoAnalysisMapper videoAnalysisMapper;

    @Autowired
    public VideoAnalysisService(VideoAnalysisMapper videoAnalysisMapper) {
        this.videoAnalysisMapper = videoAnalysisMapper;
    }

    public List<VideoAnalysis> getVideosByUser(String username) {
        return videoAnalysisMapper.getVideosByUser(username);
    }

    public VideoAnalysis getVideoAnalysisDetail(int vnum, String userid) {
        Map<String, Object> params = new HashMap<>();
        params.put("vnum", vnum);
        params.put("userid", userid);
        return videoAnalysisMapper.getVideoAnalysdetail(params);
    }


    public int insertVideoAnalysis(VideoAnalysis videoAnalysis) {
        return videoAnalysisMapper.insertVideoAnalysis(videoAnalysis);
    }

    public int updateVideoAnalysisResult(VideoAnalysis videoAnalysis) {
        return videoAnalysisMapper.updateVideoAnalysisResult(videoAnalysis);
    }

}
