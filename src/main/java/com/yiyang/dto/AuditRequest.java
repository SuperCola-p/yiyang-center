package com.yiyang.dto;

import lombok.Data;

@Data
public class AuditRequest {
    private String status;
    private String auditor;
}
