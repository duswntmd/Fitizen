package com.sku.fitizen.service;

import com.sku.fitizen.dao.UserDao;
import com.sku.fitizen.domain.User;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;
    //    @Autowired
    //EmailService emailService; // 이메일 서비스 주입

    @Override
    @Transactional
    public int insertUser(User user) {
        int userInsertResult = userDao.insertUser(user); // 사용자 삽입

        if ("Y".equals(user.getIs_trainer())) {
            // 트레이너일 경우 트레이너 테이블에 삽입
            int trainerInsertResult = userDao.insertTrainer(user);


            if (userInsertResult > 0 && trainerInsertResult > 0) {
                return trainerInsertResult;
            } else {
                return -1;
            }
        }

        return userInsertResult; // 일반 유저 경우 사용자 삽입 결과 반환
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

    @Override
    public int addPointsToUser(String userId, int points) {
        return userDao.addPointsToUser(userId,points);
    }


//    public String findIdByEmailAndName(String email, String name) throws Exception {
    @Override
    public String findEmailByIdAndName(String id, String name){
        return userDao.findEmailByIdAndName(id,name);

    }
    @Override
    public boolean changePwd(String pwd, String id){
        return userDao.changePwd(pwd,id);
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
