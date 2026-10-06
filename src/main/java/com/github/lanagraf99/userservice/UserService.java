package com.github.lanagraf99.userservice;

import com.github.lanagraf99.userservice.dao.UserDao;
import com.github.lanagraf99.userservice.model.User;

import java.util.List;

public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void registerUser(String name, String email, Integer age) {
        User user = new User(name, email, age);
        userDao.save(user);
    }

    public User getUser(Long id) {
        return userDao.findById(id);
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public void updateUser(User user) {
        userDao.update(user);
    }

    public void deleteUser(Long id) {
        userDao.delete(id);
    }
}
