package com.revature.TaskManager.TaskServiceTests;

import com.revature.TaskManager.Entities.Task;
import com.revature.TaskManager.Enums.Status;
import com.revature.TaskManager.Enums.Type;
import com.revature.TaskManager.Repositories.TaskRepository;
import com.revature.TaskManager.Services.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateTaskTests {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;

    private ZonedDateTime created;

    @BeforeEach
    public void beforeEach() {
        created = ZonedDateTime.now();
        task = new Task(1L, 1L, Type.TASK, "Buy Groceries", created, null, Status.TO_DO);
    }

    @Test
    public void testCreateTask() {
        Task newTask = new Task(1L, Type.TASK, "Buy Groceries", Status.TO_DO);
        when(taskRepository.save(newTask)).thenReturn(task);
        Task createdTask = taskService.createTask(newTask);

        Assertions.assertEquals(1L, createdTask.getId());
    }

    @Test
    public void testCreateTaskWithDate() {
        Task newTask = new Task(1L, Type.TASK, "Buy Groceries", created, Status.TO_DO);
        when(taskRepository.save(newTask)).thenReturn(task);
        Task createdTask = taskService.createTask(newTask);

        Assertions.assertEquals(1L, createdTask.getId());
        Assertions.assertEquals(created, createdTask.getDateCreated());
    }
}