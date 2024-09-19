package com.sku.fitizen.service;

import com.sku.fitizen.domain.BoardComment;
import com.sku.fitizen.mapper.BoardCommentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class BoardCommentService {

    @Autowired
    private BoardCommentMapper boardCommentMapper;

    public int getCommentCount(Long bno, boolean isDeleted) {
        return boardCommentMapper.countCommentsByBoard(bno, isDeleted);
    }
    // 댓글 개수 조회
    public int countComments(Long bno) {
        return boardCommentMapper.count(bno);
    }

    // 특정 게시글의 전체 댓글 조회
    public List<BoardComment> getCommentsByBoard(Long bno) {
        return boardCommentMapper.selectAll(bno);
    }

    // 특정 댓글 조회
    public BoardComment getComment(Long cno) {
        return boardCommentMapper.select(cno);
    }

    // 댓글 추가
    @Transactional
    public void addComment(BoardComment comments) {
        boardCommentMapper.insert(comments);
    }

    // 댓글 수정
    @Transactional
    public void updateComment(BoardComment comments) {
        boardCommentMapper.update(comments);
    }

    @Transactional
    public void deleteComment(Long cno, String commenter) {
        // 댓글 논리적 삭제 처리
        boardCommentMapper.markAsDeleted(cno, commenter);

        // 자식 댓글이 모두 삭제되었는지 확인하고 부모 댓글이 논리적으로 삭제되었는지 확인
        Long parentCno = boardCommentMapper.findParentCno(cno);

        if (parentCno != null && boardCommentMapper.countNonDeletedReplies(parentCno) == 0) {
            // 부모 댓글이 논리적으로 삭제되었고, 자식 댓글이 없으면 부모 댓글 물리적 삭제
            if (boardCommentMapper.isDeleted(parentCno)) {
                boardCommentMapper.deletePhysically(parentCno);
            }
        }

        // 자식 댓글이 모두 삭제된 경우 부모 댓글 물리적 삭제
        if (boardCommentMapper.countNonDeletedReplies(cno) == 0) {
            boardCommentMapper.deletePhysically(cno);
        }
    }
}
