package com.sku.fitizen.controller;

import com.sku.fitizen.Dto.MychatListDTO;
import com.sku.fitizen.domain.User;
import com.sku.fitizen.domain.challenge.Challenge;
import com.sku.fitizen.service.challenge.ChallengeService;
import com.sku.fitizen.service.challenge.ParticipationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/ws")
public class ChatController {

    @Autowired
    private ChallengeService service;
    @Autowired
    private ParticipationService pService;

    @GetMapping("/myChallenges")
    @ResponseBody
    public MychatListDTO test(@SessionAttribute(value = "user") User user )
    {
        List<Challenge> myChall= service.getMyChallengeList(user.getId());
        return new MychatListDTO(user.getId(), myChall);
    }

    @GetMapping("/challUsers/{roomId}")
    @ResponseBody
    public MychatListDTO test2(@PathVariable("roomId") int roomId)
    {
        List<String> users =pService.getUserIdsByChallengeId(roomId);
        Challenge challenge=service.getChallengeById(roomId);
        String creator =challenge.getCreatorId();
        return new MychatListDTO(users,creator);
    }



    @GetMapping("/in")   //  localhost/ws/in?userid=smith  형식으로 요청하여 로그인 테스트
    public String chatForm(@SessionAttribute(value = "user") User user,
                           @RequestParam(value = "roomId" )String roomId,
                           Model model)
    {

        model.addAttribute("userId",user.getId());  // 이용자 아이디를 웹소켓 핸들러 안으로 전달하는 절차 시작부분
        // userid -> chat.jsp 로 전달 -> 인터셉터로 전달 -> 웹소켓핸들러에서 수신
        model.addAttribute("roomId", roomId);
        // HttpSession session = request.getSession();

        return "th/chall/chat";  //chat.jsp 보여줌 프론트에서는 웹소캣 서버가 연결됨
        /* chat.jsp에서 ws://localhost/ws/chat 으로 요청하면 인터셉터를 거쳐 웹소켓핸들러에 접속됨 */
    }
    @GetMapping("/tin")
    public String trainerChatForm(@SessionAttribute(value = "user") User user,
                                  @RequestParam(value = "consultId") String consultId,
                                  Model model
                                  )
    {

        model.addAttribute("userId",user.getId());
        model.addAttribute("consultId", consultId);

        return "th/chall/chat";
    }



}