package com.sku.fitizen.controller;

import com.sku.fitizen.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import java.time.LocalDateTime;

@Slf4j
@Controller
@RequestMapping("/login")
@SessionAttributes("user")  // 'user'를 세션에 저장하도록 설정
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginForm(HttpServletRequest request) {
        String uri = request.getHeader("Referer");
        // 이전 페이지가 로그인 페이지가 아닌 경우에만 설정
        if (uri != null && !uri.contains("/login")) {
            request.getSession().setAttribute("prevPage", uri);
        }
        return "loginForm";
    }

    @GetMapping("/logout")
    public String logout(SessionStatus status) {
        // 1. 세션을 종료
        status.setComplete();
        // 2. 홈으로 이동
        return "redirect:/";
    }

}
