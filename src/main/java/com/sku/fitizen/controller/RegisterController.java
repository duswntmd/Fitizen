package com.sku.fitizen.controller;

import com.sku.fitizen.domain.User;
import com.sku.fitizen.domain.UserValidator;
import com.sku.fitizen.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/register")
//@SessionAttributes("user")  // 'user'를 세션에 저장하도록 설정
public class RegisterController {

    @Autowired
    UserService userService;

    final int FAIL = 0;

    @InitBinder
    public void toDate(WebDataBinder binder) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(df, false));
        binder.setValidator(new UserValidator()); // UserValidator를 WebDataBinder의 로컬 validator로 등록
    }

    @GetMapping("/add")
    public String register() {
        return "registerForm"; // WEB-INF/views/registerForm.jsp
    }


    @PostMapping("/add")
    @ResponseBody
    public Map<String, Object> save(@Valid User user, BindingResult result) throws Exception {
        Map<String, Object> response = new HashMap<>();

        // 유효성 검사 오류가 있는 경우
        if (result.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            result.getAllErrors().forEach(error -> {
                errorMsg.append(error.getDefaultMessage()).append("\n"); // 각 오류 메시지를 추가
            });

            response.put("status", "error");
            response.put("message", errorMsg.toString()); // 오류 메시지를 response에 포함

            return response;
        }

        // 아이디 중복 검사
        if (userService.isIdDuplicate(user.getId())) {
            response.put("status", "error");
            response.put("message", "이미 사용 중인 아이디입니다.");
            return response;
        }

        int rowCnt = userService.insertUser(user);
        if (rowCnt != FAIL) {
            response.put("status", "success");
            if(user.getIs_trainer().equals("N")) {
                response.put("message", "회원가입이 완료 되었습니다.");
            } else if(user.getIs_trainer().equals("Y")) {
                response.put("message","트레이너 가입이 완료되었습니다. 트레이너 승인 여부는 마이페이지에서 확인해주세요.");
            }
            return response;
        }

        response.put("status", "error");
        response.put("message", "회원가입 실패. 다시 시도해주세요.");
        return response;
    }

    @GetMapping("/updateuser")
    public String showUpdateUserForm(@SessionAttribute(value = "user", required = false) User user, Model model) {
        if (user == null || user.getId() == null) {
            return "redirect:/login/login";
        }

        try {
            User userDetails = userService.selectUser(user.getId());

            if (userDetails != null) {
                model.addAttribute("user", userDetails);
            } else {
                model.addAttribute("error", "사용자 정보를 불러오지 못했습니다.");
            }
        } catch (Exception e) {
            model.addAttribute("error", "사용자 정보를 불러오는 중 오류가 발생했습니다.");
        }

        return "updateUser";
    }

    @PostMapping("/updateuser")
    @ResponseBody
    public Map<String, Object> updateUser(@ModelAttribute("user") User user,
                                          @RequestParam("pwd") String password,
                                          @RequestParam("name") String name,
                                          @RequestParam("email") String email,
                                          @RequestParam("birth") String birth) {
        Map<String, Object> response = new HashMap<>();


        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.sql.Date birthDate = new java.sql.Date(dateFormat.parse(birth).getTime());

            // 세션에 있는 사용자 정보를 수정
            if (password != null && password.length() > 0) {
                user.setPwd(password);  // 새로운 비밀번호 설정
            }
            // 변경할 정보를 설정
            user.setPwd(password);
            user.setName(name);
            user.setEmail(email);
            user.setBirth(birthDate);

            int rowsAffected = userService.updateUser(user);

            if (rowsAffected > 0) {
                response.put("status", "success");
                response.put("message", "회원 정보가 성공적으로 수정되었습니다.");
            } else {
                response.put("status", "error");
                response.put("message", "회원 정보 수정에 실패했습니다.");
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "회원 정보 수정 중 오류가 발생했습니다.");
            e.printStackTrace();
        }

        return response;
    }

    @GetMapping("/deleteuser")
    public String showDeleteUserForm() {
        return "deleteUser";
    }

    @PostMapping("/deleteuser")
    @ResponseBody
    public Map<String, Object> deleteUser(@RequestParam("id") String id,
                                          @RequestParam("name") String name,
                                          @RequestParam("pwd") String pwd,
                                          SessionStatus status) {
        Map<String, Object> response = new HashMap<>();

        try {
            int rowsAffected = userService.deleteUser(id, name, pwd);
            status.setComplete();
            if (rowsAffected > 0) {
                response.put("status", "success");
                response.put("message", "회원탈퇴가 성공적으로 완료되었습니다.");
            } else {
                response.put("status", "error");
                response.put("message", "아이디, 이름 또는 비밀번호가 일치하지 않습니다.");
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "오류가 발생했습니다. 다시 시도해주세요.");
        }
        return response;
    }

}
