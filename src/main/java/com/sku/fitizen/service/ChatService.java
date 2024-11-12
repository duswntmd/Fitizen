package com.sku.fitizen.service;


import com.sku.fitizen.Dto.ConsultLastMessageDTO;
import com.sku.fitizen.domain.Trainer.ConsultMessage;
import com.sku.fitizen.domain.Trainer.Consultation;
import com.sku.fitizen.domain.User;
import com.sku.fitizen.domain.challenge.*;
import com.sku.fitizen.mapper.ChatMapper;
import com.sku.fitizen.service.Trainer.ConsultationService;
import com.sku.fitizen.service.Trainer.TrainerService;
import com.sku.fitizen.service.challenge.ChallengeService;
import com.sku.fitizen.service.challenge.ParticipationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class ChatService {

    @Autowired
    ChatMapper mapper;
    @Autowired
    UserService userService;
    @Autowired
    ChallengeService cService;
    @Autowired
    ParticipationService pService;
    @Autowired
    TrainerService tService;
    @Autowired
    ConsultationService consultService;

    public List<Map<String, BigDecimal>> unreadCountsByConsultId(String userId)
    {
        return mapper.unreadCountsByConsultId(userId);
    }

    //유저가 속한 각 채린지 별 마지막 메세지가져오기
    public List<Map<Integer, Message>> getLastMessage(String userId)
    {
      List<Integer> ids= pService.getChallengeIdsByUser(userId);
        // ids 리스트가 비어있는지 확인하여 비어있으면 빈 리스트 반환
        if (ids.isEmpty()) {
            return new ArrayList<>();  // 빈 리스트 반환
        }

        return  mapper.getLastMessage(ids);
    }

    public List<Map<Integer, Message>> getLastConsultMessage(String userId){
        ConsultLastMessageDTO dto = new ConsultLastMessageDTO();

        if(userService.isTrainer(userId) =='Y')
        {
            int id = tService.getTrainerNoByUserId(userId);
            dto.setTrainerId(id);
        }else if(userService.isTrainer(userId) =='N') {
            dto.setUserId(userId);
        }



        List<Integer> ids= consultService.getConsultIdsByUser(dto);
        // ids 리스트가 비어있는지 확인하여 비어있으면 빈 리스트 반환
        if (ids.isEmpty()) {
            return new ArrayList<>();  // 빈 리스트 반환
        }
        return  mapper.getLastConsultMessage(ids);
    }


    // 유저 개인이 안읽은 전체 챌린지 메세지 개수 : 폴링 방식
    public int getUnreadMessageCount(String userId) {
        return mapper.countUnreadMessages(userId);
    }


    public List<Map<String, BigDecimal>> getUnreadCountsByChallengeId(String userId)
    {
        return mapper.unreadCountsByChallengeId(userId);
    }



    // 챌린지 채팅 저장
    @Transactional
    public void saveChallMessage(Message message ,List<String> users,List<String> activeUserIds)
   {
      mapper.saveChallMessage(message);

       // 메시지를 보낼 때마다 각 참여자에게 알림 생성
       for (String userId : users) {
           // 메시지 발신자와 다른 사용자만 알림 생성
           if (!userId.equals(message.getSenderId())) {
               // 사용자의 채팅방 입장 시점을 가져옴
               Date userJoinDate = pService.getUserJoinDate(userId, message.getRoomId());
               /* *********나중에 Date 다 바꿀것. 메세지******** */
               LocalDateTime userJoinDateTime = userJoinDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
               // 사용자가 방에 들어온 이후의 메시지인지 확인
               if (message.getSentAt().isAfter(userJoinDateTime)) {
                   if (activeUserIds.contains(userId)) {

                       mapper.saveChallAlimLIVE(message.getMessageId(), userId);  // 자동 읽음 처리
                   } else {
                       ChallAlim alim = new ChallAlim();
                       alim.setUserId(userId);
                       alim.setMessageId(message.getMessageId());
                       alim.setSeen(0);  // 기본값으로 '읽지 않음' 설정

                       // 알림 저장
                       mapper.saveChallAlim(alim);
                   }
               }
           }
       }
   }

   // 트레이너 상담 채팅 저장
   public void saveConsultMessage(ConsultMessage message ,List<String> users,List<String> activeUserIds)
   {
       mapper.saveConsultMessage(message);

       for (String userId : users)
       {
           if (!userId.equals(message.getSenderId()))
           {
               if (activeUserIds.contains(userId))
               {
                   //1
                   mapper.saveConsultAlimLIVE(message.getMessageId(), userId);
               }else{
                   //0
                   ConsultAlim alim = new ConsultAlim();
                   alim.setUserId(userId);
                   alim.setMessageId(message.getMessageId());
                   alim.setSeen(0);
                   mapper.saveConsultAlim(alim);
               }
           }
       }

   }

    //트레이너 상담 채팅 동기화
   public List<ConsultMessage> getConsultMessages(String consultId)
   {
       return mapper.getMessagesByConsultId(Integer.parseInt(consultId));
   }


    // 단일 메시지 읽음 처리
    public void readMessage(int messageId, String userId) {
        mapper.readMessage(messageId, userId);
    }
    public void readMessageConsult(int messageId, String userId) {
        mapper.readMessageConsult(messageId, userId);
    }


    // 챌린지  채팅 목록 동기화
    public List<Message> getMessages(String userId, String roomId) {
        Participation participation = new Participation(Integer.parseInt(roomId),userId);
        return mapper.getMessagesByRoomId(participation);
    }


    public boolean checkParticipationExists(String userId, String roomId) {
        Participation participation = new Participation(Integer.parseInt(roomId),userId);

        return mapper.checkParticipationExists(participation);
    }


    //알림 서비스
    public Integer checkIfSeen(int messageId, String userId) {
        // 알림 테이블에서 messageId와 userId를 기준으로 SEEN 필드를 확인하는 로직
        // SEEN이 1이면 확인된 것이므로 true, 아니면 false 반환
        return mapper.checkIfSeen(messageId, userId);

    }


    //알림 서비스 상담
    public Integer checkIfSeenConsult(int messageId, String userId) {
        // 알림 테이블에서 messageId와 userId를 기준으로 SEEN 필드를 확인하는 로직
        // SEEN이 1이면 확인된 것이므로 true, 아니면 false 반환
        return mapper.checkIfSeenConsult(messageId, userId);

    }
}
