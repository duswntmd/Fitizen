package com.sku.fitizen.mapper;

import com.sku.fitizen.domain.Board;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardMapper {

    // 게시글 목록 조회
    List<Board> getBoardList();

    // 게시글 조회 (bno 기준)
    Board getBoard(Long bno);

    // 게시글 삽입
    void insertBoard(Board board);

    // 게시글 수정
    void updateBoard(Board board);

    // 게시글 삭제
    void deleteBoard(Long bno);

    // 게시글 조회수 증가
    void updateHits(Long bno);

    // 제목과 작성자 기준으로 게시글 검색 (페이징 적용 가능)
    List<Board> searchBoardList(@Param("title") String title, @Param("author") String author);

}
