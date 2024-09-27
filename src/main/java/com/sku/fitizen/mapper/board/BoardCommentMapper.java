package com.sku.fitizen.mapper.board;

import com.sku.fitizen.domain.board.BoardComment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BoardCommentMapper {

    // 댓글 개수 조회 (특정 게시글)
    int count(Long bno);

    // 댓글 조회 (특정 게시글 전체)
    List<BoardComment> selectAll(Long bno);

    // 특정 댓글 조회
    BoardComment select(Long cno);

    // 댓글 추가
    void insert(BoardComment comments);

    // 댓글 수정
    void update(BoardComment comments);

    // 댓글 삭제 상태 변경 (논리적 삭제)
    void markAsDeleted(@Param("cno") Long cno, @Param("commenter") String commenter);

    // 물리적 삭제 (논리적 삭제된 경우)
    void deletePhysically(Long cno);

    // 대댓글이 남아있는지 확인
    int countNonDeletedReplies(Long cno);

    // 대댓글의 부모 댓글 번호 찾기
    Long findParentCno(Long cno);

    int countCommentsByBoard(@Param("bno") Long bno, @Param("isDeleted") boolean isDeleted);

    boolean isDeleted(Long cno);

    void deleteAllByBoard(Long bno);
}
