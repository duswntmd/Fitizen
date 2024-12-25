package com.sku.fitizen.handler;

import com.sku.fitizen.domain.User;
import com.sku.fitizen.domain.challenge.Message;
import com.sku.fitizen.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class AlarmWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private ChatService chatService;

    // 알림 WebSocket 세션 관리
    private static Map<String, WebSocketSession> alarmSessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {

        User user = (User) session.getAttributes().get("user");

        alarmSessions.put(user.getId(), session);
        log.info("알림 소켓 연결: userId={}", user.getId());

        sendAllUnreadCounts(user.getId()); // 연결 시 현재 읽지 않은 메시지 개수를 전송
    }
    // 새로운 메서드
    private void sendAllUnreadCounts(String userId) {
        // 챌린지별 안 읽은 메시지 개수 가져오기
        List<Map<String, BigDecimal>> unreadChallengeCounts = chatService.getUnreadCountsByChallengeId(userId);
        // 상담별 안 읽은 메시지 개수 가져오기
        List<Map<String, BigDecimal>> unreadConsultCounts = chatService.unreadCountsByConsultId(userId);

        // 각각의 마지막 메세지
        List<Map<Integer, Message>> getLastMessage = chatService.getLastMessage(userId);
        List<Map<Integer,Message>> getLastConsultMessage= chatService.getLastConsultMessage(userId);
        int totalUnreadChallengeCount = 0;
        int totalUnreadConsultCount = 0;

        // 챌린지별 안 읽은 메시지 개수 JSON 배열 생성
        JSONArray challengeJsonArray = new JSONArray();
        for (Map<String, BigDecimal> countInfo : unreadChallengeCounts) {
            totalUnreadChallengeCount += countInfo.get("UnreadCount").intValue();
            JSONObject jsonObject = new JSONObject(countInfo);
            challengeJsonArray.put(jsonObject);
        }

        // 상담별 안 읽은 메시지 개수 JSON 배열 생성
        JSONArray consultJsonArray = new JSONArray();
        for (Map<String, BigDecimal> countInfo : unreadConsultCounts) {
            totalUnreadConsultCount += countInfo.get("UnreadCount").intValue();
            JSONObject jsonObject = new JSONObject(countInfo);
            consultJsonArray.put(jsonObject);
        }

        JSONArray lastMessagesArray = new JSONArray();
        if (getLastMessage != null) {  // getLastMessage 리스트가 null인지 확인
            getLastMessage.forEach(messageInfo -> {
                if (messageInfo != null) {  // 개별 항목이 null인지 확인
                    lastMessagesArray.put(new JSONObject(messageInfo));
                }
            });
        }

        JSONArray lastMessagesConsultArray = new JSONArray();
        if (getLastConsultMessage != null) {  // getLastConsultMessage 리스트가 null인지 확인
            getLastConsultMessage.forEach(messageInfo -> {
                if (messageInfo != null) {  // 개별 항목이 null인지 확인
                    lastMessagesConsultArray.put(new JSONObject(messageInfo));
                }
            });
        }
        // 결과 JSON 객체 생성 및 합산된 총 알림 개수 추가
        JSONObject responseJson = new JSONObject();
        responseJson.put("challengeJsonArray", challengeJsonArray);      // 개별 챌린지별 알림
        responseJson.put("totalUnreadChallengeCount", totalUnreadChallengeCount); // 전체 챌린지 알림 수
        responseJson.put("lastMessages", lastMessagesArray);

        responseJson.put("consultJsonArray", consultJsonArray);
        responseJson.put("totalUnreadConsultCount", totalUnreadConsultCount); // 전체 상담 알림 수
        responseJson.put("lastMessagesConsult", lastMessagesConsultArray);
        // 사용자 세션에 알림 전송
        WebSocketSession session = alarmSessions.get(userId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(responseJson.toString())); // JSON 객체 전송
            } catch (Exception e) {
                log.error("알림 메시지 전송 오류:", e);
            }
        }
    }

    // 챌린지 및 상담 알림 통합 전송
    public void notifyAllUnreadCountsToUser(String userId) {
        sendAllUnreadCounts(userId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String userId = (String) session.getAttributes().get("userId");
        alarmSessions.remove(userId);
        log.info("알림 소켓 연결 종료: userId={}", userId);
    }


}

