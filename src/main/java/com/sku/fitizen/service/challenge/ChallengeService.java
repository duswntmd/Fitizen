package com.sku.fitizen.service.challenge;

import com.sku.fitizen.domain.challenge.Challenge;
import com.sku.fitizen.domain.challenge.ChallCategory;
import com.sku.fitizen.domain.challenge.Message;
import com.sku.fitizen.domain.challenge.Participation;
import com.sku.fitizen.mapper.ChatMapper;
import com.sku.fitizen.mapper.challenge.ChallengeMapper;
import com.sku.fitizen.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ChallengeService {

    @Autowired
    ChallengeMapper mapper;

    @Autowired
    ChatMapper chatMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;


    //챌린지 등록, 및  참여 테이블에 만든사람 아이디 추가
        //이유: 챌린지 등록한 사람도 하나의 참여자이기 때문에
        @Transactional
        public boolean saveChallenge(Challenge challenge , MultipartFile file){
            //saveChallenge를 @Transactional 할경우  챌린지 등록이 커밋 안된상태라 참여목록에서 FK를 참조 못함
            String creatorId = challenge.getCreatorId(); //참여자에 작성자 아이디도 넣기 위해

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

                Message data= new Message();
                data.setSenderId(parti.getUserId());
                data.setSentAt(LocalDateTime.now());
                data.setRoomId(parti.getChallengeId());
                data.setMessage(parti.getUserId()+"(당신)은 주최자 입니다.!");
                data.setImg("");
                data.setUImg("");
                chatMapper.saveChallMessage(data);



                if (saved == 1) return true;

            }

            return false;
        }

        // 카테고리 정보만 가져오기 (작성폼에서 사용하려고)
        public  List<ChallCategory> getChallCategories()
        {
            List<ChallCategory> list =mapper.getChallCategories();

            return list ;
        }

        // 카테고리별 챌린지정보들
        public List<Challenge> getChallByCategory(int categoryId)
        {

            return mapper.getChallByCategory(categoryId);
        }
        /*
        public  List<ChallCategory> getCategoryByChallenges()
        {
            List<ChallCategory> list =mapper.getCategoryByChallenges();
            return list;
        }
        */

        // 챌린지 검색: 저자 , 제목 ,내용
        public List<Challenge> searchChallenges(Map<String,String> info)
        {

            return  mapper.searchChallenges(info);
        }


        // 챌린지 top3
        public List<Challenge> getTop3Challenge()
        {
            return mapper.getTop3Challenge();
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
            if(success==1)
            {
                mapper.increaseMembers(parti.getChallengeId());
                /*
                 *  챌린지 참여하면 -> 참여테이블 -> 채팅테이블  메세지 (" 참여하였씁니다 ")
                 *  이유 : 메세지 동기화시 참여한 시점 부터 동기화 하기위해  ex(카톡 오픈챗 )
                 *  이유2: 메세지 알림 : 안읽음 표시시 들어온 시점("참여하였습니다") 부터  안읽은 메세지가 몇개인지 처리하기 위해 메세지를 보냄
                 */
                Message obj= new Message();
                obj.setSenderId(parti.getUserId());
                obj.setSentAt(LocalDateTime.now());
                obj.setRoomId(parti.getChallengeId());
                obj.setMessage(parti.getUserId()+"님이 참여하였습니다.");
                obj.setImg("");
                obj.setUImg("");
                chatMapper.saveChallMessage(obj);
            }



            return success;
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
