package com.sku.fitizen.service.Trainer;

import com.sku.fitizen.domain.Trainer.Trainer;
import com.sku.fitizen.mapper.Trainer.TrainerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerService {
    @Autowired
    TrainerMapper mapper;

    //관리자 승인된 트레이너 리스트
    public List<Trainer> getTrainersByApproved()
    {
        return mapper.getTrainersByApproved();
    }

    // 트레이너 상세 정보
    public Trainer getTrainerDetailById(int id) {return mapper.getTrainerDetailById(id);}

    // 트레이너 번호의 유저아이디
    public  String getUserIdByTrainerNo(int trainerNo) {return mapper.getUserIdByTrainerNo(trainerNo);}

    // 유저가 트레이너일경우 유저아이디로 트레이너 번호
    public int  getTrainerNoByUserId(String userId) {return mapper.getTrainerNoByUserId(userId);}
}
