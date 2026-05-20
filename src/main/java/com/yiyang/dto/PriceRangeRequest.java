package com.yiyang.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PriceRangeRequest {
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
