package com.sku.fitizen.mapper.challenge;

import com.sku.fitizen.domain.challenge.Message;
import com.sku.fitizen.domain.challenge.Participation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ChatMapper {


   int saveMessage(Message message);

   boolean  checkParticipationExists(Participation participation);

   List<Message> getMessagesByRoomId(Participation parti);


}
