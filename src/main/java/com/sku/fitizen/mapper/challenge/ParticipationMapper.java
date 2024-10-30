package com.sku.fitizen.mapper.challenge;


import com.sku.fitizen.domain.challenge.Participation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface ParticipationMapper {


  // 한 유저가 특정 챌린지에 참여자인지
  int existUser(Participation parti);

  // 스캐쥴러
  List<String> getUserIdList(int challengeId);

  // 유저가 참가한 날짜
  Date getUserJoinDate (Participation parti);


  // 유저가 참가한 챌린지 번호 리스트
  List<Integer> getChallengeIdsByUser(@Param("userId") String userId);
}
