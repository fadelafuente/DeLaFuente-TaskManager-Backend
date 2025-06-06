package com.revature.TaskManager.TaskServiceTests;

import com.revature.TaskManager.Entities.UsersTask;
import com.revature.TaskManager.Enums.Status;
import com.revature.TaskManager.Enums.Type;
import com.revature.TaskManager.Repositories.UsersTaskRepository;
import com.revature.TaskManager.Services.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListTasksTests {
    @Mock
    private UsersTaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private UsersTask task;

    @BeforeEach
    public void beforeEach() {
        task = new UsersTask(1L, Type.TASK, "Buy Groceries", ZonedDateTime.now(), Status.TO_DO);
    }

    @Test
    public void testListTask() {
        when(taskRepository.findAll()).thenReturn(List.of(task));
        List<UsersTask> taskList = taskService.getTasks();

        Assertions.assertEquals(1, taskList.size());
    }
}