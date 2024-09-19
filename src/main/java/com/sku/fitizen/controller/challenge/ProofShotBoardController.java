package com.sku.fitizen.controller.challenge;


import com.sku.fitizen.domain.challenge.Participation;
import com.sku.fitizen.domain.challenge.PhotoVerification;
import com.sku.fitizen.domain.challenge.ProofShotBoard;
import com.sku.fitizen.domain.User;
import com.sku.fitizen.service.challenge.ParticipationService;
import com.sku.fitizen.service.challenge.ProofShotBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/proofShot")
public class ProofShotBoardController {


    @Autowired
    ProofShotBoardService service;

    @Autowired
    ParticipationService partiService;

    //  인증사진 게시판으로
    @GetMapping("/{id}")
    public String proofShotMain(@SessionAttribute(value = "user" ,required = false) User user,
                                @PathVariable("id") int id,
                                Model model)
    {
        // 로그인하지 않은 경우
        if (user == null) {
            model.addAttribute("readOnly", true);
        } else {
            // 로그인한 유저가 해당 챌린지에 참여자인지 확인
            Participation parti = new Participation();
            parti.setChallengeId(id);
            parti.setUserId(user.getId());
            boolean existed = partiService.existUser(parti);

            if (existed) {
                model.addAttribute("readOnly", false);
            } else {
                model.addAttribute("readOnly", true);
            }
        }
        // 챌린지마다 인증 게시물이 존재   챌린지에 해당하는 인증 게시물 불러오기
        List<ProofShotBoard> list =service.getProofShotListById(id);
        model.addAttribute("user",user);
        model.addAttribute("id",id);
        model.addAttribute("list",list);
        return "th/chall/proofShotMain";

    }
    // 인증 사진 작성 폼으로
    @GetMapping("/addProofShotForm/{id}")
    public String addProofShotForm(@SessionAttribute(value = "user") User user,
                                   @PathVariable("id") int id,
                                   Model model)
    {
        ProofShotBoard proofShotBoard = new ProofShotBoard();
        proofShotBoard.setChallengeId(id);
        proofShotBoard.setUserId(user.getId());
        model.addAttribute("proofShotBoard", proofShotBoard);
        return "th/chall/addProofShotForm";
    }



    // 인증 사진 게시물 올리기
    @PostMapping("/add")
    @ResponseBody
    public Map<String,Boolean> addProofShot(@SessionAttribute(value ="user") User user,
                                            ProofShotBoard board,
                                            MultipartFile file)
    {
        boolean saved=service.addProofShot(board,file);
        Map<String,Boolean> map = new HashMap<>();
        map.put("saved",saved);
        return map;
    }

    // 채팅방에서 인증게시물로 한번에 올리기
    @PostMapping("/addChatProof")
    @ResponseBody
    public Map<String,Boolean> addChatProof(
                                            ProofShotBoard board,
                                            @RequestParam("base64Img") String base64Img
                                            //@RequestParam("writeDate") String writeDate
                                           )
    {

        boolean success=service.addChatProofShot(board,base64Img);
        Map<String,Boolean> map = new HashMap<>();

        map.put("success",success);
        return map;
    }



    //인증 사진에 따른 사용자 인증 받기
    @PostMapping("/verify")
    @ResponseBody
    public  Map<String,Boolean> verifyProofShot(PhotoVerification  verify, @SessionAttribute(value = "user") User user)
    {

        Map<String,Boolean> map = new HashMap<>();
        verify.setVerifierId(user.getId());

       boolean success= service.verifyProofShot(verify);
       map.put("success",success);
        return  map;

    }

}
