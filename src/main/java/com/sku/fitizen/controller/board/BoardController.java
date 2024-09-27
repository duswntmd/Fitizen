package com.sku.fitizen.controller.board;

import com.github.pagehelper.PageInfo;
import com.sku.fitizen.domain.User;
import com.sku.fitizen.domain.board.Board;
import com.sku.fitizen.domain.board.BoardComment;
import com.sku.fitizen.domain.board.BoardFilesVO;
import com.sku.fitizen.service.board.BoardCommentService;
import com.sku.fitizen.service.board.BoardService;
import com.sku.fitizen.service.board.FileService;
import com.sku.fitizen.service.board.PageService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/board")
@SessionAttributes("user")  // 세션에 저장된 "user" 정보를 사용
public class BoardController {



    @Autowired
    private BoardService boardService;

    @Autowired
    private FileService fileService;

    @Autowired
    private PageService pageService;

    @Autowired
    private BoardCommentService boardCommentService;

    // 게시글 목록 조회
    @GetMapping("/list")
    public String list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "5") int pageSize,
            Model model) {

        PageInfo<Board> pageInfo = pageService.getBoardList(pageNum, pageSize);  // 게시글 목록 조회
        Map<Long, Integer> likeCounts = new HashMap<>();

        // 각 게시글에 대한 좋아요 수 계산
        for (Board board : pageInfo.getList()) {
            int likeCount = boardService.getLikeCount(board.getBno());
            likeCounts.put(board.getBno(), likeCount);

            int commentCount = boardCommentService.getCommentCount(board.getBno(), false);  // 논리적 삭제 제외한 댓글 수 조회
            board.setCommentCount(commentCount);  // 댓글 수 설정
        }

        model.addAttribute("pageInfo", pageInfo);  // 페이지 정보 모델에 추가
        model.addAttribute("likeCounts", likeCounts);  // 좋아요 수 모델에 추가
        return "th/board/list";
    }

    // 검색 결과 조회 (검색 + 페이지네이션)
    @GetMapping("/search")
    public String search(
            @RequestParam String searchType,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "5") int pageSize,
            Model model) {

        // 검색 조건에 따라 검색 수행
        PageInfo<Board> pageInfo;
        if ("title".equals(searchType)) {
            pageInfo = boardService.searchBoardList(keyword, null, pageNum, pageSize);  // 제목으로 검색
        } else if ("author".equals(searchType)) {
            pageInfo = boardService.searchBoardList(null, keyword, pageNum, pageSize);  // 작성자로 검색
        } else {
            pageInfo = boardService.searchBoardList(null, null, pageNum, pageSize);  // 기본 목록
        }

        // 각 게시글에 대한 댓글 수와 좋아요 수 계산
        Map<Long, Integer> likeCounts = new HashMap<>();
        for (Board board : pageInfo.getList()) {
            int likeCount = boardService.getLikeCount(board.getBno());
            likeCounts.put(board.getBno(), likeCount);

            int commentCount = boardCommentService.getCommentCount(board.getBno(), false);  // 논리적 삭제 제외한 댓글 수 조회
            board.setCommentCount(commentCount);  // 댓글 수 설정
        }

        model.addAttribute("pageInfo", pageInfo);  // 검색된 결과 모델에 추가
        model.addAttribute("likeCounts", likeCounts);  // 좋아요 수 모델에 추가

        return "th/board/list";
    }

    // 게시글 조회
    @GetMapping("/view/{bno}")
    public String view(@PathVariable("bno") Long bno, HttpServletRequest request, HttpServletResponse response,
                       @SessionAttribute(value = "user", required = false) User user, Model model) {
        // 쿠키 확인
        Cookie[] cookies = request.getCookies();
        boolean hasViewed = false;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("viewedBoard_" + bno)) {
                    hasViewed = true;
                    break;
                }
            }
        }

        // 쿠키가 없으면 조회수 증가 중요한거면 세션을 사용해서 안전하게 사용
        if (!hasViewed) {
            boardService.increaseHits(bno);

            // 쿠키 설정 (유효 시간: 600초 = 10분)
            Cookie cookie = new Cookie("viewedBoard_" + bno, "true");
            cookie.setMaxAge(600);  // 10분 동안 유지
            cookie.setPath("/");  // 모든 경로에서 유효
            response.addCookie(cookie);
        }

        Board board = boardService.getBoard(bno);

        // 게시글에 첨부된 파일 목록 조회
        List<BoardFilesVO> files = fileService.getFilesByBoard(bno);
        List<BoardComment> comments = boardCommentService.getCommentsByBoard(bno);
        boolean likedByUser = boardService.isLikedByUser(bno, user.getId());

//        String contentWithImages = board.getContent();  // 기존 content 내용
//        for (BoardFilesVO file : files) {
//            if (file.getFtype().startsWith("image/")) {
//                // 이미지 경로를 content 안에 추가 (이미지 태그로)
//                String imageUrl = "/board/download/" + file.getFnum();
//                contentWithImages += "<img src='" + imageUrl + "' style='max-width: 100%;'/><br>";
//            }
//        }
//
//        board.setContent(contentWithImages);

        // 모델에 게시글과 파일 정보를 추가 및 좋아요
        model.addAttribute("comments", comments);
        model.addAttribute("likedByUser", likedByUser);
        model.addAttribute("board", board);
        model.addAttribute("files", files);

        return "th/board/view";
    }

//    @PostMapping("/uploadImage")
//    @ResponseBody
//    public Map<String, Object> uploadImage(@RequestParam("file") MultipartFile file) {
//        Map<String, Object> result = new HashMap<>();
//        try {
//            // 파일 저장 후 경로 반환
//            String imageUrl = fileService.storeFile(file);  // 파일 저장 후 URL 반환
//            result.put("success", true);
//            result.put("url", imageUrl);  // URL을 응답으로 보냄
//        } catch (Exception e) {
//            result.put("success", false);
//            result.put("message", "파일 업로드 실패");
//            e.printStackTrace();
//        }
//        return result;
//    }

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
                                     @RequestParam(value = "youtubeUrl", required = false) String youtubeUrl,
                                     @RequestParam("files") List<MultipartFile> files) throws IOException {

        Map<String, Object> result = new HashMap<>();

        if (user == null) {
            result.put("success", false);
            result.put("message", "로그인이 필요합니다.");
            return result;
        }

        board.setAuthor(user.getId());

        // 파일 개수 제한 (최대 5개)
        if (files.size() > 5) {
            result.put("success", false);
            result.put("message", "파일은 최대 5개까지 업로드할 수 있습니다.");
            return result;
        }

        // 파일 타입 체크: 동영상 파일 업로드 차단
        for (MultipartFile file : files) {
            String contentType = file.getContentType();
            if (contentType != null && contentType.startsWith("video/")) {
                result.put("success", false);
                result.put("message", "동영상 파일은 업로드할 수 없습니다.");
                return result;
            }
        }


        try {
            // 게시글과 파일, 유튜브 URL을 함께 저장
            boardService.insertBoard(board, files, youtubeUrl);

            result.put("success", true);
            result.put("message", "게시글이 성공적으로 작성되었습니다.");
        } catch (Exception e) {
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
    @ResponseBody  // JSON 응답을 위해 추가
    public Map<String, Object> edit(@ModelAttribute Board board,
                                    @SessionAttribute(value = "user", required = false) User user,
                                    @RequestParam("files") List<MultipartFile> files,
                                    @RequestParam(value = "deleteFiles", required = false) List<Long> deleteFileIds,
                                    @RequestParam(value = "youtubeUrl", required = false) String youtubeUrl) throws IOException {

        Map<String, Object> result = new HashMap<>();

        if (user == null) {
            result.put("success", false);
            result.put("message", "로그인이 필요합니다.");
            return result;
        }

        // 수정 시에도 author 정보가 유지되도록 설정
        board.setAuthor(user.getId());

        try {
            // 삭제할 파일 처리
            if (deleteFileIds != null && !deleteFileIds.isEmpty()) {
                for (Long fnum : deleteFileIds) {
                    fileService.deleteFileByFnum(fnum);  // 파일 삭제
                }
            }

            // 게시글과 파일 수정
            boardService.updateBoard(board, files, deleteFileIds, youtubeUrl);
            result.put("success", true);
            result.put("message", "게시글이 성공적으로 수정되었습니다.");
        } catch (Exception e) {
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
        // 파일 정보 조회
        BoardFilesVO file = fileService.getFileByFnum(fnum);

        // 파일을 리소스로 로드
        Path filePath = fileService.getFileStorageLocation().resolve(file.getUuidName()).normalize();
        Resource resource = fileService.loadFileAsResource(file.getUuidName());

        // 파일을 리소스로 반환
        if (resource.exists()) {
            // 파일 이름을 UTF-8로 인코딩
            String encodedFileName = URLEncoder.encode(file.getRealName(), "UTF-8").replace("+", "%20");

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
                    .body(resource);
        } else {
            throw new IOException("파일을 찾을 수 없습니다.");
        }
    }

    // 좋아요 추가
    @PostMapping("/like")
    @ResponseBody
    public Map<String, Object> addLike(@RequestParam Long bno,
                                       @SessionAttribute(value = "user", required = false) User user) {
        Map<String, Object> result = new HashMap<>();

        if (user == null) {
            result.put("success", false);
            result.put("message", "로그인이 필요합니다.");
            return result;
        }

        boolean alreadyLiked = boardService.isLikedByUser(bno, user.getId());

        if (alreadyLiked) {
            result.put("success", false);
            result.put("message", "이미 좋아요를 눌렀습니다.");
        } else {
            boardService.addLike(bno, user.getId());
//            boardService.incrementLikes(bno);
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

        if (user == null) {
            result.put("success", false);
            result.put("message", "로그인이 필요합니다.");
            return result;
        }

        boolean alreadyLiked = boardService.isLikedByUser(bno, user.getId());

        if (!alreadyLiked) {
            result.put("success", false);
            result.put("message", "좋아요를 누르지 않았습니다.");
        } else {
            boardService.removeLike(bno, user.getId());
//            boardService.decrementLikes(bno);
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

}
