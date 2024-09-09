package com.sku.fitizen.controller;


import com.sku.fitizen.domain.Challenge;
import com.sku.fitizen.domain.Participation;
import com.sku.fitizen.domain.User;
import com.sku.fitizen.service.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/challenge")
@SessionAttributes("user")
public class ChallengController {

    // 챌린지 서비스는 여기서만 사용하기에 변수를 service로
    @Autowired
    ChallengeService service;



    // 챌린지 페이지  버튼(챌린지 목록, 챌린지 작성, 내가 참여한 챌린지)
    @GetMapping("")
    public String mainPage(Model model)
    {
        return "th/chall/challengePage";
    }

    // 전체 첼린지 목록 조회
    @GetMapping("/list")
    public String challList(Model model)
    {
        List<Challenge> list =service.getChallengeList();
        model.addAttribute("list",list);
        return "th/chall/challengeList";
    }

    // 챌린지 작성 폼 이동
    @GetMapping("/add")
    public String challAddForm( Model model)
    {
        Challenge chall=new Challenge();
        //model.addAttribute("chall",chall);
        return "th/chall/challengeAddForm";
    }

    // 챌린지 작성 폼 값 전달 받는 곳 : 챌린지 등록
    @PostMapping("/save")
    public String challSave(@SessionAttribute(value = "user",required = false) User user,Challenge challenge)
    {

        challenge.setCreatorId(user.getId());
        boolean success =service.saveChallenge(challenge);
        System.out.println(success);
        return "redirect:/challenge/list";
    }

    // 챌린지 고유 번호로 챌린지 상세정보 로딩 ok?
    @GetMapping("/detail/{id}")
    public String challDetailForm(@PathVariable("id") Integer id, Model model)
    {
        Challenge challe=service.getChallengeById(id);
        model.addAttribute("challe",challe);
        return "th/chall/challengeDetail";
    }


    // 이용자 챌린지 참여하기
    @GetMapping("/participate/{challengeId}")
    public  String participate(@SessionAttribute(value = "user",required = false) User user ,@PathVariable Integer challengeId)
    {
        String userId = user.getId();
        service.participate(new Participation(userId,challengeId));
        return "th/chall/myChallengePage";
    }

    // 내가 참여한 챌린지로 이동
    @GetMapping("/myChall")
    public String myChallList(@SessionAttribute(value = "user", required = false) User user,Model model)
    {
        String userId = user.getId();

        List<Challenge> myChall=service.getMyChallengeList(userId);

        model.addAttribute("myChall",myChall);
        return "th/chall/myChallengePage";
    }

}
