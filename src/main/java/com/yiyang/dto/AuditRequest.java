package com.yiyang.dto;

import lombok.Data;

@Data
public class AuditRequest {
    private String status;
    private String auditor;
    /** 前端传的布尔值：true=同意/通过，false=拒绝 */
    private Boolean approve;
}
