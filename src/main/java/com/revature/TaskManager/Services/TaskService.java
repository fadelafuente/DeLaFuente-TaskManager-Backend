package com.revature.TaskManager.Services;

import com.revature.TaskManager.Entities.Task;
import com.revature.TaskManager.Entities.UsersTask;
import com.revature.TaskManager.Exceptions.TaskNotFoundException;
import com.revature.TaskManager.Repositories.TaskRepository;
import com.revature.TaskManager.Repositories.UsersTaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    private final UsersTaskRepository usersTaskRepository;

    public TaskService(TaskRepository taskRepository, UsersTaskRepository usersTaskRepository) {
        this.taskRepository = taskRepository;
        this.usersTaskRepository = usersTaskRepository;
    }

    public Task createTask(Task task) {
        if(task.getDateCreated() == null) {
            task.setDateCreated(ZonedDateTime.now());
        }

        return taskRepository.save(task);
    }

    public Page<UsersTask> getTasks(Pageable page) {
        return usersTaskRepository.findAll(page);
    }

    public UsersTask getTaskById(Long id) {
        return usersTaskRepository.findById(id).orElseThrow(() ->
                new TaskNotFoundException("Task with id not found: " + id));
    }

    public Task updateTaskById(Task task) {
        taskRepository.findById(task.getId()).orElseThrow(() ->
                new TaskNotFoundException("Task with id not found: " + task.getId()));

        return taskRepository.save(task);
    }

    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }
}