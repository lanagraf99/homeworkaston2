package com.github.lanagraf99.userservice.controller;

import com.github.lanagraf99.userservice.dto.UserMapper;
import com.github.lanagraf99.userservice.dto.UserRequestDto;
import com.github.lanagraf99.userservice.dto.UserResponseDto;
import com.github.lanagraf99.userservice.model.User;
import com.github.lanagraf99.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public UserResponseDto getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        return UserMapper.toResponseDto(user);
    }

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(UserMapper::toResponseDto)
                .toList();
    }

    @PostMapping
    public UserResponseDto createUser(@RequestBody UserRequestDto request) {
        User user = UserMapper.toEntity(request);
        User saved = userService.createUser(user);
        return UserMapper.toResponseDto(saved);
    }

    @PutMapping("/{id}")
    public UserResponseDto updateUser(@PathVariable Long id, @RequestBody UserRequestDto request) {
        User existing = userService.getUser(id);
        if (existing == null) {
            throw new RuntimeException("User not found: " + id);
        }
        existing.setName(request.getName());
        existing.setEmail(request.getEmail());
        existing.setAge(request.getAge());
        User updated = userService.updateUser(existing);
        return UserMapper.toResponseDto(updated);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}