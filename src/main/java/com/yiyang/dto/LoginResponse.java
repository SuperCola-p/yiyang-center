package com.yiyang.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String loginCode;
    private String realName;
    private String operatorType;
    private String token;
}
