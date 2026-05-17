// NursingRecord.java
import java.time.LocalDateTime;

public class NursingRecord {
    private Long id;
    // 客户ID
    private Long clientId;
    // 护理项目ID
    private Long nursingItemId;
    // 执行护理的健康管家(护工)ID
    private Long healthAssistantId;
    // 护理执行时间
    private LocalDateTime nursingTime;
    // 本次护理消耗的数量
    private Integer execQuantity;
    // 逻辑删除标记
    private Boolean isDeleted = false;

    // 构造方法、Getter和Setter省略...
}