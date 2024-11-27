package com.sku.fitizen.controller.board;

import com.github.pagehelper.PageInfo;
import com.sku.fitizen.domain.User;
import com.sku.fitizen.domain.board.Board;
import com.sku.fitizen.domain.board.BoardFilesVO;
import com.sku.fitizen.service.board.BoardCommentService;
import com.sku.fitizen.service.board.BoardService;
import com.sku.fitizen.service.board.FileService;
import com.sku.fitizen.service.board.PageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/board")
@SessionAttributes("user")  // 세션에 저장된 "user" 정보를 사용
public class BoardController {

    private final BoardService boardService;
    private final FileService fileService;
    private final PageService pageService;
    private final BoardCommentService boardCommentService;

    public BoardController(BoardService boardService, FileService fileService, PageService pageService, BoardCommentService boardCommentService) {
        this.boardService = boardService;
        this.fileService = fileService;
        this.pageService = pageService;
        this.boardCommentService = boardCommentService;
    }

    // 게시글 목록 조회
    @GetMapping("/list")
    public String list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "5") int pageSize,
            Model model) {

        PageInfo<Board> pageInfo = pageService.getBoardList(pageNum, pageSize);
        Map<Long, Integer> likeCounts = processBoardDetails(pageInfo.getList());

        model.addAttribute("pageInfo", pageInfo);  // 페이지 정보 모델에 추가
        model.addAttribute("likeCounts", likeCounts);  // 좋아요 수 모델에 추가

        return "th/board/list";
    }

    @GetMapping("/search")
    public String search(
            @RequestParam String searchType,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "5") int pageSize,
            Model model) {

        PageInfo<Board> pageInfo = boardService.searchBoardList(searchType, keyword, pageNum, pageSize);
        Map<Long, Integer> likeCounts = processBoardDetails(pageInfo.getList());

        model.addAttribute("pageInfo", pageInfo);  // 검색된 결과 모델에 추가
        model.addAttribute("likeCounts", likeCounts);  // 좋아요 수 모델에 추가

        return "th/board/list";
    }

    // 게시글 조회
    @GetMapping("/view/{bno}")
    public String view(@PathVariable("bno") Long bno,
                       HttpServletRequest request,
                       HttpServletResponse response,
                       @SessionAttribute(value = "user", required = false) User user,
                       Model model) {

        // 조회수 증가 및 쿠키 처리
        boardService.updateViewCount(bno, request, response);
        // 게시글 상세 정보 가져오기
        Map<String, Object> boardDetails = boardService.getBoardDetails(bno, user.getId());

        // 모델에 데이터 추가
        model.addAttribute("board", boardDetails.get("board"));
        model.addAttribute("files", boardDetails.get("files"));
        model.addAttribute("comments", boardDetails.get("comments"));
        model.addAttribute("likedByUser", boardDetails.get("likedByUser"));

        return "th/board/view";
    }

    // 게시글 작성 페이지로 이동
    @GetMapping("/write")
    public String writeForm(Model model) {
        model.addAttribute("board", new Board());
        return "th/board/write";
    }

    // 게시글 작성 처리
    @PostMapping("/write")
    @ResponseBody
    public Map<String, Object> write(@ModelAttribute Board board,
                                     @SessionAttribute(value = "user", required = false) User user,
                                     @RequestParam(value = "youtubeUrls", required = false) List<String> youtubeUrls, // 리스트로 받음
                                     @RequestParam("files") List<MultipartFile> files) {

        Map<String, Object> result = new HashMap<>();
        board.setAuthor(user.getId());
        try {
            // 파일 검증
            fileService.validateFiles(files);
            Long bno = boardService.insertBoard(board, files, youtubeUrls);  // List<String>으로 전달

            result.put("success", true);
            result.put("message", "게시글이 성공적으로 작성되었습니다.");
            result.put("bno", bno);
        }catch (IllegalArgumentException e) {
            // 사용자 예외 처리
            result.put("success", false);
            result.put("message", e.getMessage());
        }catch (Exception e) {
            result.put("success", false);
            result.put("message", "게시글 작성에 실패했습니다.");
            e.printStackTrace();
        }

        return result;
    }


    // 게시글 수정 페이지로 이동
    @GetMapping("/edit/{bno}")
    public String editForm(@PathVariable("bno") Long bno, Model model) {
        Board board = boardService.getBoard(bno);
        List<BoardFilesVO> files = fileService.getFilesByBoard(bno);

        // 모델에 게시글과 파일 정보를 추가
        model.addAttribute("board", board);
        model.addAttribute("files", files);
        return "th/board/edit";
    }

    // 게시글 수정 처리
    @PostMapping("/edit")
    @ResponseBody
    public Map<String, Object> edit(@ModelAttribute Board board,
                                    @SessionAttribute(value = "user", required = false) User user,
                                    @RequestParam("files") List<MultipartFile> files,
                                    @RequestParam(value = "deleteFiles", required = false) List<Long> deleteFileIds,
                                    @RequestParam(value = "youtubeUrls", required = false) List<String> youtubeUrls) {

        Map<String, Object> result = new HashMap<>();

        try {
            board.setAuthor(user.getId());
            // 게시글과 파일 수정, 유튜브 URL 저장
            boardService.validateAndUpdateBoard(board, files, deleteFileIds, youtubeUrls); // youtubeUrls를 List로 전달
            result.put("success", true);
            result.put("message", "게시글이 성공적으로 수정되었습니다.");
        } catch (IllegalArgumentException e) {
            result.put("success", false);
            result.put("message", e.getMessage()); // 예외 메시지를 사용자에게 반환
        }catch (Exception e) {
            result.put("success", false);
            result.put("message", "게시글 수정에 실패했습니다.");
        }

        return result;
    }

    // 게시글 삭제
    @PostMapping("/delete/{bno}")
    @ResponseBody  // JSON 응답을 위해 추가
    public Map<String, Object> delete(@PathVariable("bno") Long bno) {
        Map<String, Object> result = new HashMap<>();
        try {
            boardService.deleteBoard(bno);
            result.put("success", true);
            result.put("message", "게시글이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "게시글 삭제에 실패했습니다.");
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping("/download/{fnum}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fnum") Long fnum) throws IOException {
        return fileService.downloadFile(fnum);
    }

    // 좋아요 추가
    @PostMapping("/like")
    @ResponseBody
    public Map<String, Object> addLike(@RequestParam Long bno,
                                       @SessionAttribute(value = "user", required = false) User user) {
        Map<String, Object> result = new HashMap<>();

        boolean alreadyLiked = boardService.isLikedByUser(bno, user.getId());

        if (alreadyLiked) {
            result.put("success", false);
            result.put("message", "이미 좋아요를 눌렀습니다.");
        } else {
            boardService.addLike(bno, user.getId());
            result.put("success", true);
            result.put("message", "좋아요를 눌렀습니다.");
        }

        return result;
    }

    // 좋아요 취소
    @PostMapping("/unlike")
    @ResponseBody
    public Map<String, Object> removeLike(@RequestParam Long bno,
                                          @SessionAttribute(value = "user", required = false) User user) {
        Map<String, Object> result = new HashMap<>();

        boolean alreadyLiked = boardService.isLikedByUser(bno, user.getId());

        if (!alreadyLiked) {
            result.put("success", false);
            result.put("message", "좋아요를 누르지 않았습니다.");
        } else {
            boardService.removeLike(bno, user.getId());
            result.put("success", true);
            result.put("message", "좋아요를 취소했습니다.");
        }

        return result;
    }

    // 특정 게시글 좋아요 수 조회
    @GetMapping("/like-count")
    public Map<String, Object> getLikeCount(@RequestParam Long bno) {
        Map<String, Object> result = new HashMap<>();
        int likeCount = boardService.getLikeCount(bno);
        result.put("likeCount", likeCount);
        return result;
    }

    private Map<Long, Integer> processBoardDetails(List<Board> boards) {
        Map<Long, Integer> likeCounts = new HashMap<>();

        for (Board board : boards) {
            int likeCount = boardService.getLikeCount(board.getBno());
            likeCounts.put(board.getBno(), likeCount);

            int commentCount = boardCommentService.getCommentCount(board.getBno(), false);  // 논리적 삭제 제외한 댓글 수 조회
            board.setCommentCount(commentCount);  // 댓글 수 설정
        }

        return likeCounts;
    }

}
