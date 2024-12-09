package com.sku.fitizen.service.challenge;

import com.sku.fitizen.Dto.SchedulerDTO;
import com.sku.fitizen.domain.challenge.*;
import com.sku.fitizen.domain.pay.SpendingPoint;
import com.sku.fitizen.mapper.ChatMapper;
import com.sku.fitizen.mapper.PaymentMapper;
import com.sku.fitizen.mapper.challenge.ChallengeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

@Service
public class ChallengeService {

    @Autowired
    ChallengeMapper mapper;

    @Autowired
    PaymentMapper paymentMapper;

    @Autowired
    ChatMapper chatMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;
    @Autowired
    private ChallengeMapper challengeMapper;

        @Transactional
        public void challengeScheduler()
        {
            //mapper.endedChallenge(); // 챌린지 종료 시키기
            // 종료된 공식 챌린지 ids( 포인트 지급 안된 )
            List<Integer> ids =mapper.getEndedChallenges();
            System.err.println("ids:"+ids);
            if (ids != null && !ids.isEmpty()) {
                // 유저별 달성도 정보 가져오기
                List<SchedulerDTO> challenges = mapper.getPercentageForChallengesByUser(ids);
                System.err.println(challenges);

                List<Rewards> rewardsList = new ArrayList<>();

                for (SchedulerDTO users : challenges) {
                    double percentage = users.getPercentage(); // 달성도

                    // 달성도에 따라 포인트 지급
                    int rewardPoints = 0;
                    if (percentage >= 80) {
                        rewardPoints = 5000; // 80% 이상
                    } else if (percentage >= 70) {
                        rewardPoints = 2500; // 70% 이상 80% 미만
                    }

                    // 포인트 지급 대상이 있을 경우 리스트에 추가
                    if (rewardPoints > 0) {
                        Rewards reward = new Rewards(users.getUserId(), users.getChallengeId(), rewardPoints);
                        rewardsList.add(reward);
                    }
                }
                // 포인트 지급 처리 (매퍼 호출)
                if (!rewardsList.isEmpty()) {
                    int success = mapper.insertRewards(rewardsList);
                    if (success > 0) {
                       int ok= mapper.updatePointGranted(ids);
                       if (ok > 0) { System.err.println("success");}
                       else System.err.println("failed");

                    }
                }
            }}

        // 공식 챌린지인지 아닌지
        public int isOfficial (int challengeId){ return mapper.isOfficial(challengeId);}
        // 내가 작성한 챌린지
        public List<Challenge> getChallengeCreatedByMe(String userId){return mapper.getChallengeCreatedByMe(userId);}
        //내가 참여한 챌린지
        public List<Challenge> getMyChallengeList(String userId) {return mapper.getMyChallengeList(userId);}

        // 카테고리 정보만 가져오기 (작성폼에서 사용하려고)
        public  List<ChallCategory> getChallCategories() {return mapper.getChallCategories();}
        // 카테고리별 챌린지정보들
        public List<Challenge> getChallByCategory(int categoryId) {return mapper.getChallByCategory(categoryId);}
        // 챌린지 검색: 저자 , 제목 ,내용
        public List<Challenge> searchChallenges(Map<String,String> info) {return  mapper.searchChallenges(info);}
        // 챌린지 top3
        public List<Challenge> getTop3Challenge() {return mapper.getTop3Challenge();}
        // 관리자 챌린지 : 공식 챌린지 목록
        public List<Challenge> getChallengesByAdmin() {return mapper.getChallengesByAdmin();}
        //챌린지 전체 목록
        public List<Challenge> getChallengeList() {return mapper.getChallengeList();}
        // 챌린지 고유 번호로 챌린지 상세정보 가져오기
        public Challenge getChallengeById(Integer id) {return mapper.getChallengeById(id);}

    //챌린지 등록, 및  참여 테이블에 만든사람 아이디 추가
    //이유: 챌린지 등록한 사람도 하나의 참여자이기 때문에
    @Transactional
    public boolean saveChallenge(Challenge challenge , MultipartFile file){
        //saveChallenge를 @Transactional 할경우  챌린지 등록이 커밋 안된상태라 참여목록에서 FK를 참조 못함
        String creatorId = challenge.getCreatorId(); //참여자에 작성자 아이디도 넣기 위해

        if (!file.isEmpty())
        {
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
            Participation parti = new Participation(challengeId,creatorId);
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
                if(parti.getSpentPoint()>0)
                {
                    paymentMapper.saveSpendingPoint(new SpendingPoint(parti.getUserId(),parti.getSpentPoint(),0,"공식 챌린지"));

                }
            }else return success;

            return success;
        }

        // 챌린지 시작전 삭제
        @Transactional
        public boolean deleteChallenge(int challengeId) {
            //fk참조 관계:챌린지(이미지),챌알림,챌댓글,메세지(이미지),참여테이블,인증게시판댓글,인증게시판(이미지)
            // 이미지 먼저 삭제 및 caseCade 진행

            // 이미지 파일 이름과 소스를 DB에서 가져오기
            List<Map<String, String>> imageInfoList = challengeMapper.getImageList(challengeId);

            System.out.println(imageInfoList);
            // 이미지 파일 삭제
            for (Map<String, String> imageInfo : imageInfoList) {
                String source = imageInfo.get("source");  // 어떤 테이블에서 나왔는지 구분
                String filename = imageInfo.get("image_filename");  // 이미지 파일 이름
                // 각 테이블에 맞는 이미지 경로 설정
                String basePath = "";
                switch (source) {
                    case "challenge":
                        basePath = uploadDir+"challengeCover/";  // UCOVERIMG 이미지 경로
                        break;
                    case "chat_message":
                        basePath = uploadDir+"chatImg/";  // UIMG 이미지 경로
                        break;
                    case "proofShot":
                        basePath = uploadDir+"proofShot/";  // UPHOTO 이미지 경로
                        break;
                    default:
                        // 해당 테이블에 대한 경로가 없으면 무시
                        continue;
                }

                String imagePath = basePath + filename;
                File imageFile = new File(imagePath);

                if (imageFile.exists()) {
                    try {
                        Files.delete(imageFile.toPath());  // 이미지 파일 삭제
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            challengeMapper.deleteChallenge(challengeId);
            return true;
        }


}
