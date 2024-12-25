package com.sku.fitizen.service.board;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sku.fitizen.domain.board.Board;
import com.sku.fitizen.domain.board.BoardComment;
import com.sku.fitizen.domain.board.BoardFilesVO;
import com.sku.fitizen.mapper.board.BoardCommentMapper;
import com.sku.fitizen.mapper.board.BoardLikeMapper;
import com.sku.fitizen.mapper.board.BoardMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class BoardService {

    private final BoardMapper boardMapper;
    private final FileService fileService;
    private final BoardLikeMapper boardLikeMapper;
    private final BoardCommentMapper boardCommentMapper;

    public BoardService(BoardMapper boardMapper, FileService fileService, BoardLikeMapper boardLikeMapper, BoardCommentMapper boardCommentMapper) {
        this.boardMapper = boardMapper;
        this.fileService = fileService;
        this.boardLikeMapper = boardLikeMapper;
        this.boardCommentMapper = boardCommentMapper;
    }


    // 게시글 조회
    public Board getBoard(Long bno) {
        Board board = boardMapper.getBoard(bno);
        return board;
    }

    // 게시글 생성 및 파일 저장
    @Transactional
    public Long insertBoard(Board board, List<MultipartFile> files, List<String> youtubeUrls) throws IOException {
        // 게시글 저장
        boardMapper.insertBoard(board);

        Long bno = board.getBno();

        // 파일 저장
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    // 파일 저장 로직
                    fileService.storeFileOrYoutube(null, file, board.getBno());
                }
            }
        }

        // 유튜브 URL 저장
        if (youtubeUrls != null && !youtubeUrls.isEmpty()) {
            for (String youtubeUrl : youtubeUrls) {
                fileService.storeFileOrYoutube(youtubeUrl, null, board.getBno());
            }
        }
        return bno;
    }

    // 게시글 수정
    @Transactional
    public void updateBoard(Board board, List<MultipartFile> files, List<Long> deleteFileIds, List<String> youtubeUrls) throws IOException {
        // 게시글 수정
        boardMapper.updateBoard(board);

        // 파일 처리
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    fileService.storeFileOrYoutube(null, file, board.getBno());
                }
            }
        }

        // 유튜브 URL 처리 (기존 유튜브 URL 삭제 후 새로 추가하는 방식)
        if (youtubeUrls != null && !youtubeUrls.isEmpty()) {
            for (String youtubeUrl : youtubeUrls) {
                if (youtubeUrl != null && !youtubeUrl.isEmpty()) {
                    fileService.storeFileOrYoutube(youtubeUrl, null, board.getBno());
                }
            }
        }
    }

    // 게시글 삭제 및 파일 삭제
    @Transactional
    public void deleteBoard(Long bno) {
        // 1. 댓글 먼저 삭제
        boardCommentMapper.deleteAllByBoard(bno);

        // 2. 좋아요 삭제
        boardLikeMapper.deleteAllByBoard(bno);

        // 3. 게시글에 속한 파일들 먼저 삭제
        fileService.deleteFilesByBoard(bno);

        // 4. 파일 삭제가 완료된 후 게시글 삭제
        boardMapper.deleteBoard(bno);
    }

    public PageInfo<Board> searchBoardList(String searchType, String keyword, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);  // 페이지 설정
        List<Board> boards;

        // 검색 타입에 따라 쿼리 수행
        if ("title".equals(searchType)) {
            boards = boardMapper.searchBoardList(keyword, null);  // 제목으로 검색
        } else if ("author".equals(searchType)) {
            boards = boardMapper.searchBoardList(null, keyword);  // 작성자로 검색
        } else {
            boards = boardMapper.searchBoardList(null, null);  // 기본 목록
        }

        return new PageInfo<>(boards);  // PageInfo로 결과 반환
    }

    @Transactional
    public int increaseHits(Long bno) {
        return boardMapper.updateHits(bno);
    }

    public boolean isLikedByUser(Long bno, String userId) {
        return boardLikeMapper.checkLike(bno, userId) > 0;
    }

    @Transactional
    public int addLike(Long bno, String userId) {
        return boardLikeMapper.insertLike(bno, userId);
    }

    @Transactional
    public int removeLike(Long bno, String userId) {
        return boardLikeMapper.deleteLike(bno, userId);
    }

    public int getLikeCount(Long bno) {
        return boardLikeMapper.countLikes(bno);
    }

    // 조회수 증가 및 쿠키 처리
    public boolean updateViewCount(Long bno, HttpServletRequest request, HttpServletResponse response) {
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

        if (!hasViewed) {
            increaseHits(bno);  // 조회수 증가
            Cookie cookie = new Cookie("viewedBoard_" + bno, "true");
            cookie.setMaxAge(600);  // 10분 동안 유지
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        return hasViewed;
    }

    // 게시글 상세 정보 가져오기
    public Map<String, Object> getBoardDetails(Long bno, String userId) {
        Map<String, Object> details = new HashMap<>();

        Board board = getBoard(bno);  // 게시글 정보
        List<BoardFilesVO> files = fileService.getFilesByBoard(bno);  // 첨부 파일
        List<BoardComment> comments = boardCommentMapper.selectAll(bno);  // 댓글
        boolean likedByUser = isLikedByUser(bno, userId);  // 좋아요 여부

        details.put("board", board);
        details.put("files", files);
        details.put("comments", comments);
        details.put("likedByUser", likedByUser);

        return details;
    }

    @Transactional
    public void validateAndUpdateBoard(Board board, List<MultipartFile> files, List<Long> deleteFileIds, List<String> youtubeUrls) throws IOException {
        // 기존 파일 목록 조회
        List<BoardFilesVO> existingFiles = fileService.getFilesByBoard(board.getBno());

        // 파일 및 유튜브 URL 검증
        fileService.validateFilesForUpdate(files, deleteFileIds, existingFiles);
        fileService.validateYoutubeUrlsForUpdate(youtubeUrls, deleteFileIds, existingFiles);

        // 삭제할 파일 처리
        if (deleteFileIds != null) {
            for (Long fnum : deleteFileIds) {
                fileService.deleteFileByFnum(fnum);
            }
        }

        // 게시글과 파일 수정
        updateBoard(board, files, deleteFileIds, youtubeUrls);
    }

}
