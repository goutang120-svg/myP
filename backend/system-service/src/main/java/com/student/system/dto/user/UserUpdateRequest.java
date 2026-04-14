package com.student.system.dto.user;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String username;
    private String password;
    private String role;
}
