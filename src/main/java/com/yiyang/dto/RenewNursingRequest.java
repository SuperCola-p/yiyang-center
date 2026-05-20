package com.yiyang.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class RenewNursingRequest {
    private Integer additionalQuantity;
    private LocalDate newDueDate;
}
