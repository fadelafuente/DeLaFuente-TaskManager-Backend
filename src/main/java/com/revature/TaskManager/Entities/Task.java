package com.revature.TaskManager.Entities;

import com.revature.TaskManager.Enums.Status;
import com.revature.TaskManager.Enums.Type;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(updatable = false)
    private Long creator_id;

    @Enumerated(EnumType.STRING)
    private Type type;

    private String description;

    @Column(name = "date_created")
    private ZonedDateTime dateCreated;

    @Column(name = "due_date")
    private ZonedDateTime dueDate;

    @Enumerated(EnumType.STRING)
    private Status status;
}