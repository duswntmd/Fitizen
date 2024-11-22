package com.sku.fitizen.mapper;

import com.sku.fitizen.domain.VideoAnalysis;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface VideoAnalysisMapper {

    // 비디오 분석 리스트
    List<VideoAnalysis> getVideosByUser(String userid);

    // 비디오 분석 데이터 삽입
    int insertVideoAnalysis(VideoAnalysis videoAnalysis);

    // 분석 결과 업데이트
    int updateVideoAnalysisResult(VideoAnalysis videoAnalysis);

    // 특정 비디오 상세 정보 조회
    VideoAnalysis getVideoAnalysdetail(Map<String, Object> params);

}
