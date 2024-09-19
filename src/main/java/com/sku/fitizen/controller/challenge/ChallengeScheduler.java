package com.sku.fitizen.controller.challenge;


import com.sku.fitizen.mapper.challenge.ChallengeMapper;
import com.sku.fitizen.service.UserService;
import com.sku.fitizen.service.challenge.ChallengeService;
import com.sku.fitizen.service.challenge.ParticipationService;
import com.sku.fitizen.service.challenge.ProofShotBoardService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChallengeScheduler {

    @Value("${challenge.points.award}")
    private int pointsToAward;


    private static final Logger log = LoggerFactory.getLogger(ChallengeScheduler.class);
    private  final ChallengeService challengeService;
    private final UserService userService;
    private final ProofShotBoardService boardService;
    private final ParticipationService participationService;



    @Scheduled(cron = "0 23 18 * * ?")
    public  void test()
    {
        log.info("스케줄러 포인트 지급 시작!");
        List<Integer> endedChallengeIds =challengeService.getChallengesEndingToday();
        for (Integer challengeId : endedChallengeIds) {
            // 해당 챌린지에 참여한 사용자 목록을 가져옵니다.
            List<String> userIds = participationService.getUserIdsByChallengeId(challengeId);

            // 각 사용자에게 포인트를 지급합니다.
            for (String userId : userIds) {
                userService.addPointsToUser(userId,pointsToAward);

            }
        }
    }
}
