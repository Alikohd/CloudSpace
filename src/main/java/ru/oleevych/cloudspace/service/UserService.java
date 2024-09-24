package ru.oleevych.cloudspace.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.oleevych.cloudspace.dto.UserRegisterDto;
import ru.oleevych.cloudspace.entity.User;
import ru.oleevych.cloudspace.exceptions.PasswordsMismatchException;
import ru.oleevych.cloudspace.exceptions.UserAlreadyExistsException;
import ru.oleevych.cloudspace.mapper.UserRegisterMapper;
import ru.oleevych.cloudspace.repository.UserRepository;
import ru.oleevych.cloudspace.security.UserDetailsImpl;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + "not found."));
    }
}
