package com.sku.fitizen.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sku.fitizen.domain.Board;
import com.sku.fitizen.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageService {

    @Autowired
    private BoardMapper boardMapper;

    public PageInfo<Board> getBoardList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);  // 페이지 설정
        List<Board> boards = boardMapper.getBoardList();  // 모든 게시글 조회
        return new PageInfo<>(boards);  // 페이지 정보 반환
    }
}
