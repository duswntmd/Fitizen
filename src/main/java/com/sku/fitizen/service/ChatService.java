package com.sku.fitizen.service;


import com.sku.fitizen.domain.Trainer.ConsultMessage;
import com.sku.fitizen.domain.Trainer.Consultation;
import com.sku.fitizen.domain.challenge.Message;
import com.sku.fitizen.domain.challenge.Participation;
import com.sku.fitizen.mapper.ChatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    ChatMapper mapper;

    // 챌린지 채팅 저장
    public void saveChallMessage(Message message)
   {
     mapper.saveChallMessage(message);
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

}
