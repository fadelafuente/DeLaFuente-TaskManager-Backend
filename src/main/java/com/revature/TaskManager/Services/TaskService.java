package com.revature.TaskManager.Services;

import com.revature.TaskManager.Entities.Task;
import com.revature.TaskManager.Entities.UsersTask;
import com.revature.TaskManager.Exceptions.TaskNotFoundException;
import com.revature.TaskManager.Repositories.TaskRepository;
import com.revature.TaskManager.Repositories.UsersTaskRepository;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    private final UsersTaskRepository usersTaskRepository;

    public TaskService(TaskRepository taskRepository, UsersTaskRepository usersTaskRepository) {
        this.taskRepository = taskRepository;
        this.usersTaskRepository = usersTaskRepository;
    }

    public void createTask(Task task) {
        if(task.getDateCreated() == null) {
            task.setDateCreated(ZonedDateTime.now());
        }

        taskRepository.save(task);
    }

    public List<UsersTask> getTasks() {
        return usersTaskRepository.findAll();
    }

    public UsersTask getTaskById(Long id) {
        return usersTaskRepository.findById(id).orElseThrow(() ->
                new TaskNotFoundException("Task with id not found: " + id));
    }

    public void updateTaskById(Task task) {
        taskRepository.findById(task.getId()).orElseThrow(() ->
                new TaskNotFoundException("Task with id not found: " + task.getId()));

        taskRepository.save(task);
    }

    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }
}