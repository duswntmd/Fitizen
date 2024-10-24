package com.sku.fitizen.controller.challenge;

import com.github.pagehelper.PageInfo;
import com.sku.fitizen.domain.challenge.ChallCategory;
import com.sku.fitizen.domain.challenge.ChallComment;
import com.sku.fitizen.domain.challenge.Challenge;
import com.sku.fitizen.domain.challenge.Participation;
import com.sku.fitizen.domain.User;
import com.sku.fitizen.service.board.PageService;
import com.sku.fitizen.service.challenge.ChallCommentService;
import com.sku.fitizen.service.challenge.ChallengeService;
import com.sku.fitizen.service.challenge.ParticipationService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ChallengController {


    // 챌린지 서비스는 여기서만 사용하기에 변수를 service로
    @Autowired
    ChallengeService service;

    @Autowired
    ChallCommentService challCommentService;

    @Autowired
    private PageService pageService;


    @GetMapping("")
    public String mainPage(@SessionAttribute(value = "user", required = false) User user,
                           @RequestParam(value = "categoryId",defaultValue ="0") int categoryId,
                           Model model) {

        List<Challenge> list;

        // 카테고리에 따라 챌린지 목록을 가져옴

        if (categoryId == 0) {

            list= service.getChallengeList();
        } else if (categoryId == -1) {
            list = service.getChallByCategory(categoryId);
        } else {
            list = service.getChallByCategory(categoryId);
        }

        List<Challenge> userChall = new ArrayList<>();
        if (user != null) {
            userChall = service.getMyChallengeList(user.getId());
        }


        List<Challenge> top3 = service.getTop3Challenge(); // 일단 임시로 참여자 수 많은 순으로(제한유저 꽉찬거 말고)
        model.addAttribute("top3", top3);
        List<ChallCategory> c = service.getChallCategories();
        model.addAttribute("c", c);
        model.addAttribute("userChall", userChall);
        model.addAttribute("list", list);
        model.addAttribute("user", user);
        // 목록이 비어있는지 여부를 모델에 추가
        model.addAttribute("isEmpty", list.isEmpty());
        return "th/chall/challengePage";
    }




    @GetMapping("/search")
    public String searchChallenges(@SessionAttribute(value = "user", required = false) User user,
                                   @RequestParam Map<String, String> info,

                                   Model model) {

        List<Challenge> userChall = new ArrayList<>();
        if (user != null) {
            userChall = service.getMyChallengeList(user.getId());
        }

        List<ChallCategory> c = service.getChallCategories();
        List<Challenge>searchResults = service.searchChallenges(info);

        model.addAttribute("c", c);
        model.addAttribute("userChall", userChall);
        model.addAttribute("list", searchResults);
        return "th/chall//challengePage"; // 검색 결과를 보여줄 페이지로 이동
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
        List<ChallCategory> list = service.getChallCategories();
        model.addAttribute("category", list);
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
        log.info(challenge.toString());
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
        return "redirect:/challenge/myChall";
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
