package com.sku.fitizen.service;

import com.sku.fitizen.domain.User;
import com.sku.fitizen.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User findUserByUsername(String username) {
        return userMapper.selectUserByUsername(username);
    }
    // 사용자 등록
    public int insertUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPwd());
        user.setPwd(encodedPassword);
        user.setEnabled(1);

        userMapper.insertUser(user);

        userMapper.insertUserRole(user.getId(), "ROLE_USER");
        return 0;
    }

    // 트레이너 등록
    public void insertTrainer(User user) {
        userMapper.insertTrainer(user);
    }

    // 사용자 정보 조회
    public User selectUser(String id) {
        return userMapper.selectUser(id);
    }

    // 사용자 정보 업데이트
    public int updateUser(User user) {
        // 만약 비밀번호가 변경되었다면 암호화
        if (user.getPwd() == null || user.getPwd().isEmpty()) {
            User existingUser = userMapper.selectUser(user.getId());
            user.setPwd(existingUser.getPwd());
        } else {
            // 비밀번호가 변경될 경우에만 암호화 처리
            String encodedPassword = passwordEncoder.encode(user.getPwd());
            user.setPwd(encodedPassword);
        }

        // 업데이트 실행
        return userMapper.updateUser(user);
    }

    // 사용자 삭제
    public int deleteUser(String id, String name, String pwd) {
        try {
            // 데이터베이스에서 사용자 정보 조회
            User user = userMapper.selectUser(id);
            if (user == null) {
                log.info("아이디: {}", userMapper.selectUser(id));
                return 0; // 해당 사용자가 없으면 삭제 불가
            }

            // 암호화된 비밀번호와 입력된 비밀번호 비교
            if (!passwordEncoder.matches(pwd, user.getPwd())) {
                log.info("비밀번호가 일치하지 않습니다. 입력된 비밀번호: {}, 저장된 비밀번호: {}", pwd, user.getPwd());
                return 0; // 비밀번호가 일치하지 않으면 삭제 불가
            }
            userMapper.deleteUserAuthorities(id);
            log.info("Received delete request for user: id={}, name={}, pwd={}", id, name, pwd);
            // 비밀번호가 일치하면 사용자 삭제
            return userMapper.deleteUser(id, name);
        } catch (Exception e) {
            throw new RuntimeException("사용자 삭제 중 오류가 발생했습니다.", e);
        }
    }

    // 아이디 중복 체크
    public boolean isIdDuplicate(String id) {
        return userMapper.isIdDuplicate(id) > 0;
    }

    // 이메일과 이름으로 아이디 찾기
    public String findIdByEmailAndName(String email, String name) {
        return userMapper.findIdByEmailAndName(email, name);
    }

    // 이메일과 아이디로 비밀번호 찾기
    public String findPasswordByEmailAndId(String email, String id) {
        return userMapper.findPasswordByEmailAndId(email, id);
    }

    // 사용자 포인트 적립
    public void addPointsToUser(String userId, int points) {
        userMapper.addPointsToUser(userId, points);
    }

    // 이메일 찾기
    public String findEmailByIdAndName(String id, String name) {
        return userMapper.findEmailByIdAndName(id, name);
    }

    // 비밀번호 변경
    public boolean changePwd(String newPassword, String id) {
        int result = userMapper.changePwd(newPassword, id);
        return result > 0;  // 변경된 레코드가 있을 경우 true 반환
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
