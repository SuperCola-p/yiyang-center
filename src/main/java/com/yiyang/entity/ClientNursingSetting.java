package com.yiyang.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "client_nursing_setting")
public class ClientNursingSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "nursing_item_id")
    private Long nursingItemId;

    @Column(name = "nursing_level_id")
    private Long nursingLevelId;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "total_quantity")
    private Integer totalQuantity;

    @Column(name = "remaining_quantity")
    private Integer remainingQuantity;

    @Column(name = "service_due_date")
    private LocalDate serviceDueDate;

    @Column(name = "service_status", length = 20)
    private String serviceStatus;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}
