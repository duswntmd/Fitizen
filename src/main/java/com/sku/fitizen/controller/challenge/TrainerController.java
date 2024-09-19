package com.sku.fitizen.controller.challenge;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/trainer")
public class TrainerController
{
    @GetMapping("")
    public String trainerList(Model model)
    {
        return "th/chall/trainerList";
    }
}
