package com.sku.fitizen.controller.board;

import com.sku.fitizen.domain.User;
import com.sku.fitizen.domain.board.BoardComment;
import com.sku.fitizen.service.board.BoardCommentService;
import lombok.extern.slf4j.Slf4j;
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

    private final BoardCommentService boardCommentService;

    public BoardCommentController(BoardCommentService boardCommentService) {
        this.boardCommentService = boardCommentService;
    }


    // 댓글 조회
    @GetMapping("/list")
    @ResponseBody
    public List<BoardComment> getComments(@RequestParam("bno") Long bno) {
        return boardCommentService.getCommentsByBoard(bno);
    }

    // 댓글 추가
    @PostMapping("/add")
    @ResponseBody
    public Map<String, Object> addComment(@RequestBody BoardComment comments,
                                          @SessionAttribute(value = "user", required = false) User user) {
        return execute(() -> {
            comments.setCommenter(user.getId());
            boardCommentService.addComment(comments);
        }, "댓글이 성공적으로 등록되었습니다.", "댓글 등록에 실패했습니다.");
    }

    // 댓글 수정
    @PostMapping("/edit")
    @ResponseBody
    public Map<String, Object> updateComment(@RequestBody BoardComment comments,
                                             @SessionAttribute(value = "user", required = false) User user) {
        if (!hasPermission(comments.getCommenter(), user)) {
            return createResponse(false, "댓글 수정 권한이 없습니다.");
        }

        return execute(() -> boardCommentService.updateComment(comments),
                "댓글이 성공적으로 수정되었습니다.", "댓글 수정에 실패했습니다.");
    }

    // 댓글 삭제
    @PostMapping("/delete")
    @ResponseBody
    public Map<String, Object> deleteComment(@RequestParam("cno") Long cno,
                                             @SessionAttribute(value = "user", required = false) User user) {
        return execute(() -> boardCommentService.deleteComment(cno, user.getId()),
                "댓글이 성공적으로 삭제되었습니다.", "댓글 삭제에 실패했습니다.");
    }

    // 공통 응답 생성
    private Map<String, Object> createResponse(boolean success, String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("message", message);
        return result;
    }

    // 공통 실행 메서드
    private Map<String, Object> execute(Runnable action, String successMessage, String errorMessage) {
        try {
            action.run();
            return createResponse(true, successMessage);
        } catch (Exception e) {
            log.error(errorMessage, e);
            return createResponse(false, errorMessage);
        }
    }

    // 권한 확인
    private boolean hasPermission(String commenterId, User user) {
        return user != null && commenterId.equals(user.getId());
    }
}
