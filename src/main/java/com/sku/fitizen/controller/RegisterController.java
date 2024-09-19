package com.sku.fitizen.controller;

import com.sku.fitizen.domain.User;
import com.sku.fitizen.domain.UserValidator;
import com.sku.fitizen.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/register")
@SessionAttributes("user")  // 'user'를 세션에 저장하도록 설정
public class RegisterController {

    @Autowired
    UserService UserService;

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
    public Map<String, Object> save(@Valid User user, BindingResult result, Model model) throws Exception {
        Map<String, Object> response = new HashMap<>();

        if (!result.hasErrors()) {
            if (UserService.isIdDuplicate(user.getId())) {
                response.put("status", "error");
                response.put("message", "이미 사용 중인 아이디입니다.");
                return response;
            }

            int rowCnt = UserService.insertUser(user);

            if (rowCnt != FAIL) {
                response.put("status", "success");
                response.put("message", "회원가입 성공");
                return response;
            }
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
            User userDetails = UserService.selectUser(user.getId());

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
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestParam("pwd") String password,
                             @RequestParam("name") String name,
                             @RequestParam("email") String email,
                             @RequestParam("birth") String birth,
                             RedirectAttributes rattr,
                             Model model) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.sql.Date birthDate = new java.sql.Date(dateFormat.parse(birth).getTime());

            user.setPwd(password);
            user.setName(name);
            user.setEmail(email);
            user.setBirth(birthDate);

            int rowsAffected = UserService.updateUser(user);

            if (rowsAffected > 0) {
                rattr.addFlashAttribute("msg", "WRT_OK");
                return "redirect:/register/updateuser";
            } else {
                model.addAttribute("error", "사용자 정보 업데이트에 실패했습니다.");
            }
        } catch (ParseException e) {
            model.addAttribute("error", "생년월일 형식이 올바르지 않습니다.");
        } catch (Exception e) {
            model.addAttribute("error", "사용자 정보 업데이트 중 오류가 발생했습니다: ");
        }
        return "updateUser";
    }

    @GetMapping("/deleteuser")
    public String showDeleteUserForm() {
        return "deleteUser";
    }

    @PostMapping("/deleteuser")
    public String deleteUser(@ModelAttribute("user") User user,
                             RedirectAttributes rattr,
                             Model model,
                             HttpSession session) {  // HttpSession 추가
        try {
            int rowsAffected = UserService.deleteUser(user.getId(), user.getName(), user.getPwd());

            if (rowsAffected > 0) {
                model.addAttribute("message", "회원탈퇴가 성공했습니다.");
                rattr.addFlashAttribute("msg", "DEL_OK");

                session.invalidate();  // 회원 탈퇴 성공 시 세션 무효화

                return "redirect:/register/deleteuser";
            } else {
                model.addAttribute("error", "회원탈퇴가 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "회원탈퇴 중 오류가 발생했습니다.");
        }
        return "deleteUser";
    }
}
