package com.sku.fitizen.mapper.challenge;


import com.sku.fitizen.domain.challenge.ProofComment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProofCommentMapper {

    //사진 인증 게시판 댓글 작성하기
    int addComment(ProofComment proofComment);

    //사진 인증 게시판 댓글 수정하기
    int editComment(int proofCommentId, String proofComment);
    //사진 인증 게시판 댓글 삭제하기
    int deleteProofComment(int proofCommentId);
}
