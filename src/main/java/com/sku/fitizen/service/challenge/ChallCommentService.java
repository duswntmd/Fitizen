package com.sku.fitizen.service.challenge;


import com.sku.fitizen.domain.ChallComment;
import com.sku.fitizen.mapper.challenge.challCommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChallCommentService {

    @Autowired
    challCommentMapper commentMapper;


   public List<ChallComment> getChallCommentList(Integer challId)
   {

        List<ChallComment> list =commentMapper.challCommentList(challId);


       return list;
   }

}
