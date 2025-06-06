package com.revature.TaskManager.TaskServiceTests;

import com.revature.TaskManager.Entities.Task;
import com.revature.TaskManager.Enums.Status;
import com.revature.TaskManager.Enums.Type;
import com.revature.TaskManager.Exceptions.TaskNotFoundException;
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
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateTaskTests {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;

    @BeforeEach
    public void beforeEach() {
        task = new Task(1L, 1L, Type.TASK, "Buy Different Groceries", ZonedDateTime.now(), null, Status.TO_DO);
    }

    @Test
    public void testUpdateTaskSuccess() {
        Task updateTask = new Task(1L, 1L, Type.TASK, "Buy Different Groceries", Status.TO_DO);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(updateTask)).thenReturn(task);
        Task updatedTask = taskService.updateTaskById(updateTask);

        Assertions.assertEquals(1L, updatedTask.getId());
        Assertions.assertEquals("Buy Different Groceries", updatedTask.getDescription());
    }

    @Test
    public void testUpdateTaskTaskNotFound() {
        Task updateTask = new Task(1L, 1L, Type.TASK, "Buy Different Groceries", Status.TO_DO);
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.updateTaskById(updateTask));
    }
}