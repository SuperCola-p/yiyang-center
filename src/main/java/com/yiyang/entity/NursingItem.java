package com.yiyang.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "nursing_item")
public class NursingItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String code;

    @Column(length = 100)
    private String name;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(length = 20)
    private String status;

    @Column(name = "exec_period", length = 20)
    private String execPeriod;

    @Column(name = "exec_quantity")
    private Integer execQuantity;

    @Column(length = 500)
    private String description;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}
