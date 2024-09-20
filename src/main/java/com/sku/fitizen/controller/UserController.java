package com.sku.fitizen.controller;


import com.sku.fitizen.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/myPage")
    public String myPage(@SessionAttribute(value = "user") User user , Model model ) {
        model.addAttribute("user", user);

        System.out.println(user.getId());
        return "th/user/myPage";
    }

}
