package com.sku.fitizen.dao;

import com.sku.fitizen.domain.User;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private SqlSession session;
    private static final String namespace = "com.sku.fitizen.dao.UserMapper.";

    @Override
    public int insertUser(User user) {
        return session.insert(namespace + "insertUser", user);
    }

    @Override
    public User selectUser(String id) {
        return session.selectOne(namespace + "selectUser", id);
    }

    @Override
    public int updateUser(User user) {
        return session.update(namespace + "updateUser", user);
    }

    @Override
    public int deleteUser(String id, String name, String pwd) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("pwd", pwd);
        return session.delete(namespace + "deleteUser", map);
    }

    @Override
    public boolean isIdDuplicate(String id) {
        Integer count = session.selectOne(namespace + "isIdDuplicate", id);
        return count != null && count > 0;
    }

    @Override
    public String findIdByEmailAndName(String email, String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("name", name);
        return session.selectOne(namespace + "findIdByEmailAndName", map);
    }

    @Override
    public String findPasswordByEmailAndId(String email, String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("id", id);
        return session.selectOne(namespace + "findPasswordByEmailAndId", map);
    }

    @Override
    public int addPointsToUser(String userId, int points) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("points", points);

        return session.update(namespace + "addPointsToUser", map);
    }
}
