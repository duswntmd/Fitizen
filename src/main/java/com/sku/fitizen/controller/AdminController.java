package com.sku.fitizen.controller;


import com.sku.fitizen.domain.Trainer.Trainer;
import com.sku.fitizen.domain.User;
import com.sku.fitizen.service.Trainer.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private TrainerService tService;


    @GetMapping("/getTrainers")
    public String getTrainers(@SessionAttribute(value = "user") User user, Model model)
    {
        List<Trainer> unapproved =tService.getTrainersByUnapproved();
        List<Trainer> approved =tService.getTrainersByApproved();
        // Null 대신 빈 리스트로 초기화
        if (unapproved == null) {
            unapproved = new ArrayList<>();
        }
        if (approved == null) {
            approved = new ArrayList<>();
        }
        model.addAttribute("unapproved", unapproved);
        model.addAttribute("approved", approved);
        return "th/admin/trainers";
    }


    @GetMapping("/approveTrainer/{trainerNo}")
    @ResponseBody
    public Map<String,Boolean> approveTrainer(@SessionAttribute(value = "user") User user,@PathVariable int trainerNo) {
        Map<String,Boolean> result = new HashMap<>();

        if(tService.approveTrainer(trainerNo)>0){
            result.put("success",true);
            return result;
        }
        result.put("success",false);
        return result;
    }

    @GetMapping("/getUsers")
    public String getUsers(Model model,@SessionAttribute("user")User user)
    {
        return "th/admin/users";
    }

}
