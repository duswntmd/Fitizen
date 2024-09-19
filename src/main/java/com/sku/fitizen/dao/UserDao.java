package com.sku.fitizen.dao;

import com.sku.fitizen.domain.User;

public interface UserDao {
    // 사용자 추가
    int insertUser(User user);

    // 사용자 목록
    User selectUser(String id);

    // 사용자 수정
    int updateUser(User user);

    // 사용자 삭제
    int deleteUser(String id, String name, String pwd);

    // 중복체크
    boolean isIdDuplicate(String id);

    // 사용자 아이디 찾기
    String findIdByEmailAndName(String email, String name);

    // 사용자 비밀번호 찾기
    String findPasswordByEmailAndId(String email, String id);

    // 포인트 충전
    int  addPointsToUser(String userId,int points);

    String findEmailByIdAndName(String id, String name);

    boolean changePwd( String pwd,String id);
}
