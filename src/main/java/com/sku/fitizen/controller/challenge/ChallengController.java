package com.sku.fitizen.controller.challenge;


import com.sku.fitizen.domain.challenge.ChallComment;
import com.sku.fitizen.domain.challenge.Challenge;
import com.sku.fitizen.domain.challenge.Participation;
import com.sku.fitizen.domain.User;
import com.sku.fitizen.service.challenge.ChallCommentService;
import com.sku.fitizen.service.challenge.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/challenge")
public class ChallengController {


    // 챌린지 서비스는 여기서만 사용하기에 변수를 service로
    @Autowired
    ChallengeService service;

    @Autowired
    ChallCommentService challCommentService;

    // 챌린지 페이지  버튼(챌린지 목록, 챌린지 작성, 내가 참여한 챌린지)
    @GetMapping("")
    public String mainPage(Model model)
    {

        return "th/chall/challengePage";
    }

    // 전체 첼린지 목록 조회
    @GetMapping("/list")
    public String challList(@SessionAttribute(value = "user", required = false) User user,Model model)
    {
        List<Challenge> list = service.getChallengeList();
        List<Challenge> userChall = new ArrayList<>();
        // 로그인된 사용자일 경우, 참여 중인 챌린지 목록을 가져옴
        if (user != null) {
            userChall = service.getMyChallengeList(user.getId());
        }
        model.addAttribute("userChall", userChall);
        model.addAttribute("list", list);
        return "th/chall/challengeList";
    }


    // 챌린지 작성 폼 이동
    @GetMapping("/add")
    public String challAddForm(@SessionAttribute(value = "user" ,required = false) User user, Model model)
    {
        if (user == null || user.getId() == null) {
            return "redirect:/login/login";
        }
        model.addAttribute("userId",user.getId());
        model.addAttribute("challenge",new Challenge());
        return "th/chall/challengeAddForm";
    }

    // 챌린지 작성 폼 값 전달 받는 곳 : 챌린지 등록
    @PostMapping("/save")
    @ResponseBody
    public Map<String,Boolean> challSave(@SessionAttribute(value = "user",required = false) User user,
                                         @ModelAttribute Challenge challenge,
                                         MultipartFile file
                                         )
    {

        challenge.setCreatorId(user.getId());
        boolean saved=  service.saveChallenge(challenge,file);
        Map<String,Boolean> map =new HashMap<>();
        map.put("saved",saved);

        return map ;
    }

    // 챌린지 고유 번호로 챌린지 상세정보 로딩 ok?
    @GetMapping("/detail/{id}")
    public String challDetailForm(@SessionAttribute(value = "user",required = false) User user,
                                  @PathVariable("id") Integer id,
                                  Model model
                                 )
    {
        Challenge challe=service.getChallengeById(id);

        List<ChallComment> list =challCommentService.getChallCommentList(id);
        model.addAttribute("user",user);
        model.addAttribute("list",list);
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
        if (user == null || user.getId() == null) {
            return "redirect:/login/login";
        }

        String userId = user.getId();
        List<Challenge> myChall=service.getMyChallengeList(userId);
        model.addAttribute("userId",userId);
        model.addAttribute("myChall",myChall);
        return "th/chall/myChallengePage";
    }








}
