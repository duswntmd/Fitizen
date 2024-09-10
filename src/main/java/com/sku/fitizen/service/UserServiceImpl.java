package com.sku.fitizen.service;

import com.sku.fitizen.dao.UserDao;
import com.sku.fitizen.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;
    //    @Autowired
    //EmailService emailService; // 이메일 서비스 주입

    @Override
    public int insertUser(User user) {
        return userDao.insertUser(user);
    }

    @Override
    public User selectUser(String id)  {
        return userDao.selectUser(id);
    }

    @Override
    public int updateUser(User user)  {
        return userDao.updateUser(user);
    }

    @Override
    public int deleteUser(String id, String name, String pwd)  {
        return userDao.deleteUser(id, name, pwd);
    }

    @Override
    public boolean isIdDuplicate(String id)  {
        return userDao.isIdDuplicate(id);
    }

    @Override
    public String findIdByEmailAndName(String email, String name)  {
        return userDao.findIdByEmailAndName(email, name);
    }
    @Override
    public String findPasswordByEmailAndId(String email, String id)  {
        return userDao.findPasswordByEmailAndId(email, id);
    }
//    public String findIdByEmailAndName(String email, String name) throws Exception {
//        String id = userDao.findIdByEmailAndName(email, name);
//
//        if (id != null) {
//            // 비밀번호를 이메일로 전송
//            EmailDto emailDto = new EmailDto();
//            emailDto.setSenderName("Your Application");
//            emailDto.setSenderMail("your-application@example.com");
//            emailDto.setReceiveMail(email);
//            emailDto.setSubject("Your id Recovery");
//            emailDto.setMessage("Your id is: " + id);
//
//            emailService.sendMail(emailDto); // 이메일 전송
//        } else {
//            // 해당 이메일과 아이디로 가입된 사용자가 없는 경우 처리
//        }
//
//        return id;
//    }


//    public String findPasswordByEmailAndId(String email, String id) throws Exception {
//        String password = userDao.findPasswordByEmailAndId(email, id);
//
//        if (password != null) {
//            // 비밀번호를 이메일로 전송
//            EmailDto emailDto = new EmailDto();
//            emailDto.setSenderName("Your Application");
//            emailDto.setSenderMail("your-application@example.com");
//            emailDto.setReceiveMail(email);
//            emailDto.setSubject("Your Password Recovery");
//            emailDto.setMessage("Your password is: " + password);
//
//            emailService.sendMail(emailDto); // 이메일 전송
//        } else {
//            // 해당 이메일과 아이디로 가입된 사용자가 없는 경우 처리
//        }
//
//        return password;
//    }

}
