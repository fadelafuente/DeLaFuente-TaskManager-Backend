package com.revature.TaskManager.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @SequenceGenerator(name = "user_generator", sequenceName = "user_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @Pattern(regexp = "[a-zA-Z0-9]{5,}", message = "Please enter a valid username.")
    private String username;

    @NotNull
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&.])[A-Za-z0-9!@#$%^&.]{8,}$",
            message = "Please enter a valid password.")
    private String password;

    private String role;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = "USER";
    }
}