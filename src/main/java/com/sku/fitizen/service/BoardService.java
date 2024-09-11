package com.sku.fitizen.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sku.fitizen.domain.Board;
import com.sku.fitizen.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class BoardService {

    @Autowired
    private BoardMapper boardMapper;

    @Autowired
    private FileService fileService;

    // 게시글 목록 조회
    public List<Board> getBoardList() {
        return boardMapper.getBoardList();
    }

    // 게시글 조회
    public Board getBoard(Long bno) {
        Board board = boardMapper.getBoard(bno);
        return board;
    }

    // 게시글 생성 및 파일 저장
    @Transactional
    public void insertBoard(Board board, List<MultipartFile> files, String youtubeUrl) throws IOException {
        // 게시글 저장
        boardMapper.insertBoard(board);

        // 파일 및 유튜브 URL 처리
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    // 파일이 있을 경우 파일만 저장
                    fileService.storeFileOrYoutube(null, file, board.getBno());
                }
            }
        }

        // 유튜브 URL 저장
        if (youtubeUrl != null && !youtubeUrl.isEmpty()) {
            fileService.storeFileOrYoutube(youtubeUrl, null, board.getBno());
        }
    }

    // 게시글 수정
    @Transactional
    public void updateBoard(Board board, List<MultipartFile> newFiles, List<Long> deleteFileIds, String youtubeUrl) throws IOException {
        // 1. 삭제할 파일 처리
        if (deleteFileIds != null && !deleteFileIds.isEmpty()) {
            for (Long fnum : deleteFileIds) {
                boolean deleted = fileService.deleteFileByFnum(fnum);  // 파일 삭제
                if (!deleted) {
                    System.err.println("파일 삭제 실패: " + fnum);
                }
            }
        }

        // 2. 새로운 파일 저장
        if (newFiles != null && !newFiles.isEmpty()) {
            for (MultipartFile file : newFiles) {
                fileService.storeFileOrYoutube(youtubeUrl, file, board.getBno());  // 새 파일 또는 유튜브 URL 저장
            }
        }

        // 게시글 정보 업데이트
        boardMapper.updateBoard(board);
    }

    // 게시글 삭제 및 파일 삭제
    @Transactional
    public void deleteBoard(Long bno) {
        // 1. 게시글에 속한 파일들 먼저 삭제
        fileService.deleteFilesByBoard(bno);

        // 2. 파일 삭제가 완료된 후 게시글 삭제
        boardMapper.deleteBoard(bno);
    }

    public PageInfo<Board> searchBoardList(String title, String author, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);  // 페이지 설정
        List<Board> boards = boardMapper.searchBoardList(title, author);  // 조건에 맞는 게시글 조회
        return new PageInfo<>(boards);  // 페이지 정보 반환
    }
}
