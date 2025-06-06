package com.revature.TaskManager.Controllers;

import com.revature.TaskManager.Entities.Task;
import com.revature.TaskManager.Entities.TaskManagerUserDetails;
import com.revature.TaskManager.Entities.UsersTask;
import com.revature.TaskManager.Exceptions.UnauthorizedException;
import com.revature.TaskManager.Services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/tasks")
    public ResponseEntity<Void> createTask(@RequestBody Task task) {
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
    public ResponseEntity<List<UsersTask>> getTasks() {
        List<UsersTask> tasks = taskService.getTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<UsersTask> getTaskById(@PathVariable Long id) {
        UsersTask task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @PostMapping("/tasks/{id}")
    public ResponseEntity<Void> updateTaskById(@PathVariable Long id, @RequestBody Task task) {
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
}