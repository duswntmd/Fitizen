package com.sku.fitizen.handler;

import com.sku.fitizen.domain.Message;
import com.sku.fitizen.mapper.challenge.ChatMapper;
import com.sku.fitizen.service.challenge.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class WebSocketHandler extends TextWebSocketHandler
{   //리스트 보단 맵
    //private final List<WebSocketSession> webSocketSessions = new ArrayList<>();
    @Autowired
    ChatMapper mapper;
    @Autowired
    ChatService chatService;

   // private static Map<String, WebSocketSession> userMap = new HashMap<>();
    private static Map<String, Map<String, WebSocketSession>> roomMap = new HashMap<>(); // roomId 별로 사용자 저장
    @Override  /* 클라이언트 접속시에 호출됨 */
    public void afterConnectionEstablished(WebSocketSession session) throws Exception //session이 한 이용자 역할을 함
    {   //클라이언트가 접속하자마자 돌아감
        /* 인터셉터에서 전달된 userid 를 추출하여 사용하는 예 */
       // String userid = (String)session.getAttributes().get("userid"); // Map<String ,object >   dbject 형이 나오기에 (String)으로
       // log.info("웹소켓핸들러, userid={}", userid);

       // userMap.put(userid, session); //이용자를 모아야만 메세지가 이용자마다 전달됨
       // log.info("Client Connected");


        String userId = (String) session.getAttributes().get("userId");
        String roomId = (String) session.getAttributes().get("roomId");

        log.info("웹소켓 연결: userid={}, roomId={}", userId, roomId);

        // 방에 사용자 추가
        roomMap.putIfAbsent(roomId, new HashMap<>());
        roomMap.get(roomId).put(userId, session);

        log.info("사용자 {}가 방 {}에 연결되었습니다.", userId, roomId);


        boolean exists = chatService.checkParticipationExists(userId, roomId);
        if(exists)
        {
            List<Message> messages = chatService.getMessages(userId, roomId);
            System.out.println(messages.size());
            for (Message m : messages) {
                JSONObject jsonMessage = new JSONObject();
                jsonMessage.put("msg", m.getMessage());
                jsonMessage.put("img", m.getImg());

                TextMessage textMessage = new TextMessage(jsonMessage.toString());
                session.sendMessage(textMessage);
            }
        }
        
    }

    @Override  /* 서버에 메시지 도착시 호출됨 */
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("서버에서 받은 메시지:{}", message.getPayload()); // getPayload: 문자열로 ,WebSocketSession session 메세지를 보낸사람

        // JSON 문자열을 파싱하여 JSONObject로 변환
        JSONObject jsObj = new JSONObject(message.getPayload());
        String msg = jsObj.optString("msg", null);
        String img = jsObj.optString("img", null);

        String roomId = (String) session.getAttributes().get("roomId");
        String userId = (String) session.getAttributes().get("userId");






        // 동일한 roomId에 있는 사용자들에게만 메시지 전송
        if (roomId != null && roomMap.containsKey(roomId)) {
            for (WebSocketSession ss : roomMap.get(roomId).values()) {
                if (ss.isOpen()) {  // 세션이 열려 있는지 확인
                    ss.sendMessage(message);

                    Message data= new Message();
                    data.setSenderId(userId);
                    data.setRoomId(Integer.parseInt(roomId));
                    data.setMessage(msg != null ? msg : "");
                   // data.setImg(img != null ? img : "");
                    data.setSentAt(LocalDateTime.now());
                    mapper.saveMessage(data);



                }
            }
        } else {
            log.warn("존재하지 않는 방 ID: {}", roomId);
        }
        /*
        //채팅서비스에 접속된 모든 클라이언트에게 브로드캐스팅
        Collection<WebSocketSession> coll = userMap.values(); // 세션의 집합
        for(WebSocketSession ss : coll) {
            ss.sendMessage(message); //브로드캐스팅
        }

         */

		/* JSON 포맷으로 통신할 때는 아래처럼... 복잡한 메세지일 경우
		JSONParser parser = new JSONParser();
		JSONObject jsObj = (JSONObject) parser.parse( message.getPayload()); json 문자열이 jsonObject로
		String receiver = (String)jsObj.get("receiver");

		WebSocketSession wss = userMap.get(receiver);
		wss.sendMessage(message);  // 특정 접속자에게만 메시지를 전달함
		*/
    }







    @Override   /* 접속 해제시 호출됨 */
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = (String) session.getAttributes().get("userId");
        String roomId = (String) session.getAttributes().get("roomId");

        log.info("웹소켓 연결 종료: userid={}, roomId={}", userId, roomId);

        // roomId에 해당하는 방에서 사용자 세션 제거
        if (roomId != null && roomMap.containsKey(roomId)) {
            Map<String, WebSocketSession> userSessions = roomMap.get(roomId);
            if (userSessions != null) {
                userSessions.remove(userId);
                log.info("사용자 {}가 방 {}에서 나갔습니다.", userId, roomId);

                // 방에는 유지, 사용자가 나간 것만 처리
            }
        } else {
            log.warn("존재하지 않는 방 ID: {}", roomId);
        }
    }

    @Override   /* 오류 발생시 호출됨 */
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("Error:" + exception);
        super.handleTransportError(session, exception);
    }
}
