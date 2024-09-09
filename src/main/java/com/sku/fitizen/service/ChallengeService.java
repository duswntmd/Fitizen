package com.sku.fitizen.service;


import com.sku.fitizen.domain.Challenge;
import com.sku.fitizen.domain.Participation;
import com.sku.fitizen.mapper.ChallengeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChallengeService {

    @Autowired
    ChallengeMapper mapper;


        //챌린지 등록, 및  참여 테이블에 만든사람 아이디 추가
        //이유: 챌린지 등록한 사람도 하나의 참여자이기 때문에

        @Transactional
        public boolean saveChallenge(Challenge challenge)
        {
            //saveChallenge를 @Transactional 할경우  챌린지 등록이 커밋 안된상태라 참여목록에서 FK를 참조 못함
            String creatorId=challenge.getCreatorId(); //참여자에 작성자 아이디도 넣기 위해

            /* *****챌린지 등록***** */
            mapper.saveChallenge(challenge);
            //자동 증가된 Key 값 가져오기 =challengeId
            int challengeId = challenge.getChallengeId();

            /* *****참여자 등록***** */
            //챌린지 등록한 사람도 참여자 ->참여자 등록
            Participation parti =new Participation(creatorId,challengeId);
             int saved =mapper.addCreatorToParticipation(parti);

            if(saved==1) return true;
            else return false;
        }

        //챌린지 전체 목록
        public List<Challenge> getChallengeList()
        {
            List<Challenge> list =mapper.getChallengeList();
            return list;
        }

        // 챌린지 고유 번호로 챌린지 상세정보 가져오기
        public Challenge getChallengeById(Integer id)
        {
            Challenge chall =mapper.getChallengeById(id);

            return chall;
        }

        // 챌린지 참여하기
        @Transactional
        public int participate(Participation parti)
        {
            int success =mapper.participate(parti);
            if(success==1) mapper.increaseMembers(parti.getChallengeId());
            return 1;
        }

        //내가 참여한 챌린지
        public List<Challenge> getMyChallengeList(String userId)
        {
            List<Challenge> myChall= mapper.getMyChallengeList(userId);

            return myChall;
        }



}
