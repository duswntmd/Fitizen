package com.sku.fitizen.mapper.board;

import com.sku.fitizen.domain.board.Board;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardMapper {

    // 게시글 목록 조회
    List<Board> getBoardList();

    // 게시글 조회 (bno 기준)
    Board getBoard(Long bno);

    // 게시글과 파일 정보를 함께 조회
    Board getBoardWithFiles(Long bno);

    // 게시글 삽입
    int insertBoard(Board board);

    // 게시글 수정
    int updateBoard(Board board);

    // 게시글 삭제
    int deleteBoard(Long bno);

    // 게시글 조회수 증가
    int updateHits(Long bno);

    // 제목과 작성자 기준으로 게시글 검색 (페이징 적용 가능)
    List<Board> searchBoardList(@Param("title") String title, @Param("author") String author);


}
