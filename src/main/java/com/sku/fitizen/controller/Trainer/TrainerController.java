package com.sku.fitizen.controller.Trainer;

import com.sku.fitizen.domain.Trainer.Trainer;
import com.sku.fitizen.domain.User;
import com.sku.fitizen.service.Trainer.TrainerService;
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
@RequestMapping("/trainer")
public class TrainerController
{

    @Autowired
    TrainerService service;


    @GetMapping("")
    public String trainerList(Model model,@SessionAttribute(value = "user" ,required = false)User user)
    {
        List<Trainer> list =service.getTrainersByApproved();

        if (user !=null){
            model.addAttribute("user",user);
        }
        model.addAttribute("trainers", list);
        return "th/trainer/trainerList";
    }
    @GetMapping("/search")
    public String searchTrainerList(@SessionAttribute(value = "user", required = false) User user,
                                    @RequestParam Map<String, String> info,
                                    Model model)
    {
        //System.out.println(info);
        if (user !=null){
            model.addAttribute("user",user);
        }
        List<Trainer> searchResults = service.searchTrainerList(info);
        model.addAttribute("trainers",searchResults);
        return "th/trainer/trainerList";
    }
    @GetMapping("/detail/{trainerNo}")
    public String trainerDetail(Model model, @PathVariable("trainerNo") int trainerNo,@SessionAttribute(value = "user" ,required = false)User user)
    {
        if (user !=null){
            model.addAttribute("user",user);
        }
        Trainer trainer =service.getTrainerDetailById(trainerNo);
        model.addAttribute("trainer", trainer);
        return "th/trainer/trainerDetail";
    }

    @GetMapping("/InfoPage")
    public String updateInfo(@SessionAttribute("user") User user , Model model)
    {
        int no = service.getTrainerNoByUserId(user.getId());
        Trainer info = service.getTrainerDetailById(no);
        model.addAttribute("info", info);
        model.addAttribute("user", user);
        return "th/user/myInfoPage";
    }

    @PostMapping("/updateInfo")
    @ResponseBody
    public Map<String,Boolean> updateInfo(@SessionAttribute("user") User user,
                                          Trainer info,
                                          @RequestParam(required = false) MultipartFile profilePhoto,
                                          @RequestParam(required = false) MultipartFile workPlacePhoto
                                         )
    {
        info.setUserId(user.getId());
        MultipartFile[] files = new MultipartFile[]{profilePhoto, workPlacePhoto};

        boolean saved =service.updateInfo(info,files);
        Map<String,Boolean> map =new HashMap<>();
        map.put("saved",saved);
        return map;
    }

}
