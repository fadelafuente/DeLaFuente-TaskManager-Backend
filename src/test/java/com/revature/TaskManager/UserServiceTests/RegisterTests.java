package com.revature.TaskManager.UserServiceTests;

import com.revature.TaskManager.Entities.User;
import com.revature.TaskManager.Exceptions.InvalidPasswordException;
import com.revature.TaskManager.Exceptions.UserAlreadyExistsException;
import com.revature.TaskManager.Repositories.UserRepository;
import com.revature.TaskManager.Services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegisterTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void beforeEach() {
        user = new User(1L, "username", "encodedPassword", "USER");
    }

    @Test
    public void testRegisterSuccess() {
        User newUser = new User("username", "P@ssW0rd!");
        when(userRepository.findUserByUsername("username")).thenReturn(Optional.empty());
        when(userRepository.save(newUser)).thenReturn(user);
        when(passwordEncoder.encode("P@ssW0rd!")).thenReturn("encodedPassword");

        User savedUser = userService.register(newUser);
        Assertions.assertEquals(1L, savedUser.getId());
        Assertions.assertEquals("encodedPassword", savedUser.getPassword());
    }

    @Test
    public void testRegisterUsernameAlreadyTaken() {
        User newUser = new User("username", "P@ssW0rd!");
        when(userRepository.findUserByUsername("username")).thenReturn(Optional.of(user));

        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.register(newUser));
    }

    @Test
    public void testRegisterInvalidPassword() {
        User newUser = new User("username", "P@ssWord!");

        Assertions.assertThrows(InvalidPasswordException.class, () -> userService.register(newUser));
    }
}
