package ru.oleevych.cloudspace.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.oleevych.cloudspace.entity.User;
import ru.oleevych.cloudspace.exceptions.UserAlreadyExistsException;
import ru.oleevych.cloudspace.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    public final UserRepository userRepository;
    public final PasswordEncoder encoder;
    public void registerUser(User user) {
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            ConstraintViolationException constraintViolationException = (ConstraintViolationException) e.getCause();
            String constraintName = constraintViolationException.getConstraintName();
            if ("unique_username".equals(constraintName)){
                throw new UserAlreadyExistsException(String.format
                        ("User with username %s already exists", user.getUsername()), constraintViolationException);
            }

        }
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

}
