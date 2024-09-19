package com.sku.fitizen.controller.challenge;


import com.sku.fitizen.domain.challenge.ChallComment;
import com.sku.fitizen.domain.User;
import com.sku.fitizen.service.challenge.ChallCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/chellComment")
public class ChallCommentController {

        @Autowired
        private ChallCommentService service;

        // 댓글 작성
        @PostMapping("/addChallComment")
        @ResponseBody
        public Map<String,Boolean> challComment(@SessionAttribute(value = "user" ,required = false) User user  ,
                                                ChallComment challComment)
        {
            boolean success = service.addChallComment(challComment);
            Map<String,Boolean> map = new HashMap<>();
            map.put("success",success);


            return map;
        }

        // 댓글 삭제
        @GetMapping("/deleteChallComment/{commentId}")
        @ResponseBody
        public Map<String,Boolean> deleteChallComment(@SessionAttribute("user") User user,@PathVariable("commentId") int commentId)
        {
            Map<String,Boolean> map = new HashMap<>();

            Map<String,Object> data= new HashMap<>();
            data.put("userId", user.getId());
            data.put("commentId", commentId);

            boolean deleted = service.deleteChallComment(data);

            map.put("deleted",deleted);

            return  map;
        }


        // 댓글 수정
        @PostMapping("/editChallComment")
        @ResponseBody
        public Map<String,Boolean> editChallComment(ChallComment challComment,@SessionAttribute(value = "user") User user)
        {
            challComment.setUserId(user.getId());
            Map<String,Boolean> map = new HashMap<>();
            boolean success  = service.editChallComment(challComment);
            map.put("success",success);

            return map;
        }



}
