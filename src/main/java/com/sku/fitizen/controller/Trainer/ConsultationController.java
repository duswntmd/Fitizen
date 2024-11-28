package com.sku.fitizen.controller.Trainer;

import com.sku.fitizen.domain.Trainer.Consultation;
import com.sku.fitizen.domain.User;
import com.sku.fitizen.service.Trainer.ConsultationService;
import com.sku.fitizen.service.Trainer.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/consultation")
public class ConsultationController {

    @Autowired
    ConsultationService cService;

    @Autowired
    TrainerService tService;

    // 트레이너 상담신청
    @GetMapping("/{trainerNo}")
    @ResponseBody
    public Map<String,Object> consultation(@SessionAttribute(value = "user") User user, @PathVariable int trainerNo)
    {

        Map<String, Object> result = new HashMap<>();

        if (user == null) {
            result.put("success", false);
            result.put("message", "로그인이 필요합니다.");
            return result;
        }

        if(user.getIs_trainer().equals("Y"))
        {
            result.put("success", false);
            result.put("message","트레이너는 트레이너 상담 서비스를 이용할 수 없습니다.");
            return result;
        }

        // 이미 신청한 상담인지
       if(cService.existByUserId(new Consultation(user.getId(),trainerNo))>0)
        {
            result.put("success", false);
            result.put("message","이미 상담신청된 트레이너입니다. ");
            return result;
        }

        // 유저(트레이너) -> 자기 자신에 상담을 신청하는가 (불가능하게) 세션Id = trainerNo의 userid
        String userId = tService.getUserIdByTrainerNo(trainerNo);
        if(user.getId().equals(userId))
        {
            result.put("success", false);
            result.put("message","본인에게 상담을 신청할 수는 없습니다.");
            return result;
        }

        // 상담 신청
        Consultation consult = new Consultation(user.getId(),trainerNo);
        if(cService.saveConsultation(consult))
            result.put("success", true);
        else {
            result.put("success", false);
            result.put("message", "상담신청 실패");
        }

        return result;
    }

    //일반유저: 내가 신청한  트레이너 상담 ,트레이너: 상담신청한 유저목록
    @GetMapping("/myConsultation")
    public String consultation(@SessionAttribute("user")User user,  Model model)
    {

        if(user.getIs_trainer().equals("Y")) // 트레이너일 경우
          {
              int trainerNo=tService.getTrainerNoByUserId(user.getId());
             List<Map<String, Object>> list =cService.getMyUsers(trainerNo);
             model.addAttribute("list", list);
             List<Map<String, Object>> approvedList =cService.getMyUsersByApproved(trainerNo);
             model.addAttribute("approvedList", approvedList);
          }
        else if(user.getIs_trainer().equals("N")) // 일반 유저일 경우
         {
             //'REQUESTED'(기본값:요청됨), 'APPROVED(승인됨)', 'REJECTED(거절됨)'
             List<Map<String, Object>> list =cService.getMyTrainers(user.getId());
             model.addAttribute("list", list);
             List<Map<String, Object>> approvedList =cService.getMyTrainersByApproved(user.getId());
             model.addAttribute("approvedList", approvedList);
         }
        model.addAttribute("user", user);
        return "th/user/myConsultation";
    }

    // 상담 취소 -유저
    @GetMapping("/cancel/{trainerNo}")
    @ResponseBody
    public Map<String,Boolean> cancel(@PathVariable int trainerNo,@SessionAttribute("user") User user)
    {
        Map<String, Boolean> map = new HashMap<>();

        Consultation consult = new Consultation(user.getId(),trainerNo);
        boolean result = cService.cancel(consult);
        if(result)
        {
            map.put("success", true);
            return map;
        }

        map.put("success", false);
        return map;
    }


    // 상담 승인- 트레이너
    @GetMapping("/approve/{userId}")
    @ResponseBody
    public Map<String,Boolean> approve(@SessionAttribute("user")User user, @PathVariable String userId)
    {
        Map<String, Boolean> map = new HashMap<>();
        int trainerNo=tService.getTrainerNoByUserId(user.getId());
        Consultation consult = new Consultation(userId,trainerNo);

        if(cService.approve(consult))
        {
            map.put("approved", true);
            return map;
        }

        map.put("approved", false);
        return map;
    }



    // 상담 거절- 트레이너
    @GetMapping("/reject/{userId}")
    @ResponseBody
    public Map<String,Boolean> reject(@SessionAttribute("user")User user, @PathVariable String userId)
    {
        Map<String, Boolean> map = new HashMap<>();
        int trainerNo=tService.getTrainerNoByUserId(user.getId());
        Consultation consult = new Consultation(userId,trainerNo);

        if(cService.reject(consult))
        {
            map.put("rejected", true);
            return map;
        }

        map.put("rejected", false);
        return map;
    }


    // 상담 재신청
    @GetMapping("/reapply/{trainerNo}")
    @ResponseBody
    public Map<String,Boolean> reapply(@SessionAttribute("user")User user, @PathVariable int trainerNo)
    {
        Map<String, Boolean> map = new HashMap<>();
        int success = cService.reapplyConsultation(new Consultation(user.getId(),trainerNo));
        if(success > 0)
        {
            map.put("success", true);
            return map;
        }
        map.put("rejected", false);
        return map;
    }

}
