package com.sku.fitizen.controller.challenge;


import com.sku.fitizen.domain.ChallComment;
import com.sku.fitizen.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/chellComment")
public class ChallCommentController {


        @GetMapping("/addChallComment")
        @ResponseBody
        public Map<String,Boolean> challComment(@SessionAttribute(value = "user" ,required = false) User user  , ChallComment challComment)
        {
            Map<String,Boolean> map = new HashMap<>();

            if(user ==null)
            {
                map.put("success",false);
            }


            return map;
        }




}
