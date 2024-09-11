package com.sku.fitizen.mapper;

import com.sku.fitizen.domain.Message;
import com.sku.fitizen.domain.Participation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ChatMapper {


   int saveMessage(Message message);

   boolean  checkParticipationExists(Participation participation);

   List<Message> getMessagesByRoomId(Participation parti);


}
