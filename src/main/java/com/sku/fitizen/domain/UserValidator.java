package com.sku.fitizen.domain;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UserValidator implements Validator {


    @Override
    //이 검증기로 검증가능한 객체인지 알려주는 메서드
    public boolean supports(Class<?> clazz) {
//		return User.class.equals(clazz); // 검증하려는 객체가 User타입인지 확인
        return User.class.isAssignableFrom(clazz); // clazz가 User 또는 그 자손인지 확인
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User)target;

        String id = user.getId();
        String pwd = user.getPwd();

//		if(id==null || "".equals(id.trim())) {
//			errors.rejectValue("id", "required");
//		}

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "id",  "required");

        if (!user.getId().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{4,10}$")) {
            errors.rejectValue("id", "id.invalid", "아이디는 4~10자리의 영대소문자와 숫자 조합이어야 합니다.");
        }

        // 비밀번호 정규식 검사 (4~10자리의 영대소문자와 숫자 조합)
        if (!user.getPwd().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{4,10}$")) {
            errors.rejectValue("pwd", "pwd.invalid", "비밀번호는 4~10자리의 영대소문자와 숫자 조합이어야 합니다.");
        }

    }
}