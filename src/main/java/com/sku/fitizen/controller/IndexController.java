package com.sku.fitizen.controller;

import com.sku.fitizen.domain.User;
import com.sku.fitizen.service.UserService;
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
    public String index(@AuthenticationPrincipal UserDetails loggedInUser, Model model) {
        if (loggedInUser == null) {
            return "index";
        }

        String username = loggedInUser.getUsername();

        // 2. 데이터베이스에서 사용자 정보 조회
        User user = userService.findUserByUsername(username);

        model.addAttribute("user", user);
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
        System.out.println("선택된 운동:"+sport);
        model.addAttribute("sport", sport);  // 운동 이름을 모델에 추가
        return "exerciseDetail";  // exerciseDetail.jsp로 이동
    }


}
