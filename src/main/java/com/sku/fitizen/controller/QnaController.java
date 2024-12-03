package com.sku.fitizen.controller;

import com.sku.fitizen.domain.QNA;
import com.sku.fitizen.service.QnaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/qna")
public class QnaController {
    private QnaService qnaService;


    public QnaController(QnaService qnaService) {
        this.qnaService = qnaService;
    }
    @Autowired
    private HttpSession session;

    @GetMapping("")
    public String list(Model model) {
    model.addAttribute("list",qnaService.findAll());
        return "th/qna/qnalist";
    }
    @GetMapping("/add")
    public String add(Model model) {
        QNA qna = new QNA();
        String userId = (String) session.getAttribute("userId");  // 세션에서 userId 값을 가져옴
        if (userId != null) {
            qna.setAuthor(userId);  // 가져온 userId를 author 필드에 설정
        }
        model.addAttribute("qna",qna);
        return "th/qna/qnaAdd";
    }
    @PostMapping("/add")
    public String addQuestion(@ModelAttribute QNA qna) {
    qnaService.save(qna);
    return "redirect:/qna"; // 질문 추가 후 목록 페이지로 리디렉션

    }

    @GetMapping("/detail")
    public String detail(@RequestParam("qid") int qid, Model model) {
       QNA qna=qnaService.findById(qid);
        model.addAttribute("qna",qna);
        return "th/qna/qnaDetail";
    }

    @PostMapping("/comment")
    public String answerQuestion(@RequestParam("qid") int qid, @RequestParam("comment") String comment, Model model) {
        QNA qna= qnaService.addComment(qid, comment);
  model.addAttribute("qna",qna);
  return "redirect:/qna/detail?qid=" + qid;  }
}
