package com.sku.fitizen.service;

import com.sku.fitizen.domain.User;

public interface UserService {
    int insertUser(User user);

    User selectUser(String id);

    int updateUser(User user);

    int deleteUser(String id, String name, String pwd);

    boolean isIdDuplicate(String id);
}
