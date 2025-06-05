package com.revature.TaskManager.Services;

import com.revature.TaskManager.Entities.User;
import com.revature.TaskManager.Exceptions.InvalidPasswordException;
import com.revature.TaskManager.Exceptions.UserAlreadyExistsException;
import com.revature.TaskManager.Repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {
    public static final Pattern VALID_PASSWORD_REGEX = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&.])[A-Za-z0-9!@#$%^&.]{8,}$");

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(User newUser) {
        validatePassword(newUser.getPassword());

        Optional<User> user = userRepository.findUserByUsername(newUser.getUsername());
        if(user.isPresent()) {
            throw new UserAlreadyExistsException("Username is already taken: " + newUser.getUsername());
        }

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userRepository.save(newUser);
    }

    public void validatePassword(String password) {
        Matcher matcher = VALID_PASSWORD_REGEX.matcher(password);
        if(!matcher.matches()) {
            throw new InvalidPasswordException("Please enter a valid password.");
        }
    }
}
