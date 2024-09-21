package ru.oleevych.cloudspace.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.oleevych.cloudspace.dto.UserRegisterDto;
import ru.oleevych.cloudspace.entity.User;
import ru.oleevych.cloudspace.exceptions.PasswordsMismatchException;
import ru.oleevych.cloudspace.exceptions.UserAlreadyExistsException;
import ru.oleevych.cloudspace.mapper.UserRegisterMapper;
import ru.oleevych.cloudspace.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    public final UserRepository userRepository;
    public final PasswordEncoder encoder;
    public final UserRegisterMapper userRegisterMapper;

    public void registerUser(UserRegisterDto userDto) {
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            throw new PasswordsMismatchException("Passwords didn't match!");
        }

        String encodedPassword = encoder.encode(userDto.getPassword());
        userDto.setPassword(encodedPassword);
        try {
            userRepository.save(userRegisterMapper.toUser(userDto));
        } catch (DataIntegrityViolationException e) {
            ConstraintViolationException constraintViolationException = (ConstraintViolationException) e.getCause();
            String constraintName = constraintViolationException.getConstraintName();
            if ("unique_username".equals(constraintName)){
                throw new UserAlreadyExistsException(String.format
                        ("User with username %s already exists", userDto.getUsername()), constraintViolationException);
            }

        }
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

}
