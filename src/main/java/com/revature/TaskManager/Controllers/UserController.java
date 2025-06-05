package com.revature.TaskManager.Controllers;

import com.revature.TaskManager.Entities.User;
import com.revature.TaskManager.Exceptions.ExceptionResponse;
import com.revature.TaskManager.Exceptions.InvalidPasswordException;
import com.revature.TaskManager.Exceptions.UserAlreadyExistsException;
import com.revature.TaskManager.Exceptions.UserNotFoundException;
import com.revature.TaskManager.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/auth")
public class UserController {
    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    public UserController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody User body) {
        userService.register(body);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public void login(@RequestBody User body, HttpServletRequest request, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(
                body.getUsername(), body.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        securityContextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context, request, response);
    }

    @ExceptionHandler({ InvalidPasswordException.class, UserNotFoundException.class })
    public ResponseEntity<ExceptionResponse> handleBadRequests(Exception exception) {
        ExceptionResponse response = new ExceptionResponse(exception.getMessage(), new Date());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleConflicts(Exception exception) {
        ExceptionResponse response = new ExceptionResponse(exception.getMessage(), new Date());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
