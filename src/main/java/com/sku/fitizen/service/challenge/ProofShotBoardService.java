package com.sku.fitizen.service.challenge;


import com.sku.fitizen.domain.challenge.PhotoVerification;
import com.sku.fitizen.domain.challenge.ProofShotBoard;
import com.sku.fitizen.handler.Base64Image;
import com.sku.fitizen.mapper.challenge.ProofShotBoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ProofShotBoardService {

    @Autowired
    ProofShotBoardMapper  mapper;

    @Autowired
    Base64Image base64ImageService;

    @Value("${file.upload-dir}")
    private String uploadDir;


    // 챌린지 마다 인증 게시판 불러오기
    public List<ProofShotBoard> getProofShotListById(int id)
    {
        List<ProofShotBoard> list =mapper.getProofShotListById(id);

        return  list;
    }

    // 인증 사진 올리기
    public Boolean addProofShot(ProofShotBoard proofShotBoard, MultipartFile file)
    {

        if (!file.isEmpty()) {
            try {
                // 파일 이름 및 경로 설정
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + "proofShot/", fileName);

                // 파일 저장
                file.transferTo(filePath.toFile());

                // 챌린지 객체에 파일 경로 및 원본 파일명 저장
                proofShotBoard.setUPhoto(fileName);
                proofShotBoard.setPhoto(file.getOriginalFilename());

                } catch (IOException e)
                  {
                    e.printStackTrace();
                    return false; // 파일 저장 실패 시 false 반환
                  }

            int result = mapper.addProofShot(proofShotBoard);
            if (result == 1) return true;

        }

        return false;
    }


     // 채팅에서 바로 인증 샷 올리기
    public  boolean addChatProofShot(ProofShotBoard board, String base64Img)
    {

        try {
            String UUIDImgName = UUID.randomUUID() + "_" +board.getPhoto();
            base64ImageService.saveToProofShotBoard(base64Img, UUIDImgName);

            board.setUPhoto(UUIDImgName);
            board.setTitle("임시 제목");
          int result=  mapper.addProofShot(board);
          if (result == 1) return true;

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return  false;
    }


    // 사용자가 인증 사진에 대한 인증해주기

    public boolean verifyProofShot (PhotoVerification verify)
    {

       int result= mapper.verifyProofShot(verify);
       if (result == 1) return true;
        return  false;
    }

    //게시판 삭제
    public boolean deleteProofShot(int proofNum)
    {
        return mapper.deleteProofShot(proofNum)>0;
    }

}
