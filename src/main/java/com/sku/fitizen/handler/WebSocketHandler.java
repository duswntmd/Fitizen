package com.sku.fitizen.handler;

import com.sku.fitizen.domain.Trainer.ConsultMessage;
import com.sku.fitizen.domain.Trainer.Consultation;
import com.sku.fitizen.domain.User;
import com.sku.fitizen.domain.challenge.Message;
import com.sku.fitizen.service.ChatService;
import com.sku.fitizen.service.Trainer.ConsultationService;
import com.sku.fitizen.service.Trainer.TrainerService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class WebSocketHandler extends TextWebSocketHandler
{   //리스트 보단 맵
    //private final List<WebSocketSession> webSocketSessions = new ArrayList<>();
    @Autowired
    ChatService chatService;

    @Autowired
    TrainerService trainerService;
    @Autowired
    ConsultationService consultationService;

    @Autowired
    Base64Image base64Image;


   // private static Map<String, WebSocketSession> userMap = new HashMap<>();
    private static Map<String, Map<String, WebSocketSession>> roomMap = new HashMap<>(); // roomId 별로 사용자 저장
    private static Map<String, Map<String, WebSocketSession>> consultMap = new HashMap<>();
    @Override  /* 클라이언트 접속시에 호출됨 */
    public void afterConnectionEstablished(WebSocketSession session) throws Exception //session이 한 이용자 역할을 함
    {   //클라이언트가 접속하자마자 돌아감
        /* 인터셉터에서 전달된 userid 를 추출하여 사용하는 예 */
       // String userid = (String)session.getAttributes().get("userid"); // Map<String ,object >   dbject 형이 나오기에 (String)으로
       // log.info("웹소켓핸들러, userid={}", userid);

       // userMap.put(userid, session); //이용자를 모아야만 메세지가 이용자마다 전달됨
       // log.info("Client Connected");

        User user = (User) session.getAttributes().get("user");
        String roomId = (String) session.getAttributes().get("roomId");
        String consultId =(String)session.getAttributes().get("consultId");

        log.info("웹소켓 연결: userid={}, roomId={} consultId={}", user.getId(), roomId ,consultId);

        // userid 를 조회해 트레이너 Y 이면 상담테이블에 번호가 존재하는지
        //N이면  userid 가 상담 테이블에 존재하는지
        //map에 담는다
        // 상담테이블의 메세지를 가져온다 메세지를 보낸다

        // 챌린지 방에 사용자 추가
        roomMap.putIfAbsent(roomId, new HashMap<>());
        roomMap.get(roomId).put(user.getId(), session);
        // 상담 방에 사용자 추가
        consultMap.put(consultId,new HashMap<>());
        consultMap.get(consultId).put(user.getId(), session);

        // 트레이너 상담 메세지 백업
        if(StringUtils.hasText(consultId)) {

               List<ConsultMessage> messages= chatService.getConsultMessages(consultId);
               if(messages!=null && messages.size()>0)
               {
                   for (ConsultMessage m : messages) {
                       JSONObject jsonMessage = new JSONObject();
                       jsonMessage.put("sender", m.getSenderId());
                       jsonMessage.put("msg", m.getMessage());
                       if (m.getUImg() != null) {
                           String img = "data:image/jpeg;base64," + base64Image.imageToBase64(m.getUImg());
                           jsonMessage.put("img", img);
                           jsonMessage.put("fileName", m.getImg());
                       }
                       TextMessage textMessage = new TextMessage(jsonMessage.toString());
                       session.sendMessage(textMessage);
                   }
               }

        }


        // 챌린지 채팅방 메세지 백업
        if(StringUtils.hasText(roomId)) {
        boolean exists = chatService.checkParticipationExists(user.getId(), roomId);
        if (exists) {
            List<Message> messages = chatService.getMessages(user.getId(), roomId);
            System.err.println("2"+messages.size());
            for (Message m : messages) {
                JSONObject jsonMessage = new JSONObject();
                jsonMessage.put("sender", m.getSenderId());
                jsonMessage.put("msg", m.getMessage());
                if (m.getUImg() != null) {
                    String img = "data:image/jpeg;base64," + base64Image.imageToBase64(m.getUImg());
                    jsonMessage.put("img", img);
                    jsonMessage.put("fileName", m.getImg());
                }
                TextMessage textMessage = new TextMessage(jsonMessage.toString());
                session.sendMessage(textMessage);
            }
        }
    }
    }





    @Override  /* 서버에 메시지 도착시 호출됨 */
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
       // log.info("서버에서 받은 메시지:{}", message.getPayload()); // getPayload: 문자열로 ,WebSocketSession session 메세지를 보낸사람

        // JSON 문자열을 파싱하여 JSONObject로 변환
        JSONObject jsObj = new JSONObject(message.getPayload());

        String msg = jsObj.optString("msg", null);
        String img = jsObj.optString("img", null);
        String originImgName = jsObj.optString("fileName", null);


        String roomId = (String) session.getAttributes().get("roomId");
        String consultId = (String) session.getAttributes().get("consultId");
        User user = (User) session.getAttributes().get("user");


        Message data= new Message();
        ConsultMessage cdata= new ConsultMessage();

        String UUIDImgName =UUID.randomUUID().toString()+ "_" + originImgName;

        if (img != null) {
            try {
                // 파일 이름 생성
                base64Image.saveBase64ToImage(img, UUIDImgName);
            } catch (IOException e) {
                log.error("이미지 저장 오류: ", e);
            }
        }

        // 트레이너 상담 메세지 전달
        if (StringUtils.hasText(consultId) && consultMap.containsKey(consultId)) {
            for (WebSocketSession ss : consultMap.get(consultId).values()) {
                if (ss.isOpen()) {  // 세션이 열려 있는지 확인
                    ss.sendMessage(message);

                    cdata.setSenderId(user.getId());
                    cdata.setConsultId(Integer.parseInt(consultId));
                    cdata.setMessage(msg != null ? msg : "");
                    cdata.setImg(img != null ?  originImgName: "");
                    cdata.setUImg(img != null ? UUIDImgName: "");
                    chatService.saveConsultMessage(cdata);



                }
            }
        }

        // 동일한 roomId에 있는 사용자들에게만 메시지 전송
        if (StringUtils.hasText(roomId) && roomMap.containsKey(roomId)) {
            for (WebSocketSession ss : roomMap.get(roomId).values()) {
                if (ss.isOpen()) {  // 세션이 열려 있는지 확인
                    ss.sendMessage(message);


                    data.setSenderId(user.getId());
                    data.setRoomId(Integer.parseInt(roomId));
                    data.setMessage(msg != null ? msg : "");
                    data.setImg(img != null ?  originImgName: "");
                    data.setUImg(img != null ? UUIDImgName: "");
                    data.setSentAt(LocalDateTime.now());
                    chatService.saveChallMessage(data);

                }
            }
        }


    }


    @Override   /* 접속 해제시 호출됨 */
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        User user = (User) session.getAttributes().get("user");
        String roomId = (String) session.getAttributes().get("roomId");
        String consultId =(String)session.getAttributes().get("consultId");

        log.info("웹소켓 연결 종료: userid={}, roomId={} consultId={}",user.getId(), roomId, consultId );

        // roomId에 해당하는 방에서 사용자 세션 제거
        if (StringUtils.hasText(roomId) && roomMap.containsKey(roomId)) {
            Map<String, WebSocketSession> userSessions = roomMap.get(roomId);
            if (userSessions != null) {
                userSessions.remove(user.getId());
                log.info("사용자 {}가 방 {}에서 나갔습니다.",user.getId(), roomId);

            }
        }
    }

    @Override   /* 오류 발생시 호출됨 */
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("Error:" + exception);
        super.handleTransportError(session, exception);
    }
}
