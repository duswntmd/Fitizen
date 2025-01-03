package com.sku.fitizen.service;

import com.sku.fitizen.domain.User;
import com.sku.fitizen.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public User findUserByUsername(String id) {
        return userMapper.selectUserByUsername(id);
    }
    // 사용자 등록
    @Transactional
    public int insertUser(User user) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(user.getPwd());
        user.setPwd(encodedPassword);
        user.setEnabled(1);  // 계정 활성화 여부 설정

        // 사용자 정보 삽입
        int rowsAffected = userMapper.insertUser(user);  // insert 성공 시 영향 받은 행 수 반환
        if(user.getIs_trainer().equals("Y")) {
            userMapper.insertTrainer(user);
        }

        // 삽입이 성공하면 권한 추가
        if (rowsAffected > 0) {
            userMapper.insertUserRole(user.getId(), "ROLE_USER");  // 기본 사용자 역할 추가
        }

        return rowsAffected;  // 영향 받은 행 수를 반환
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
        // 새 비밀번호를 암호화
        String encodedPassword = passwordEncoder.encode(newPassword);

        // 암호화된 비밀번호를 사용하여 업데이트
        int result = userMapper.changePwd(encodedPassword, id);
        return result > 0;  // 변경된 레코드가 있을 경우 true 반환
    }


    //  유저 아이디로 is trainer  Y or N 조회
    public char isTrainer (String userId)
    {
        System.out.println("asdasdasd"+userMapper.isTrainer(userId));
        return userMapper.isTrainer(userId);
    }

}
