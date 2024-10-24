package com.sku.fitizen.service.Trainer;

import com.sku.fitizen.domain.Trainer.Consultation;
import com.sku.fitizen.mapper.Trainer.ConsultationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ConsultationService {

    @Autowired
    ConsultationMapper mapper;

    // 상담 신청하기
    public boolean saveConsultation(Consultation consult)
    {

       int row= mapper.saveConsultation(consult);
       if(row>0) return true;
       else return false;
    }





    // 이미 신청한 상담인지
    public int existByUserId(Consultation consult) {return mapper.existByUserId(consult);}

    // 트레이너에게 신청된 상담 목록 조회
    public List<Map<String, Object>> getMyUsers(int trainerNo) {return mapper.getMyUsers(trainerNo);}

    // 일반 유저가 신청한 상담 목록 조회
    public List<Map<String, Object>> getMyTrainers(String userId) {return mapper.getMyTrainers(userId);}

    // 상담 취소
    public boolean cancel(Consultation consult)
    {
      if(mapper.cancel(consult)>0) return true;
      else return false;
    }

    // 상담 승인- 트레이너
    public  boolean approve(Consultation consult)
    {
        if(mapper.approve(consult)>0) return true;
        else return false;
    }

    // 상담 거절- 트레이너
    public  boolean reject(Consultation consult)
    {
        if(mapper.reject(consult)>0) return true;
        else return false;
    }
}
