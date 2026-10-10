package com.github.lanagraf99.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lanagraf99.userservice.dto.UserRequestDto;
import com.github.lanagraf99.userservice.model.User;
import com.github.lanagraf99.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class UserControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>
            ("postgres:16-alpine");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();
    }

    @Test
    void createUser_whenValidRequest_returnsCreateUser() throws Exception {
        UserRequestDto request = new UserRequestDto();
        request.setName("Лупа");
        request.setEmail("lupa@mail.ru");
        request.setAge(30);

        mockMvc.perform(post("/api/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Лупа"))
                .andExpect(jsonPath("$.email").value("lupa@mail.ru"));
    }

    @Test
    void getAllUsers_whenUsersExist_returnList() throws Exception {
        userRepository.save(new User("Хуба>", "hoob@mail.ru", 70));
        userRepository.save(new User("Буба>", "boob@mail.ru", 45));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getUser_whenExist_returnUser() throws Exception {
        User saved = userRepository.save(new User("Пупупу", "pupupu@mail.ru", 15));

        mockMvc.perform(get("/api/users/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Пупупу"))
                .andExpect(jsonPath("$.email").value("pupupu@mail.ru"));
    }

    @Test
    void deleteUser_whenExist_removesUser() throws Exception {
        User saved = userRepository.save(new User("Пупа", "pupa@mail.ru", 15));

        mockMvc.perform(delete("/api/users/" + saved.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/users/" + saved.getId()))
                .andExpect(status().isNotFound());
    }
}