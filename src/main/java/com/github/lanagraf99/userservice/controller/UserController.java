package com.github.lanagraf99.userservice.controller;

import com.github.lanagraf99.userservice.dto.UserMapper;
import com.github.lanagraf99.userservice.dto.UserRequestDto;
import com.github.lanagraf99.userservice.dto.UserResponseDto;
import com.github.lanagraf99.userservice.model.User;
import com.github.lanagraf99.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Users", description = "Операции с пользователями")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Найти пользователя по Id")
    @GetMapping("/{id}")
    public EntityModel<UserResponseDto> getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        UserResponseDto dto = UserMapper.toResponseDto(user);

        EntityModel<UserResponseDto> model = EntityModel.of(dto);
        model.add(Link.of("http://localhost:8080/api/users/" + id).withSelfRel());
        model.add(Link.of("http://localhost:8080/api/users").withRel("all-users"));
        return model;
    }

    @Operation(summary = "Получить всех пользователей")
    @GetMapping
    public CollectionModel<EntityModel<UserResponseDto>> getAllUsers() {
        List<EntityModel<UserResponseDto>> users = userService.getAllUsers().stream()
                .map(user -> {
                    UserResponseDto dto = UserMapper.toResponseDto(user);
                    EntityModel<UserResponseDto> model = EntityModel.of(dto);
                    model.add(Link.of("http://localhost:8080/api/users/" + user.getId()).withSelfRel());
                    return model;
                })
                .toList();

        CollectionModel<EntityModel<UserResponseDto>> collection = CollectionModel.of(users);
        collection.add(Link.of("http://localhost:8080/api/users").withSelfRel());
        return collection;
    }

    @Operation(summary = "Создать нового пользователя")
    @PostMapping
    public UserResponseDto createUser(@RequestBody UserRequestDto request) {
        User user = UserMapper.toEntity(request);
        User saved = userService.createUser(user);
        return UserMapper.toResponseDto(saved);
    }

    @Operation(summary = "Обновить пользователя")
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

    @Operation(summary = "Удалить пользователя")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}