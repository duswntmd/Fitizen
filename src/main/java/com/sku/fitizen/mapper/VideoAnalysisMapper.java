package com.sku.fitizen.mapper;

import com.sku.fitizen.domain.VideoAnalysis;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VideoAnalysisMapper {

    List<VideoAnalysis> getVideosByUser(String userid);

    // 비디오 분석 데이터 삽입
    int insertVideoAnalysis(VideoAnalysis videoAnalysis);

    // 분석 결과 업데이트
    void updateVideoAnalysisResult(VideoAnalysis videoAnalysis);

    // 특정 비디오 분석 데이터 조회
    VideoAnalysis getVideoAnalysisById(@Param("vnum") long vnum);

    // 모든 비디오 분석 데이터 조회
    List<VideoAnalysis> getAllVideoAnalysis();

    // 특정 비디오 분석 데이터 삭제
    int deleteVideoAnalysis(@Param("vnum") long vnum);
}
