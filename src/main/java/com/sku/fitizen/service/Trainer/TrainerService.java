package com.sku.fitizen.service.Trainer;

import com.sku.fitizen.domain.Trainer.Trainer;
import com.sku.fitizen.mapper.Trainer.TrainerMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TrainerService {
    @Autowired
    TrainerMapper mapper;

    @Value("${file.upload-dir}")
    private String uploadDir;

    //승인되지 않은 트레이너 리스트
    public List<Trainer> getTrainersByUnapproved()
    {
        return mapper.getTrainersByUnapproved();
    }
    // 트레이너 등록 승인 처리
    public int  approveTrainer(int trainerNo){return mapper.approveTrainer(trainerNo);}
    //관리자 승인된 트레이너 리스트
    public List<Trainer> getTrainersByApproved() {return mapper.getTrainersByApproved();}
    // 트레이너 상세 정보
    public Trainer getTrainerDetailById(int id) {return mapper.getTrainerDetailById(id);}
    // 트레이너 번호의 유저아이디
    public  String getUserIdByTrainerNo(int trainerNo) {return mapper.getUserIdByTrainerNo(trainerNo);}
    // 유저가 트레이너일경우 유저아이디로 트레이너 번호
    public int  getTrainerNoByUserId(String userId) {return mapper.getTrainerNoByUserId(userId);}
    // 검색 : 지역 ,트레이너 이름
    public List<Trainer> searchTrainerList(Map<String,String> info){ return mapper.searchTrainerList(info);}

    // 트레이너 정보 업데이트
    @Transactional
    public boolean updateInfo(Trainer info, MultipartFile[] files)
    {
        if(files !=null)
        {
            try {

                // 프로필 이미지
                if (files[0] != null && !files[0].isEmpty()) {
                    MultipartFile profilePhoto = files[0];
                    String profileImage = UUID.randomUUID() + "_" + profilePhoto.getOriginalFilename();
                    Path filePath = Paths.get(uploadDir + "trainer/profileImage/", profileImage);
                    profilePhoto.transferTo(filePath.toFile());
                    info.setProfileImage(profileImage);
                }else {
                    info.setProfileImage("");
                }

                // 배경 이미지
                if (files[1] != null && !files[1].isEmpty()) {
                    MultipartFile workPlacePhoto = files[1];
                    String backGroundImage = UUID.randomUUID() + "_" + workPlacePhoto.getOriginalFilename();
                    Path filePath2 = Paths.get(uploadDir + "trainer/backGroundImage/", backGroundImage);
                    workPlacePhoto.transferTo(filePath2.toFile());
                    info.setBackGroundImage(backGroundImage);
                }else {
                    info.setBackGroundImage("");
                }

            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
            System.out.println(info);
           int saved = mapper.updateInfo(info);
            return saved == 1;
        }

        return false;
    }

}
