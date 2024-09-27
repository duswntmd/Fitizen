package com.sku.fitizen.mapper.Trainer;

import com.sku.fitizen.domain.Trainer.Consultation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ConsultationMapper {

    // 상담 신청
    int saveConsultation(Consultation consult);

    // 이미 신청한 상담인지
    List<Consultation> existByUserId(String userId);

    // 트레이너의 상담 유저 목록
    List<Map<String, Object>>  getMyUsers(int trainerNo);

    // 유저의 상담 트레이너 목록
    List<Map<String, Object>>  getMyTrainers(String userId);
    // 상담 취소
    int cancel(Consultation consult);



    // 트레이너

    // 상담 승인
    int approve(Consultation consult);

    // 상담 거절
    int reject(Consultation consult);


}

