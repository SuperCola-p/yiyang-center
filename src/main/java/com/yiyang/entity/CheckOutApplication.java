package com.yiyang.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "check_out_application")
public class CheckOutApplication {
    @Id
    @Column(length = 50)
    private String id;

    @Column(name = "client_id")
    private Integer clientId;

    @Column(name = "client_name", length = 50)
    private String clientName;

    @Column(length = 20)
    private String type;

    @Column(length = 255)
    private String reason;

    private LocalDateTime time;

    @Column(length = 20)
    private String status = "已提交";

    @Column(length = 50)
    private String auditor;

    @Column(name = "audit_time")
    private LocalDateTime auditTime;

    private Boolean deleted = false;
}
