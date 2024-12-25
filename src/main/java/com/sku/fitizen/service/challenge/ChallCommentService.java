package com.sku.fitizen.service.challenge;


import com.sku.fitizen.domain.challenge.ChallComment;
import com.sku.fitizen.mapper.challenge.ChallCommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class ChallCommentService {

    @Autowired
    ChallCommentMapper commentMapper;

    // 댓글 목록 불러오기
   public List<ChallComment> getChallCommentList(Integer challId)
   {

        List<ChallComment> list =commentMapper.challCommentList(challId);

       return list;
   }

   // 댓글 작성
    public  boolean addChallComment(ChallComment comment)
    {

        comment.setCommentDate(LocalDate.now());

        int result = commentMapper.addChallComment(comment);

        if(result == 1) return true;


        return  false;
    }

    //댓글 삭제
    public  boolean deleteChallComment(Map<String,Object> map)
    {

        int result = commentMapper.deleteChallComment(map);
        if(result == 1) return true;

        return  false;
    }

    //댓글 수정
    public  boolean editChallComment(ChallComment comment)
    {
        int result= commentMapper.editChallComment(comment);
        if(result == 1) return true;
        return  false;
    }
}
