package com.sku.fitizen.mapper.challenge;


import com.sku.fitizen.Dto.SchedulerDTO;
import com.sku.fitizen.domain.challenge.ChallCategory;
import com.sku.fitizen.domain.challenge.Challenge;
import com.sku.fitizen.domain.challenge.Participation;
import com.sku.fitizen.domain.challenge.Rewards;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ChallengeMapper {

  // 챌린지 종료 시키기
  void endedChallenge();
  // 종료된 공식 챌린지( 포인트 지급 안된)
  List<Integer> getEndedChallenges();
  // 공식 챌린지 유저별 달성도 가져오기
  List<SchedulerDTO> getPercentageForChallengesByUser (List<Integer> ids);
  // 포인트 지급 :스캐쥴러
  int insertRewards( List<Rewards> reward);
  // 포인트 지급됨 처리
  int updatePointGranted(List<Integer> ids);

  //공식 챌린지인지
  int isOfficial(int challengeId);
  //내가 작성한 챌린지
  List<Challenge> getChallengeCreatedByMe(String userId);
  // 챌린지 등록
  int saveChallenge(Challenge challenge);
  // 카테고리 정보만
  List<ChallCategory> getChallCategories();
  // top3 챌린지
  List<Challenge> getTop3Challenge();
  // 관리자 챌린지: 공식 챌린지 목록
  List<Challenge> getChallengesByAdmin();
  // 챌린지 전체 목록    카테고리별- 챌린지 리스트
  List<Challenge> getChallengeList();
  // 카테고리에 속해 있는 챌린지
  List<Challenge>getChallByCategory(int categoryId);
  // 카테고리별 챌리지 리스트
  // List<ChallCategory> getCategoryByChallenges();
  // 챌린지 검색
  List<Challenge> searchChallenges(Map<String, String> obj);
  // 챌린지 참여자 등록
  int addCreatorToParticipation(Participation participation);
  //챌린지 고유 아이디로 조회
  Challenge getChallengeById(Integer id);
  //챌린지 참여하기인데 한 유저는 동일한 챌린지 중복으로 참여 불가 ( 한 챌린지에는 동일한 유저가 들어갈 수 없다)
  int participate(Participation parti);
  //챌린지 참여자수 증가  (챌린지 작성시 참여자 수는 기본값 1 ) 작성자도 하나의 참여자이기 때문에 ,유저가 챌린지 참여시 1증가
  int increaseMembers(int challengeId);
  //챌린지 참여자 수 감소  -> 사용자가 챌린지 취소 눌렀을경우
  int decreaseMembers(int challengeId);
  //내가 참여한 챌린지 리스트 (INNER JOIN)
  List<Challenge> getMyChallengeList(String userId);
  // 종료 날짜가 오늘이 챌린지 아이디 리스트 가져오기
  List<Integer> getChallengesEndingToday();
  // uuId 이미지가져오기
  List<Map<String, String>> getImageList(int challengeId);
  //챌린지 삭제
  void deleteChallenge(int challengeId);
}

