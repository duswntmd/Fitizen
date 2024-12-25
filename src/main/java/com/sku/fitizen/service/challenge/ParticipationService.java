package com.sku.fitizen.service.challenge;

import com.sku.fitizen.domain.challenge.Participation;
import com.sku.fitizen.domain.pay.SpendingPoint;
import com.sku.fitizen.mapper.PaymentMapper;
import com.sku.fitizen.mapper.challenge.ChallengeMapper;
import com.sku.fitizen.mapper.challenge.ParticipationMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ParticipationService
{
    @Autowired
    ParticipationMapper mapper;

    @Autowired
    ChallengeService challengeService;

    @Autowired
    ChallengeMapper challengeMapper;

    @Autowired
    PaymentMapper paymentMapper;

    public  List<Integer> getChallengeIdsByUser(String userId)
    {
       return mapper.getChallengeIdsByUser(userId);
    }
    public  List<String> getUserIdsByChallengeId(int challengeId) {return  mapper.getUserIdList(challengeId);}
    // 한 유저가 특정 챌린지에 참여자인지
    public boolean  existUser(Participation parti) {return mapper.existUser(parti)>0;}
    public Date getUserJoinDate  (String userId, int challengeId)
    {
      Participation parti = new Participation();
      parti.setUserId(userId);
      parti.setChallengeId(challengeId);
      return mapper.getUserJoinDate(parti);
    }
    // 참여 취소
    @Transactional
    public boolean cancelChallenge(Participation parti)
    {

        int entryPoint = challengeService.isOfficial(parti.getChallengeId());
        if(entryPoint >0)
        {
           paymentMapper.saveSpendingPoint(new SpendingPoint(parti.getUserId(),entryPoint,1,"공식챌린지 취소"));
        }
        int success = mapper.cancelChallenge(parti);
        if(success>0) {
                return challengeMapper.decreaseMembers(parti.getChallengeId())>0;
        }
        return false;
    }
}
