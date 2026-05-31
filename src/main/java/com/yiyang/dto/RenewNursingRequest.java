package com.yiyang.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class RenewNursingRequest {
    private Long id;
    private Integer quantity;
    private LocalDate serviceDueDate;
}
