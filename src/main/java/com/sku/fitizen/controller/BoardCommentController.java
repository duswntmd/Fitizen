package com.sku.fitizen.controller;

import com.sku.fitizen.domain.BoardComment;
import com.sku.fitizen.domain.User;
import com.sku.fitizen.service.BoardCommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/comments")
@SessionAttributes("user")
public class BoardCommentController {

    @Autowired
    private BoardCommentService boardCommentService;

    // 댓글 조회 (AJAX 요청으로 특정 게시글의 댓글을 가져옴)
    @GetMapping("/list")
    @ResponseBody
    public List<BoardComment> getComments(@RequestParam("bno") Long bno) {
        List<BoardComment> comments = boardCommentService.getCommentsByBoard(bno);
        return comments;
    }

    // 댓글 추가
    @PostMapping("/add")
    @ResponseBody
    public Map<String, Object> addComment(@RequestBody BoardComment comments,
                                          @SessionAttribute(value = "user", required = false) User user) {
        Map<String, Object> result = new HashMap<>();

        if (user == null) {
            result.put("success", false);
            result.put("message", "로그인이 필요합니다.");
            return result;
        }

        try {
            comments.setCommenter(user.getId());
            boardCommentService.addComment(comments);;
            result.put("success", true);
            result.put("message", "댓글이 성공적으로 등록되었습니다.");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "댓글 등록에 실패했습니다.");
        }

        return result;
    }

    // 댓글 수정
    @PostMapping("/edit")
    @ResponseBody
    public Map<String, Object> updateComment(@RequestBody BoardComment comments,
                                             @SessionAttribute(value = "user", required = false) User user) {
        Map<String, Object> result = new HashMap<>();

        if (user == null || !comments.getCommenter().equals(user.getId())) {
            result.put("success", false);
            result.put("message", "댓글 수정 권한이 없습니다.");
            return result;
        }

        try {
            boardCommentService.updateComment(comments);
            result.put("success", true);
            result.put("message", "댓글이 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "댓글 수정에 실패했습니다.");
        }

        return result;
    }

    // 댓글 삭제
    @PostMapping("/delete")
    @ResponseBody
    public Map<String, Object> deleteComment(@RequestParam("cno") Long cno,
                                             @SessionAttribute(value = "user", required = false) User user) {
        Map<String, Object> result = new HashMap<>();

        try {
            boardCommentService.deleteComment(cno, user.getId());
            result.put("success", true);
            result.put("message", "댓글이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "댓글 삭제에 실패했습니다.");
        }

        return result;
    }
}
