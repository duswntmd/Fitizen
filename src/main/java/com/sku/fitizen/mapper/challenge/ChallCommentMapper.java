package com.sku.fitizen.mapper.challenge;

import com.sku.fitizen.domain.challenge.ChallComment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ChallCommentMapper {

    // 댓글 목록 불러오기
    List<ChallComment> challCommentList(Integer challlengeId);

    // 댓글 작성
    int addChallComment(ChallComment comment);

    // 댓글 삭제
    int  deleteChallComment(Map<String,Object> map);

    int  editChallComment(ChallComment comment);
}
