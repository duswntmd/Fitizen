package com.sku.fitizen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class IndexController {



    @GetMapping("")
    public String index() {
        return "index";
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
