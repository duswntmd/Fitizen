package com.sku.fitizen.service;

import com.sku.fitizen.mapper.VideoAnalysisMapper;
import com.sku.fitizen.domain.VideoAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoAnalysisService {

    private final VideoAnalysisMapper videoAnalysisMapper;

    public List<VideoAnalysis> getVideosByUser(String username) {
        return videoAnalysisMapper.getVideosByUser(username);
    }

    @Autowired
    public VideoAnalysisService(VideoAnalysisMapper videoAnalysisMapper) {
        this.videoAnalysisMapper = videoAnalysisMapper;
    }

    // 비디오 분석 데이터 추가
    public void insertVideoAnalysis(VideoAnalysis videoAnalysis) {
        videoAnalysisMapper.insertVideoAnalysis(videoAnalysis);
    }

    public void updateVideoAnalysisResult(VideoAnalysis videoAnalysis) {
        videoAnalysisMapper.updateVideoAnalysisResult(videoAnalysis);
    }

    // 특정 비디오 분석 데이터 조회
    public VideoAnalysis getVideoAnalysisById(long vnum) {
        return videoAnalysisMapper.getVideoAnalysisById(vnum);
    }

    // 모든 비디오 분석 데이터 조회
    public List<VideoAnalysis> getAllVideoAnalysis() {
        return videoAnalysisMapper.getAllVideoAnalysis();
    }

    // 비디오 분석 데이터 삭제
    public int deleteVideoAnalysis(long vnum) {
        return videoAnalysisMapper.deleteVideoAnalysis(vnum);
    }
}
