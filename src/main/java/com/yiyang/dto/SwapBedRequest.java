package com.yiyang.dto;

import lombok.Data;

@Data
public class SwapBedRequest {
    private String building;
    private Integer roomNo;
    private String oldBedNo;
    private String newBedNo;
    private Integer clientId;
}
