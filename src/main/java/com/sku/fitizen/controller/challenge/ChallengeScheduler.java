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

    private static final Logger log = LoggerFactory.getLogger(ChallengeScheduler.class);
    private  final ChallengeService challengeService;

    @Scheduled(cron = "0 0 12 * * ?")
    public  void test()
    {
        System.err.println(" 포인트 자급 로직 시작");
        challengeService.challengeScheduler();
    }
}
