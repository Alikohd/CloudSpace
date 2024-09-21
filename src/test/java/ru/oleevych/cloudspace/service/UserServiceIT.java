package ru.oleevych.cloudspace.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.oleevych.cloudspace.dto.UserRegisterDto;
import ru.oleevych.cloudspace.entity.User;
import ru.oleevych.cloudspace.exceptions.UserAlreadyExistsException;
import ru.oleevych.cloudspace.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@Testcontainers
@SpringBootTest
class UserServiceIT {

    @Container
    @ServiceConnection
    private final static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.4");

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void registerUser_validUser_saveRecordInDataBase() {
        String username = "admin";
        String password = "admin1";
        UserRegisterDto newUser = new UserRegisterDto(username, password, password);

        assertDoesNotThrow(() -> userService.registerUser(newUser));

        Optional<User> actualUser = userRepository.findByUsername(username);
        assertThat(actualUser).isPresent();
        assertEquals(actualUser.get().getUsername(), username);
        assertTrue(passwordEncoder.matches(password, actualUser.get().getPassword()));
    }

    @Test
    void registerUser_usersWithSameUsernames_throwsUserAlreadyExistsException() {
        UserRegisterDto user1 = new UserRegisterDto("president", "password1", "password1");
        UserRegisterDto user2 = new UserRegisterDto("president", "password2", "password2");

        userService.registerUser(user1);
        assertThatThrownBy(() -> {
            userService.registerUser(user2);
        }).isInstanceOf(UserAlreadyExistsException.class).hasMessageContaining("User with username president already exists");
    }
}