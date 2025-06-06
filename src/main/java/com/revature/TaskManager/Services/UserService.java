package com.revature.TaskManager.Services;

import com.revature.TaskManager.Entities.User;
import com.revature.TaskManager.Exceptions.UserAlreadyExistsException;
import com.revature.TaskManager.Repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(User newUser) {
        Optional<User> user = userRepository.findUserByUsername(newUser.getUsername());
        if(user.isPresent()) {
            throw new UserAlreadyExistsException("Username is already taken: " + newUser.getUsername());
        }

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return userRepository.save(newUser);
    }
}