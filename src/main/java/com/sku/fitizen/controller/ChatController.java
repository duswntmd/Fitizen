package com.sku.fitizen.controller;

import com.sku.fitizen.Dto.MychatListDTO;
import com.sku.fitizen.domain.User;
import com.sku.fitizen.domain.challenge.Challenge;
import com.sku.fitizen.service.ChatService;
import com.sku.fitizen.service.Trainer.ConsultationService;
import com.sku.fitizen.service.Trainer.TrainerService;
import com.sku.fitizen.service.UserService;
import com.sku.fitizen.service.challenge.ChallengeService;
import com.sku.fitizen.service.challenge.ParticipationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.POST;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/ws")
public class ChatController {

    @Autowired
    private ChallengeService service;
    @Autowired
    private TrainerService tService;
    @Autowired
    private ConsultationService cService;
    @Autowired
    private ParticipationService pService;
    @Autowired
    private ChatService chatService;


    @GetMapping("/myConsultation")
    @ResponseBody
    public List<Map<String, Object>> myConsult(@SessionAttribute(value = "user") User user)
    {
        List<Map<String, Object>> response = new ArrayList<>();
        boolean isTrainer = user.getIs_trainer().equals("Y");

        // 역할 정보 추가
        Map<String, Object> roleInfo = new HashMap<>();
        roleInfo.put("isTrainer", isTrainer);
        response.add(roleInfo);

        // 사용자 데이터 추가
        List<Map<String, Object>> data;
        if (isTrainer) { // 트레이너일 경우
            int trainerNo = tService.getTrainerNoByUserId(user.getId());
            data = cService.getMyUsersByApproved(trainerNo);
        } else { // 일반 유저일 경우
            data = cService.getMyTrainersByApproved(user.getId());
        }
        response.addAll(data); // 리스트에 데이터를 추가

        return response;

    }



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

    // 메세지 읽기
    @PostMapping("/readMessage")
    @ResponseBody
    public void readMessage(@RequestParam int messageId,@SessionAttribute(value = "user") User user) {

            chatService.readMessage(messageId,user.getId());  // 읽음 처리 서비스 호출
    }
    // 메세지 읽기: 상담
    @PostMapping("/readMessageConsult")
    @ResponseBody
    public void readMessageConsult(@RequestParam int messageId,@SessionAttribute(value = "user") User user) {

        chatService.readMessageConsult(messageId,user.getId());  // 읽음 처리 서비스 호출
    }






    // 안읽은 메세지 수 가져오기 : 임시 . 전체 챌린지
    @GetMapping("/unreadMessageCount")
    @ResponseBody
    public int getUnreadMessageCount(@SessionAttribute(value = "user") User user) {
        // 사용자 ID로 읽지 않은 메시지 개수를 ChatService에서 조회
        return chatService.getUnreadMessageCount(user.getId());
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