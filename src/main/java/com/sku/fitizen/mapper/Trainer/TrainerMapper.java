package com.sku.fitizen.mapper.Trainer;


import com.sku.fitizen.domain.Trainer.Trainer;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TrainerMapper {

    // 관리자 승인된 트레이너들 가져오기
    List<Trainer> getTrainersByApproved();

    // 관리자 승인된 트레이너 상세 정보
    Trainer getTrainerDetailById(int id);

    //트레이너 번호의 해당 유저 아이디
    String getUserIdByTrainerNo(int id);

    // 유저가 트레이너일경우 유저아이디로 트레이너 번호
    int getTrainerNoByUserId(String userId);


}
