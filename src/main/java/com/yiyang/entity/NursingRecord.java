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
@Table(name = "nursing_record")
public class NursingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "nursing_item_id")
    private Long nursingItemId;

    @Column(name = "health_assistant_id")
    private Long healthAssistantId;

    @Column(name = "nursing_time")
    private LocalDateTime nursingTime;

    @Column(length = 500)
    private String remarks;

    @Column(name = "exec_quantity")
    private Integer execQuantity = 1;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}
