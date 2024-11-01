package com.sku.fitizen.service.board;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sku.fitizen.domain.board.Board;
import com.sku.fitizen.mapper.board.BoardCommentMapper;
import com.sku.fitizen.mapper.board.BoardLikeMapper;
import com.sku.fitizen.mapper.board.BoardMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class BoardService {

    @Autowired
    private BoardMapper boardMapper;

    @Autowired
    private FileService fileService;

    @Autowired
    private BoardLikeMapper boardLikeMapper;

    @Autowired
    private BoardCommentMapper boardCommentMapper;


    // 게시글 목록 조회
    public List<Board> getBoardList() {
        return boardMapper.getBoardList();
    }

    // 게시글 조회
    public Board getBoard(Long bno) {
        Board board = boardMapper.getBoard(bno);
        return board;
    }

    public Board getBoardWithFiles(Long bno) {
        return boardMapper.getBoardWithFiles(bno);  // JOIN 쿼리 호출
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

    public PageInfo<Board> searchBoardList(String title, String author, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);  // 페이지 설정
        List<Board> boards = boardMapper.searchBoardList(title, author);  // 조건에 맞는 게시글 조회
        return new PageInfo<>(boards);  // 페이지 정보 반환
    }

    @Transactional
    public void increaseHits(Long bno) {
        boardMapper.updateHits(bno);
    }

    public boolean isLikedByUser(Long bno, String userId) {
        return boardLikeMapper.checkLike(bno, userId) > 0;
    }

    @Transactional
    public void addLike(Long bno, String userId) {
        boardLikeMapper.insertLike(bno, userId);
    }

    @Transactional
    public void removeLike(Long bno, String userId) {
        boardLikeMapper.deleteLike(bno, userId);
    }

    public int getLikeCount(Long bno) {
        return boardLikeMapper.countLikes(bno);
    }
}
