package com.github.lanagraf99.userservice.service;

import com.github.lanagraf99.userservice.dto.Operation;
import com.github.lanagraf99.userservice.dto.UserEvent;
import com.github.lanagraf99.userservice.exception.UserNotFoundException;
import com.github.lanagraf99.userservice.kafka.UserEventProducer;
import com.github.lanagraf99.userservice.repository.UserRepository;
import com.github.lanagraf99.userservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserEventProducer userEventProducer;

    @Autowired
    public UserService(UserRepository userRepository, UserEventProducer userEventProducer) {
        this.userRepository = userRepository;
        this.userEventProducer = userEventProducer;
    }

    public User createUser(User user) {
        User saved = userRepository.save(user);
        userEventProducer.sendUserEvent(new UserEvent(saved.getEmail(), Operation.CREATE));
        return saved;
    }
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public User updateUser(User user) {
       return userRepository.save(user);
    }
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                        .orElseThrow(() -> new UserNotFoundException(id));
        userEventProducer.sendUserEvent(new UserEvent(user.getEmail(), Operation.DELETE));
        userRepository.deleteById(id);
    }
}
