package com.sku.fitizen.mapper.board;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BoardLikeMapper {

    // 좋아요 추가
    int insertLike(@Param("bno") Long bno, @Param("userId") String userId);

    // 좋아요 삭제
    int deleteLike(@Param("bno") Long bno, @Param("userId") String userId);

    // 특정 게시글에 대한 유저의 좋아요 여부 확인
    int checkLike(@Param("bno") Long bno, @Param("userId") String userId);

    // 게시글별 좋아요 수 계산
    int countLikes(@Param("bno") Long bno);

    // 게시판 좋아요 전부 삭제
    void deleteAllByBoard(Long bno);


}
