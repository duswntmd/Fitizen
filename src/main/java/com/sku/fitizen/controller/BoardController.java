package com.sku.fitizen.controller;

import com.github.pagehelper.PageInfo;
import com.sku.fitizen.domain.Board;
import com.sku.fitizen.domain.BoardFilesVO;
import com.sku.fitizen.domain.User;
import com.sku.fitizen.service.BoardService;
import com.sku.fitizen.service.FileService;
import com.sku.fitizen.service.PageService;
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

    // 게시글 목록 조회
    @GetMapping("/list")
    public String list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "5") int pageSize,
            Model model) {

        PageInfo<Board> pageInfo = pageService.getBoardList(pageNum, pageSize);  // 게시글 목록 조회
        model.addAttribute("pageInfo", pageInfo);  // 페이지 정보 모델에 추가
        return "th/board/list";
    }

    // 검색 결과 조회 (검색 + 페이지네이션)
    @GetMapping("/search")
    public String search(
            @RequestParam String searchType,  // 제목 또는 작성자
            @RequestParam String keyword,     // 검색어
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

        model.addAttribute("pageInfo", pageInfo);  // 검색된 결과 모델에 추가
        return "th/board/list";  // 동일한 템플릿에서 결과를 표시
    }

    // 게시글 조회
    @GetMapping("/view/{bno}")
    public String view(@PathVariable("bno") Long bno, Model model) {
        Board board = boardService.getBoard(bno);

        // 게시글에 첨부된 파일 목록 조회
        List<BoardFilesVO> files = fileService.getFilesByBoard(bno);

        // 모델에 게시글과 파일 정보를 추가
        model.addAttribute("board", board);
        model.addAttribute("files", files);

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
    @ResponseBody  // JSON 응답을 위해 추가
    public Map<String, Object> write(@ModelAttribute Board board,
                                     @SessionAttribute(value = "user", required = false) User user,
                                     @RequestParam("files") List<MultipartFile> files) throws IOException {

        Map<String, Object> result = new HashMap<>();

        if (user == null) {
            result.put("success", false);
            result.put("message", "로그인이 필요합니다.");
            return result;  // 로그인 페이지로 리다이렉트할 필요 없이 메시지 반환
        }

        // 세션에서 가져온 user의 id를 author에 설정
        board.setAuthor(user.getId());

        try {
            // 게시글과 파일을 함께 저장
            boardService.insertBoard(board, files);
            result.put("success", true);
            result.put("message", "게시글이 성공적으로 작성되었습니다.");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "게시글 작성에 실패했습니다.");
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
                                    @RequestParam(value = "deleteFiles", required = false) List<Long> deleteFileIds) throws IOException {

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
            boardService.updateBoard(board, files, deleteFileIds);
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
}
