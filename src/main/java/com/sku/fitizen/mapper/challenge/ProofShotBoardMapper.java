package com.sku.fitizen.mapper.challenge;
import com.sku.fitizen.domain.challenge.PhotoVerification;
import com.sku.fitizen.domain.challenge.ProofShotBoard;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ProofShotBoardMapper {


    // 챌린지별 인증게시판 리스트 가져오기
    List<ProofShotBoard> getProofShotListById(int id);

    // 인증 사진 올리기
    int addProofShot(ProofShotBoard board);

    // 사진 인증 해주기
    int verifyProofShot(PhotoVerification verify);



}
