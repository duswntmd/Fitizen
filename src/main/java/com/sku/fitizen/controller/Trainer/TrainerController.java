package com.sku.fitizen.controller.Trainer;

import com.sku.fitizen.domain.Trainer.Trainer;
import com.sku.fitizen.service.Trainer.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/trainer")
public class TrainerController
{

    @Autowired
    TrainerService service;


    @GetMapping("")
    public String trainerList(Model model)
    {
        List<Trainer> list =service.getTrainersByApproved();

        model.addAttribute("trainers", list);

        return "th/trainer/trainerList";
    }



    @GetMapping("/detail/{trainerNo}")
    public String trainerDetail(Model model, @PathVariable("trainerNo") int trainerNo)
    {
        Trainer trainer =service.getTrainerDetailById(trainerNo);
        model.addAttribute("trainer", trainer);
        return "th/trainer/trainerDetail";
    }


}
