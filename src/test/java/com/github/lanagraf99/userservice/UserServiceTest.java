package com.github.lanagraf99.userservice;

import com.github.lanagraf99.userservice.dao.UserDao;
import com.github.lanagraf99.userservice.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_whenValidData_callsDaoSave() {
        String name = "Иван";
        String email = "ivan@mail.ru";
        Integer age = 30;

        userService.registerUser(name, email, age);

        verify(userDao).save(any(User.class));
    }

    @Test
    void getUser_whenExist_returnUser() {
        User expected = new User("Степан", "stepan@mail.ru", 18);
        when(userDao.findById(1L)).thenReturn(expected);

        User actual = userService.getUser(1L);

        assertEquals(expected, actual);
    }

    @Test
    void deleteUser_whenCalled_callsDaoDelete() {
        userService.deleteUser(1L);

        verify(userDao).delete(1L);
    }

    @Test
    void getUser_whenNotExists_returnsNull() {
        when(userDao.findById(999L)).thenReturn(null);

        User actual = userService.getUser(999L);

        assertNull(actual);
    }
}