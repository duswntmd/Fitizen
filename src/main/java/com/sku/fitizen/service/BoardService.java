package com.sku.fitizen.service;

import com.sku.fitizen.domain.Board;
import com.sku.fitizen.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public void insertBoard(Board board, List<MultipartFile> files) throws IOException {
        // 게시글 저장
        boardMapper.insertBoard(board);

        // 게시글에 속한 파일 저장
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                fileService.storeFile(file, board.getBno());  // 파일 저장
            }
        }
    }

    // 게시글 수정
    public void updateBoard(Board board, List<MultipartFile> newFiles) throws IOException {
        // 게시글 수정
        boardMapper.updateBoard(board);

        // 기존 파일 삭제 및 새로운 파일 저장
        if (newFiles != null && !newFiles.isEmpty()) {
            // 게시글에 속한 기존 파일 삭제
            fileService.deleteFilesByBoard(board.getBno());

            // 새로운 파일 저장
            for (MultipartFile file : newFiles) {
                fileService.storeFile(file, board.getBno());
            }
        }
    }

    // 게시글 삭제 및 파일 삭제
    public void deleteBoard(Long bno) {
        // 1. 게시글에 속한 파일들 먼저 삭제
        fileService.deleteFilesByBoard(bno);

        // 2. 파일 삭제가 완료된 후 게시글 삭제
        boardMapper.deleteBoard(bno);
    }

}
