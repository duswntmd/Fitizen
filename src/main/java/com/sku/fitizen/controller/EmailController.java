package com.sku.fitizen.controller;


import com.sku.fitizen.domain.User;
import com.sku.fitizen.service.EmailService;
import com.sku.fitizen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/mail")
public class EmailController
{
    @Autowired
    private EmailService svc;

    @Autowired
    private UserService userService;


    @GetMapping("/findId")
    public String findId(Model model) {
        model.addAttribute("user", new User());
        return "findId";
    }


    @PostMapping("/findId")
    @ResponseBody
    public Map<String,String> findId(@RequestParam("email") String email,
                                     @RequestParam("name") String name,
                                     Model model) {
        // email과 phone을 이용하여 아이디 찾기 로직 구현
        String foundId = userService.findIdByEmailAndName(email, name);
        Map<String,String> map = new HashMap<>();
        if (foundId != null) {

            map.put("id", foundId);
        } else {
            map.put("message", "해당 정보와 일치하는 아이디가 없습니다");
        }

        return map;
    }

    @GetMapping("/findPwd")
    public String findPwd(Model model) {
        model.addAttribute("user", new User());
        return "findPwd";
    }


    @PostMapping("/findPwd")
    @ResponseBody
    public Map<String,Boolean> findPwd(@RequestParam("id") String id,
                                       @RequestParam("name") String name,
                                       Model model,HttpSession session) {
        // email과 phone을 이용하여 아이디 찾기 로직 구현
        String foundMail = userService.findEmailByIdAndName(id, name);
        System.out.println("찾은이메일:"+foundMail);
        Map<String,Boolean> response = new HashMap<>();
        String tempPassword=UUID.randomUUID().toString().substring(0, 10);

        //foundMail에 메일보내는 로직 추가
 boolean isSent = svc.sendHTMLEMAIL(foundMail,tempPassword);
 System.out.println("아이디:"+id);
 System.out.println("임시비번:"+tempPassword);

        //db에 임시비밀번호로 변경
boolean changed=userService.changePwd(tempPassword, id);
boolean res= isSent && changed;
System.out.println("결과:"+res);
        response.put("res", res);
        return response;
    }
//    @GetMapping("")
//    @ResponseBody
//    public String index()
//    {
//        return "Java Mail Test";
//    }
//
//    @GetMapping("/test")
//    @ResponseBody
//    public String sendTestMail(HttpSession session)
//    {
//        boolean isSent = svc.sendHTMLEMAIL(email);
//        //boolean isSent = svc.sendAttachMail();
//        return isSent ? "메일 보내기 성공":"메일 보내기 실패";
//    }
//
//    @GetMapping("/auth/{code}")  // 보낸 메일에서 이용자가 인증 링크를 클릭했을 때
//
//    public String index(@PathVariable("code")String returnCode, HttpSession session)
//    {
//        String authCode = (String)session.getAttribute("authCode");
//        if(returnCode.equals(authCode)) {
//
//
//            return "/";
//        }
//        log.info("인증코드 확인={}", returnCode);
//        return "이메일 인증 실패";
//    }

}