package com.yiyang.dto;

import lombok.Data;
import java.util.List;

@Data
public class NursingLevelItemRequest {
    private List<Long> itemIds;
}
