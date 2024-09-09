package com.sku.fitizen.controller;

import com.sku.fitizen.domain.Board;
import com.sku.fitizen.domain.BoardFilesVO;
import com.sku.fitizen.domain.User;
import com.sku.fitizen.service.BoardService;
import com.sku.fitizen.service.FileService;
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
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/board")
@SessionAttributes("user")  // 세션에 저장된 "user" 정보를 사용
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private FileService fileService;

    // 게시글 목록 조회
    @GetMapping("/list")
    public String list(Model model) {
        List<Board> boardList = boardService.getBoardList();
        model.addAttribute("boards", boardList);
        return "th/board/list";
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
    public String write(@ModelAttribute Board board,
                        @SessionAttribute(value = "user", required = false) User user,
                        @RequestParam("files") List<MultipartFile> files) throws IOException {

        if (user == null) {
            return "redirect:/login/login";  // user가 없을 경우 로그인 페이지로 리다이렉트
        }

        // 세션에서 가져온 user의 id를 author에 설정
        board.setAuthor(user.getId());

        // 게시글과 파일을 함께 저장
        boardService.insertBoard(board, files);  // Board와 MultipartFile 리스트를 전달
        return "redirect:/board/list";
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
    public String edit(@ModelAttribute Board board,
                       @SessionAttribute(value = "user", required = false) User user,
                       @RequestParam("files") List<MultipartFile> files,
                       @RequestParam(value = "deleteFiles", required = false) List<Long> deleteFileIds) throws IOException {

        if (user == null) {
            return "redirect:/login/login";  // user가 없을 경우 로그인 페이지로 리다이렉트
        }

        // 수정 시에도 author 정보가 유지되도록 설정
        board.setAuthor(user.getId());

        // 삭제할 파일 처리
        if (deleteFileIds != null && !deleteFileIds.isEmpty()) {
            for (Long fnum : deleteFileIds) {
                fileService.deleteFileByFnum(fnum);  // 파일 삭제
            }
        }

        // 게시글과 파일 수정
        boardService.updateBoard(board, files);
        return "redirect:/board/view/" + board.getBno();
    }

    // 게시글 삭제
    @PostMapping("/delete/{bno}")
    public String delete(@PathVariable("bno") Long bno) {
        boardService.deleteBoard(bno);
        return "redirect:/board/list";
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
