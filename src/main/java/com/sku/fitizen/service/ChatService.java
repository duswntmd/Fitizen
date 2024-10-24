package com.sku.fitizen.service;


import com.sku.fitizen.domain.Trainer.ConsultMessage;
import com.sku.fitizen.domain.Trainer.Consultation;
import com.sku.fitizen.domain.challenge.Message;
import com.sku.fitizen.domain.challenge.Participation;
import com.sku.fitizen.mapper.ChatMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sku.fitizen.domain.challenge.ChallAlim;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    ChatMapper mapper;

    // 챌린지 채팅 저장
    @Transactional
    public void saveChallMessage(Message message ,List<String> users)
   {
     mapper.saveChallMessage(message);

       // 2. 알림 생성 (참여자들에게 알림 전송)
       for (String userId : users) {
           if (!userId.equals(message.getSenderId())) {
               ChallAlim alim = new ChallAlim();
               alim.setUserId(userId);
               alim.setMessageId(message.getMessageId());

               // 알림 저장
               mapper.saveChallAlim(alim);
           }
       }

   }

   // 트레이너 상담 채팅 저장
   public void saveConsultMessage(ConsultMessage message)
   {
      mapper.saveConsultMessage(message);
   }

    //트레이너 상담 채팅 동기화
   public List<ConsultMessage> getConsultMessages(String consultId)
   {
       return mapper.getMessagesByConsultId(Integer.parseInt(consultId));
   }




    // 챌린지  채팅 목록 동기화
    public List<Message> getMessages(String userId, String roomId) {
        Participation participation = new Participation(userId, Integer.parseInt(roomId));
        //System.out.println("확인용"+mapper.getMessagesByRoomId(participation));
        return mapper.getMessagesByRoomId(participation);
    }


    public boolean checkParticipationExists(String userId, String roomId) {
        Participation participation = new Participation(userId,Integer.parseInt(roomId));


        return mapper.checkParticipationExists(participation);
    }


    //알림 서비스
    public boolean checkIfSeen(int messageId, String userId) {
        // 알림 테이블에서 messageId와 userId를 기준으로 SEEN 필드를 확인하는 로직
        Integer seen = mapper.checkIfSee(messageId, userId);
        // SEEN이 1이면 확인된 것이므로 true, 아니면 false 반환

        if(seen == 1 )return true;
        return false;
    }
}
