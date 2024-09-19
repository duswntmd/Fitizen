package com.sku.fitizen.service;

import com.sku.fitizen.domain.User;

public interface UserService {
    int insertUser(User user);

    User selectUser(String id);

    int updateUser(User user);

    int deleteUser(String id, String name, String pwd);

    boolean isIdDuplicate(String id);

    String findIdByEmailAndName(String email, String name);

    String findPasswordByEmailAndId(String email, String id);




   int addPointsToUser(String userId, int points);

    String findEmailByIdAndName(String id, String name);

    boolean changePwd(String pwd, String id);
}
