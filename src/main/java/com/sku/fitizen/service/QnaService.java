package com.sku.fitizen.service;

import com.sku.fitizen.domain.QNA;
import com.sku.fitizen.mapper.QnaMapper;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
    public class QnaService {

        private final QnaMapper qnaMapper;

        public QnaService(QnaMapper qnaMapper) {
            this.qnaMapper = qnaMapper;
        }

        public List<QNA> findAll() {
            return qnaMapper.findAll();
        }

        public QNA findById(int id) {
            return qnaMapper.findById(id);
        }

    public void save(QNA qna) {
        // 현재 시간을 가져와 java.sql.Date로 변환
        java.util.Date utilDate = new java.util.Date();
        qna.setQdate(new Date(utilDate.getTime()));

        qnaMapper.save(qna); // 저장 메서드 호출 (insert로 수정)
    }

        public void update(QNA qna) {
            qnaMapper.update(qna);
        }
    public QNA addComment(int qid, String comment) {
        QNA qna = qnaMapper.findById(qid); // 게시글을 찾아서
        qna.setComments(comment); // 댓글을 추가하고
        qnaMapper.addComment(qna); // 업데이트
        return qna;
        }

        public void deleteById(int qid) {
            qnaMapper.deleteById(qid);
        }

}
