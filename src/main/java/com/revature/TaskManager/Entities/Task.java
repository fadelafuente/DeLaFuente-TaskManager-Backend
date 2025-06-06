package com.revature.TaskManager.Entities;

import com.revature.TaskManager.Enums.Status;
import com.revature.TaskManager.Enums.Type;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_generator")
    @SequenceGenerator(name = "task_generator", sequenceName = "task_seq", allocationSize = 1)
    private Long id;

    @Column(updatable = false)
    private Long creator_id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Type is required.")
    private Type type;

    @NotNull(message = "Description is required.")
    private String description;

    @Column(name = "date_created", updatable = false)
    private ZonedDateTime dateCreated;

    @Column(name = "due_date")
    @NotNull(message = "Due date is required.")
    @Future(message = "Due date must be greater than the current date.")
    private ZonedDateTime dueDate;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status is required.")
    private Status status;

    public Task(Long creator_id, Type type, String description, ZonedDateTime dateCreated, Status status) {
        this.creator_id = creator_id;
        this.type = type;
        this.description = description;
        this.dateCreated = dateCreated;
        this.status = status;
    }

    public Task(Long creator_id, Type type, String description, Status status) {
        this.creator_id = creator_id;
        this.type = type;
        this.description = description;
        this.status = status;
    }

    public Task(Long id, Long creator_id, Type type, String description, Status status) {
        this.id = id;
        this.creator_id = creator_id;
        this.type = type;
        this.description = description;
        this.status = status;
    }
}