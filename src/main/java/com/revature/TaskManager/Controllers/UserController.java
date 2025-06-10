package com.revature.TaskManager.Controllers;

import com.revature.TaskManager.DTOs.AuthUserDTO;
import com.revature.TaskManager.Entities.TaskManagerUserDetails;
import com.revature.TaskManager.Entities.User;
import com.revature.TaskManager.Exceptions.ExceptionResponse;
import com.revature.TaskManager.Exceptions.InvalidPasswordException;
import com.revature.TaskManager.Exceptions.UserAlreadyExistsException;
import com.revature.TaskManager.Exceptions.UserNotFoundException;
import com.revature.TaskManager.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Objects;

@RestController
@RequestMapping("/auth")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class UserController {
    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    public UserController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody User body) {
        userService.register(body);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public void login(@Valid @RequestBody User body, HttpServletRequest request, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(
                body.getUsername(), body.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        securityContextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context, request, response);
    }

    @GetMapping("/session")
    public ResponseEntity<AuthUserDTO> auth(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isLoggedIn = authentication != null && !("anonymousUser").equals(authentication.getName());

        if(!isLoggedIn) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        TaskManagerUserDetails userDetails = (TaskManagerUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        String role = userDetails.getRole();
        Long id = userDetails.getId();
        AuthUserDTO userDTO = new AuthUserDTO(id, username, role);

        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        this.logoutHandler.logout(request, response, authentication);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ExceptionHandler({ InvalidPasswordException.class, UserNotFoundException.class })
    public ResponseEntity<ExceptionResponse> handleBadRequests(Exception exception) {
        ExceptionResponse response = new ExceptionResponse(exception.getMessage(), new Date());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidation(MethodArgumentNotValidException exception) {
        ExceptionResponse response = new ExceptionResponse(Objects.requireNonNull(exception.getFieldError()).getDefaultMessage(), new Date());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleConflicts(Exception exception) {
        ExceptionResponse response = new ExceptionResponse(exception.getMessage(), new Date());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}