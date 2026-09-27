package com.github.lanagraf99.userservice.dao;

import com.github.lanagraf99.userservice.model.User;
import java.util.List;

public interface UserDao {
    void save(User user);
    User findById(Long id);
    List<User> findAll();
    void update(User user);
    void delete(Long id);
}