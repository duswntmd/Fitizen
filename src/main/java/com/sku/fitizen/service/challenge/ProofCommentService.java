package com.sku.fitizen.service.challenge;


import com.sku.fitizen.domain.challenge.ProofComment;
import com.sku.fitizen.mapper.challenge.ProofCommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProofCommentService {

    @Autowired
    ProofCommentMapper mapper;

    //사진 인증 게시판 댓글 작성하기
    public boolean addComment(ProofComment proofComment)
    {

        int result = mapper.addComment(proofComment);
        if(result == 1) return true;
        return false;
    }


    //사진 인증 게시판 댓글 삭제하기
    public boolean deleteProofComment(int proofCommentId)
    {

        int result = mapper.deleteProofComment(proofCommentId);
        if(result == 1) return true;
        return false;
    }

    // 댓글 수정
   public boolean editComment(int proofCommentId, String proofComment)
   {
       return  mapper.editComment(proofCommentId,proofComment)>0;

   }
}

