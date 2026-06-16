package com.tfb.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String name;
    private String password;
    private String role;
}
