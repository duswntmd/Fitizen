package com.sku.fitizen.controller.challenge;


import com.sku.fitizen.domain.challenge.ProofComment;
import com.sku.fitizen.domain.User;
import com.sku.fitizen.service.challenge.ProofCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/proofComment")
public class ProofCommentController {

    @Autowired
    ProofCommentService service;

    //사진 인증 게시판 댓글 작성하기
    @PostMapping("/addComment")
    @ResponseBody
    public Map<String,Boolean> challComment(@SessionAttribute(value = "user") User user  ,
                                            ProofComment proofComment)
    {

        boolean success = service.addComment(proofComment);
        Map<String,Boolean> map = new HashMap<>();
        map.put("success",success);
        return map;
    }

    //사진 인증 게시판 댓글 삭제하기
    @GetMapping("/deleteComment/{proofCommentId}")
    @ResponseBody
    public Map<String ,Boolean> deleteProofComment(@SessionAttribute("user") User user,@PathVariable int  proofCommentId)
    {
        Map<String ,Boolean> map = new HashMap<>();

        if(user !=null) {
            boolean deleted = service.deleteProofComment(proofCommentId);

            map.put("deleted", deleted);
        }
        return map;

    }

    //사진 인증 게시판 댓글 수정하기
    @PostMapping("/editComment/{commentId}")
    public ResponseEntity<?> editComment(@PathVariable int commentId, @RequestParam String proofComment) {
        System.out.println("Received commentId: " + commentId + ", proofComment: " + proofComment);
        boolean success = service.editComment(commentId, proofComment);
        return ResponseEntity.ok(Map.of("success", success));

    }


}
