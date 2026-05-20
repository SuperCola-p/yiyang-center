package com.yiyang.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String loginCode;
    private String password;
}
