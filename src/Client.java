// Client.java
public class Client {
    private Long id;
    private String name;
    // 老人类型: 自理老人、护理老人
    private String elderType;
    // 当前护理级别ID
    private Long currentNursingLevelId;
    // 关联的健康管家ID
    private Long healthAssistantId;
    // 逻辑删除标记
    private Boolean isDeleted = false;

}