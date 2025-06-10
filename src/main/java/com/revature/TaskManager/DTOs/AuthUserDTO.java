package com.revature.TaskManager.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthUserDTO {
    private Long id;
    private String username;
    private String role;
}
