package com.sku.fitizen.controller.chetTest;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping("/ws")
public class ChetController {
    @GetMapping("")
    @ResponseBody
    public String index()
    {
        return "WebSocket Test";
    }

    @GetMapping("/in")   //  localhost/ws/in?userid=smith  형식으로 요청하여 로그인 테스트
    public String chatForm(HttpServletRequest request, @RequestParam("userid")String userid,@RequestParam("roomId")String roomId, Model model)
    {
        model.addAttribute("userid", userid);  // 이용자 아이디를 웹소켓 핸들러 안으로 전달하는 절차 시작부분
        // userid -> chat.jsp 로 전달 -> 인터셉터로 전달 -> 웹소켓핸들러에서 수신
        model.addAttribute("roomId", roomId);
        // HttpSession session = request.getSession();

        // log.info("로그인 성공({}), session={}", userid, session.getId());

        return "th/chall/chat";  //chat.jsp 보여줌 프론트에서는 웹소캣 서버가 연결됨
        /* chat.jsp에서 ws://localhost/ws/chat 으로 요청하면 인터셉터를 거쳐 웹소켓핸들러에 접속됨 */
    }
}