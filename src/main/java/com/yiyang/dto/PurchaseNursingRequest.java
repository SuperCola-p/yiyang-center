package com.yiyang.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PurchaseNursingRequest {
    private Long clientId;
    private Long nursingItemId;
    private Long nursingLevelId;
    private Integer totalQuantity;
    private LocalDate serviceDueDate;
}
