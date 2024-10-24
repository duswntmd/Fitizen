package com.sku.fitizen.mapper;

import com.sku.fitizen.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    User selectUserByUsername(String id);

    // 사용자 추가
    int insertUser(User user);

    // 권한 정보 저장
    void insertUserRole(@Param("id") String username, @Param("authority") String authority);

    // 트레이너 추가
    int insertTrainer(User user);

    // 사용자 목록 찾기 (로그인 등)
    User selectUser(String id);

    // 사용자 수정
    int updateUser(User user);

    // 사용자 삭제
    int deleteUser(@Param("id") String id, @Param("name") String name);

    int deleteUserAuthorities(@Param("id") String id);

    // 아이디 중복 체크
    int isIdDuplicate(String id);

    // 이메일과 이름으로 아이디 찾기
    String findIdByEmailAndName(@Param("email") String email, @Param("name") String name);

    // 이메일과 아이디로 비밀번호 찾기
    String findPasswordByEmailAndId(@Param("email") String email, @Param("id") String id);

    // 사용자 포인트 적립
    int addPointsToUser(@Param("userId") String userId, @Param("points") int points);

    // 이메일 찾기
    String findEmailByIdAndName(@Param("id") String id, @Param("name") String name);

    // 비밀번호 변경
    int changePwd(@Param("pwd") String pwd, @Param("id") String id);
}
