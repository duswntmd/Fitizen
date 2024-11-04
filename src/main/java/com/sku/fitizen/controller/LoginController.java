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

@Slf4j
@Controller
@RequestMapping("/login")
@SessionAttributes("user")  // 'user'를 세션에 저장하도록 설정
public class LoginController {

    @Autowired
    UserService userService;

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

//    @PostMapping("/login")
//    public String login(@RequestParam String id,
//                        @RequestParam String pwd,
//                        @RequestParam(required = false) String toURL,
//                        @RequestParam(defaultValue = "false") boolean rememberId,
//                        HttpServletResponse response,
//                        HttpSession session,
//                        Model model) throws Exception {
//
//        log.debug("Attempting to log in with ID: {}", id);
//
//        // 1. id와 pwd를 확인
//        if (!loginCheck(id, pwd)) {
//            // 2-1   일치하지 않으면, loginForm으로 이동
//            log.warn("로그인 실패: id 또는 비밀번호 불일치");
//            String msg = URLEncoder.encode("id 또는 pwd가 일치하지 않습니다.", "utf-8");
//            return "redirect:/login/login?msg=" + msg;
//        }
//
//        log.info("로그인 성공: id = {}", id);
//
//        // 2-2. id와 pwd가 일치하면, User 객체를 세션에 저장
//        User user = userService.selectUser(id);
//        log.debug("User found: {}", user);
//        model.addAttribute("user", user);  // Model에 user 추가, 이때 'user'가 세션에 저장됨
//        session.setAttribute("userId", user.getId());  // 세션에 userId 저장
//        session.setAttribute("user", user);  // 세션에 User 객체 저장 (필요하다면)
//
//        if (rememberId) {
//            // 1. 쿠키를 생성
//            Cookie cookie = new Cookie("id", id);
//            // 2. 응답에 저장
//            response.addCookie(cookie);
//        } else {
//            // 1. 쿠키를 삭제
//            Cookie cookie = new Cookie("id", id);
//            cookie.setMaxAge(0); // 쿠키를 삭제
//            // 2. 응답에 저장
//            response.addCookie(cookie);
//        }
//
//        // 3. 홈으로 이동
//        toURL = (toURL == null || toURL.equals("")) ? "/" : toURL;
//        log.info("홈으로 리다이렉트: toURL = {}", toURL);
//        return "redirect:" + toURL;
//    }
//
//    private boolean loginCheck(String id, String pwd) {
//        User user = null;
//
//        try {
//            user = userService.selectUser(id);
//            log.debug("User fetched: " + user);
//        } catch (Exception e) {
//            log.error("Error fetching user", e);
//            return false;
//        }
//
//        boolean isMatch = user != null && passwordEncoder.matches(pwd, user.getPwd());
//        log.debug("비밀번호 일치 여부: {}", isMatch);
//        return isMatch;
//
//    }


}
