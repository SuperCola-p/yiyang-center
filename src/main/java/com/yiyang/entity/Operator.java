package com.yiyang.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "operator")
public class Operator {
    @Id
    @Column(name = "login_code", length = 50)
    private String loginCode;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(name = "real_name", length = 50, nullable = false)
    private String realName;

    @Column(name = "operator_type", length = 20, nullable = false)
    private String operatorType = "ADMIN";

    @Column(nullable = false)
    private Boolean deleted = false;
}
