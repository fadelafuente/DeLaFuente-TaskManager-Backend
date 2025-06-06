package com.revature.TaskManager.Controllers;

import com.revature.TaskManager.Entities.Task;
import com.revature.TaskManager.Entities.TaskManagerUserDetails;
import com.revature.TaskManager.Entities.UsersTask;
import com.revature.TaskManager.Exceptions.ExceptionResponse;
import com.revature.TaskManager.Exceptions.TaskNotFoundException;
import com.revature.TaskManager.Exceptions.UnauthorizedException;
import com.revature.TaskManager.Services.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Objects;

@RestController
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/tasks")
    public ResponseEntity<Void> createTask(@Valid @RequestBody Task task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if(principal instanceof UserDetails) {
                TaskManagerUserDetails userDetails = (TaskManagerUserDetails) principal;
                task.setCreator_id(userDetails.getId());
            } else {
                throw new UnauthorizedException();
            }
        } else {
            throw new UnauthorizedException();
        }

        taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/tasks")
    public ResponseEntity<Page<UsersTask>> getTasks(Pageable page) {
        Page<UsersTask> tasks = taskService.getTasks(page);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<UsersTask> getTaskById(@PathVariable Long id) {
        UsersTask task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @PostMapping("/tasks/{id}")
    public ResponseEntity<Void> updateTaskById(@PathVariable Long id, @Valid @RequestBody Task task) {
        task.setId(id);
        taskService.updateTaskById(task);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTaskById(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Void> handleUnauthorized() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequest(Exception exception) {
        ExceptionResponse response = new ExceptionResponse(exception.getMessage(), new Date());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidation(MethodArgumentNotValidException exception) {
        ExceptionResponse response = new ExceptionResponse(Objects.requireNonNull(exception.getFieldError()).getDefaultMessage(), new Date());
        return ResponseEntity.badRequest().body(response);
    }
}