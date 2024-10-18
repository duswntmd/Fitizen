package com.sku.fitizen.mapper;

import com.sku.fitizen.domain.Trainer.ConsultMessage;
import com.sku.fitizen.domain.Trainer.Consultation;
import com.sku.fitizen.domain.challenge.Message;
import com.sku.fitizen.domain.challenge.Participation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ChatMapper {


   int saveChallMessage(Message message);

   int saveConsultMessage(ConsultMessage message);


   boolean  checkParticipationExists(Participation participation);

   List<Message> getMessagesByRoomId(Participation parti);

   List<ConsultMessage> getMessagesByConsultId(int consultId);

}
