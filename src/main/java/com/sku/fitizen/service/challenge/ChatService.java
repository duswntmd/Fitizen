package com.sku.fitizen.service.challenge;


import com.sku.fitizen.domain.Message;
import com.sku.fitizen.domain.Participation;
import com.sku.fitizen.mapper.challenge.ChatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    ChatMapper mapper;

    // 채팅 저장

    // 일단은 소캣 헨들러에 있음


    // 채팅 목록 동기화
    public List<Message> getMessages(String userId, String roomId) {
        Participation participation = new Participation(userId, Integer.parseInt(roomId));
        System.out.println("확인용"+mapper.getMessagesByRoomId(participation));
        return mapper.getMessagesByRoomId(participation);
    }


    public boolean checkParticipationExists(String userId, String roomId) {
        Participation participation = new Participation(userId,Integer.parseInt(roomId));


        return mapper.checkParticipationExists(participation);
    }

}
