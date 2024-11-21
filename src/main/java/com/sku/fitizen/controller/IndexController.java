package com.sku.fitizen.controller;

import com.sku.fitizen.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/")
@SessionAttributes("user")
public class IndexController {
    @Autowired
    UserService userService;

    @GetMapping("")
    public String index(@AuthenticationPrincipal UserDetails loggedInUser, HttpServletRequest request, Model model) {
        if (loggedInUser == null) {
            return "index";
        }

        return "index";
    }

    @GetMapping("/noaccess")
    public String noaccess() {
        return "th/noaccess";
    }

    @GetMapping("/findME")
    public String findMyExercise() {

        return "findMyExercise";
    }

    @GetMapping("/findResult")
    public String findResult() {
        return "findResult";
    }



    @GetMapping("/exerciseDetail")
    public String exerciseDetail(@RequestParam("exercise") String sport, Model model) {
        model.addAttribute("sport", sport);  // 운동 이름을 모델에 추가
        return "exerciseDetail";  // exerciseDetail.jsp로 이동
    }



}
