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

    /**
     * 护理员登录码（对应 operator.login_code），替代原来的数字 health_assistant_id
     * ddl-auto=update 会自动将列类型从 bigint 更新为 varchar
     */
    @Column(name = "health_assistant_id", length = 50)
    private String healthAssistantId;

    @Column(name = "nursing_time")
    private LocalDateTime nursingTime;

    @Column(length = 500)
    private String remarks;

    @Column(name = "exec_quantity")
    private Integer execQuantity = 1;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    // ========== 非数据库字段，关联查询时填充 ==========

    @Transient
    private String clientName;

    @Transient
    private String nursingItemName;

    @Transient
    private String healthAssistantName;
}
