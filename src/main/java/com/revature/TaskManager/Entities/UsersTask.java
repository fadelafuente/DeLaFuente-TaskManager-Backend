package com.revature.TaskManager.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.revature.TaskManager.Enums.Status;
import com.revature.TaskManager.Enums.Type;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "task")
public class UsersTask {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_generator")
    @SequenceGenerator(name = "task_generator", sequenceName = "task_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties({"password", "role"})
    private User creator;

    @Enumerated(EnumType.STRING)
    private Type type;

    private String description;

    @Column(name = "date_created")
    private ZonedDateTime dateCreated;

    @Column(name = "due_date")
    private ZonedDateTime dueDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    public UsersTask(Long id, Type type, String description, ZonedDateTime dateCreated, Status status) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.dateCreated = dateCreated;
        this.status = status;
    }
}