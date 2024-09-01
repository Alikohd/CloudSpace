package ru.oleevych.cloudspace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.oleevych.cloudspace.entity.User;
import ru.oleevych.cloudspace.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    public final UserRepository userRepository;
    public final PasswordEncoder encoder;
    public void addUser(User user) {
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

}
