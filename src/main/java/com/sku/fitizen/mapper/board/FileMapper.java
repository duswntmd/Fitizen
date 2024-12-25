package com.sku.fitizen.mapper.board;

import com.sku.fitizen.domain.board.BoardFilesVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileMapper {

    // 게시글 번호에 따른 파일 목록 조회
    List<BoardFilesVO> getFilesByBoard(Long bno);

    // 특정 파일 번호(fnum)에 해당하는 파일 정보 조회
    BoardFilesVO getFileByFnum(Long fnum);

    // 파일 삽입
    int insertFile(BoardFilesVO file);

    // 파일 삭제
    int deleteFile(Long fnum);

    // 특정 게시글의 모든 파일 삭제
    void deleteFilesByBoard(Long bno);

}
