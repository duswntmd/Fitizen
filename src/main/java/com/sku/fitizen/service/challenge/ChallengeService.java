package com.sku.fitizen.service.challenge;


import com.sku.fitizen.domain.challenge.Challenge;
import com.sku.fitizen.domain.challenge.Participation;
import com.sku.fitizen.mapper.challenge.ChallengeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ChallengeService {

    @Autowired
    ChallengeMapper mapper;

    @Value("${file.upload-dir}")
    private String uploadDir;


    //챌린지 등록, 및  참여 테이블에 만든사람 아이디 추가
        //이유: 챌린지 등록한 사람도 하나의 참여자이기 때문에

        @Transactional
        public boolean saveChallenge(Challenge challenge , MultipartFile file){
            //saveChallenge를 @Transactional 할경우  챌린지 등록이 커밋 안된상태라 참여목록에서 FK를 참조 못함
            String creatorId = challenge.getCreatorId(); //참여자에 작성자 아이디도 넣기 위해

            System.err.println(file.getOriginalFilename());

            if (!file.isEmpty()) {
                try {
                    // 파일 이름 및 경로 설정
                    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    Path filePath = Paths.get(uploadDir+"challengeCover/", fileName);

                    // 파일 저장
                    file.transferTo(filePath.toFile());

                    // 챌린지 객체에 파일 경로 및 원본 파일명 저장
                    challenge.setUCoverImg(fileName);
                    challenge.setCoverImg(file.getOriginalFilename());

                } catch (IOException e) {
                    e.printStackTrace();
                    return false; // 파일 저장 실패 시 false 반환
                }



                /* *****챌린지 등록***** */
                mapper.saveChallenge(challenge);
                //자동 증가된 Key 값 가져오기 =challengeId
                int challengeId = challenge.getChallengeId();

                /* *****참여자 등록***** */
                //챌린지 등록한 사람도 참여자 ->참여자 등록
                Participation parti = new Participation(creatorId, challengeId);
                int saved = mapper.addCreatorToParticipation(parti);

                if (saved == 1) return true;

            }

            return false;
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



        //********스캐줄링 메서드********//

        // 챌린지 종료날짜가 오늘이 챌린지 번호 가져오기
        public List<Integer> getChallengesEndingToday()
        {
            return  mapper.getChallengesEndingToday();
        }


}
