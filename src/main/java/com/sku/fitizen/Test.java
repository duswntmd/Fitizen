package com.sku.fitizen;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class Test {  // 클래스 이름을 대문자로 수정
    @GetMapping("/t")
    public String hello() {
        return "index";  // "index"라는 뷰 이름을 반환
    }

    @GetMapping("/tlf")
    public String test() {
        return "th/test";  // "index"라는 뷰 이름을 반환
    }
}
