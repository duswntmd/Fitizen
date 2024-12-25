package com.sku.fitizen.mapper.Trainer;

import com.sku.fitizen.Dto.ConsultLastMessageDTO;
import com.sku.fitizen.domain.Trainer.Consultation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ConsultationMapper {

    // 상담 신청
    int saveConsultation(Consultation consult);

    //상담 재신청
    int reapplyConsultation(Consultation consult);

    // 이미 신청한 상담인지
    Integer existByUserId(Consultation consult);

    // 상담 아이디로 유저 ,트레이너 아이디 한번에 가져오기  List<String>
    List<String> getUserIdsByConsultId(int consultId);
    // 트레이너의 상담 유저 목록
    List<Map<String, Object>>  getMyUsers(int trainerNo);
    List<Map<String, Object>> getMyUsersByApproved(int trainerNo);


    // 유저의 상담 트레이너 목록
    List<Map<String, Object>>  getMyTrainers(String userId);
    List<Map<String, Object>> getMyTrainersByApproved(String userId);
    // 상담 취소
    int cancel(Consultation consult);

    // 유저가 속한 상담 아이디 목록
    List<Integer> getConsultIdsByUser(ConsultLastMessageDTO dto);

    // 트레이너

    // 상담 승인
    int approve(Consultation consult);

    // 상담 거절
    int reject(Consultation consult);

}

