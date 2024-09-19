package com.sku.fitizen.controller;

import com.sku.fitizen.domain.User;
import com.sku.fitizen.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/login")
@SessionAttributes("user")  // 'user'를 세션에 저장하도록 설정
public class LoginController {

    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/logout")
    public String logout(SessionStatus status) {
        // 1. 세션을 종료
        status.setComplete();
        // 2. 홈으로 이동
        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(@RequestParam String id,
                        @RequestParam String pwd,
                        @RequestParam(required = false) String toURL,
                        @RequestParam(defaultValue = "false") boolean rememberId,
                        HttpServletResponse response,
                        Model model) throws Exception {

        // 1. id와 pwd를 확인
        if (!loginCheck(id, pwd)) {
            // 2-1   일치하지 않으면, loginForm으로 이동
            String msg = URLEncoder.encode("id 또는 pwd가 일치하지 않습니다.", "utf-8");
            return "redirect:/login/login?msg=" + msg;
        }

        // 2-2. id와 pwd가 일치하면, User 객체를 세션에 저장
        User user = userService.selectUser(id);
        model.addAttribute("user", user);  // Model에 user 추가, 이때 'user'가 세션에 저장됨

        if (rememberId) {
            // 1. 쿠키를 생성
            Cookie cookie = new Cookie("id", id);
            // 2. 응답에 저장
            response.addCookie(cookie);
        } else {
            // 1. 쿠키를 삭제
            Cookie cookie = new Cookie("id", id);
            cookie.setMaxAge(0); // 쿠키를 삭제
            // 2. 응답에 저장
            response.addCookie(cookie);
        }

        // 3. 홈으로 이동
        toURL = (toURL == null || toURL.equals("")) ? "/" : toURL;
        return "redirect:" + toURL;
    }

    private boolean loginCheck(String id, String pwd) {
        User user = null;

        try {
            user = userService.selectUser(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return user != null && user.getPwd().equals(pwd);
    }


}
