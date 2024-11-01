package com.sku.fitizen.service.challenge;

import com.sku.fitizen.domain.challenge.Participation;
import com.sku.fitizen.mapper.challenge.ParticipationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ParticipationService
{
    @Autowired
    ParticipationMapper mapper;

    public  List<Integer> getChallengeIdsByUser(String userId)
    {
       return mapper.getChallengeIdsByUser(userId);
    }


    // 한 유저가 특정 챌린지에 참여자인지
   public boolean  existUser(Participation parti)
   {

       int result = mapper.existUser(parti);
       if(result ==1)return true;


       return false;
   }

  public  List<String> getUserIdsByChallengeId(int challengeId)
    {
        List<String> userIds = mapper.getUserIdList(challengeId);

        return userIds;
    }

  public Date getUserJoinDate  (String userId, int challengeId) {

      Participation parti = new Participation();
      parti.setUserId(userId);
      parti.setChallengeId(challengeId);


      return mapper.getUserJoinDate(parti);
  }
}
