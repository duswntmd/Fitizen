package com.sku.fitizen.mapper;

import com.sku.fitizen.domain.QNA;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QnaMapper {

    List<QNA> findAll();
    QNA findById(int qid);
    boolean save(QNA qna);
    void addComment(QNA qna);
    boolean update(QNA qna);
    boolean deleteById(int qid);

}
