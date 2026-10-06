package com.github.lanagraf99.userservice.dao;

import com.github.lanagraf99.userservice.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class UserDaoImplTest {

    @Container
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine");

    private static SessionFactory sessionFactory;
    private UserDaoImpl userDao;

    @BeforeAll
    static void setUp() {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");

        configuration.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", postgres.getUsername());
        configuration.setProperty("hibernate.connection.password", postgres.getPassword());
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        sessionFactory = configuration.buildSessionFactory();
    }

    @AfterAll
    static void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @BeforeEach
    void initDao() {
        userDao = new UserDaoImpl(sessionFactory);
        clearTable();
    }

    private void clearTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createMutationQuery("DELETE FROM User").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    void save_thenFindById_returnsSavedUser() {
        User user = new User("Иван", "ivan@mail.ru", 30);
        userDao.save(user);

        User found = userDao.findById(user.getId());

        assertNotNull(found);
        assertEquals("Иван", found.getName());
        assertEquals("ivan@mail.ru", found.getEmail());
        assertEquals(30, found.getAge());
    }

    @Test
    void findAll_whenUsersExist_returnsAllUsers() {
        User user1 = new User("Пророк санбой", "sunboy@mail.ru", 60);
        User user2 = new User("Серёга Пират", "pirat@mail.ru", 30);
        userDao.save(user1);
        userDao.save(user2);

        List<User> result = userDao.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void update_whenUserModified_persistsChanges() {
        User user = new User("Пророк санбой", "sunboy@mail.ru", 60);
        userDao.save(user);

        user.setName("Санбой Пророк");
        user.setAge(65);
        userDao.update(user);

        User updated = userDao.findById(user.getId());
        assertNotNull(updated);
        assertEquals("Санбой Пророк", updated.getName());
        assertEquals(65, updated.getAge());
    }

    @Test
    void findById_whenNotExists_returnsNull() {
        User found = userDao.findById(999L);
        assertNull(found);
    }
}
