package com.sku.fitizen.mapper.challenge;


import com.sku.fitizen.domain.challenge.Participation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ParticipationMapper {


  // 한 유저가 특정 챌린지에 참여자인지
  int existUser(Participation parti);

  // 스캐쥴러
  List<String> getUserIdList(int challengeId);
}
