package ru.oleevych.cloudspace.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.oleevych.cloudspace.entity.User;
import ru.oleevych.cloudspace.repository.UserRepository;
import ru.oleevych.cloudspace.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
public class AuthControllerIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void registerUser_validUser_saveRecordInDataBase() {
        User user = new User("admin", "admin1");

        assertDoesNotThrow(() -> userService.registerUser(user));
        assertEquals(userRepository.count(), 1);
    }
}