package com.sku.fitizen.mapper;

import com.sku.fitizen.domain.Trainer.ConsultMessage;
import com.sku.fitizen.domain.challenge.ChallAlim;
import com.sku.fitizen.domain.challenge.ConsultAlim;
import com.sku.fitizen.domain.challenge.Message;
import com.sku.fitizen.domain.challenge.Participation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Mapper
public interface ChatMapper {


   // 유저가 참여한 챌리니지별 마지막 메세지
   List<Map<Integer,Message>> getLastMessage(List<Integer> ids);
   List<Map<Integer,Message>> getLastConsultMessage(List<Integer> ids);

   // 유저 개인이 안읽은 전체 챌린지 메세지 개수
   int countUnreadMessages(@Param("userId") String userId);

   // 상담 테이블 별 안읽은 메세지 카운트
   List<Map<String, BigDecimal>> unreadCountsByConsultId(@Param("userId") String userId);
   // 첼린지별 안읽은 메세지 카운트
   List<Map<String, BigDecimal>> unreadCountsByChallengeId(@Param("userId") String userId);

   int saveChallMessage(Message message);
   // 알림
   void saveConsultAlim(ConsultAlim alim);
   void saveChallAlim(ChallAlim alim);
   //읽음 여부 확인
   Integer checkIfSeen(@Param("messageId") int messageId, @Param("userId") String userId);
   Integer checkIfSeenConsult(@Param("messageId") int messageId, @Param("userId") String userId);

   void saveConsultAlimLIVE(@Param("messageId") int messageId, @Param("userId") String userId);
   void saveChallAlimLIVE(@Param("messageId") int messageId, @Param("userId") String userId);
   // 단일 메시지 읽음 처리 ,실시간 채팅중인 사람은 자동 읽음 처리에도 사용됨
   void readMessage(@Param("messageId") int messageId, @Param("userId") String userId);

   void readMessageConsult(@Param("messageId") int messageId, @Param("userId") String userId);

   int saveConsultMessage(ConsultMessage message);

   boolean  checkParticipationExists(Participation participation);

   List<Message> getMessagesByRoomId(Participation parti);

   List<ConsultMessage> getMessagesByConsultId(int consultId);


}
