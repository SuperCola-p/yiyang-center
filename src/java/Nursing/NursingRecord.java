package java.Nursing;// java.Nursing.NursingRecord.java
import java.time.LocalDateTime;

public class NursingRecord {
    private NursingRecord(){};
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

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Integer getExecQuantity() {
        return execQuantity;
    }

    public void setExecQuantity(Integer execQuantity) {
        this.execQuantity = execQuantity;
    }

    public LocalDateTime getNursingTime() {
        return nursingTime;
    }

    public void setNursingTime(LocalDateTime nursingTime) {
        this.nursingTime = nursingTime;
    }

    public Long getHealthAssistantId() {
        return healthAssistantId;
    }

    public void setHealthAssistantId(Long healthAssistantId) {
        this.healthAssistantId = healthAssistantId;
    }

    public Long getNursingItemId() {
        return nursingItemId;
    }

    public void setNursingItemId(Long nursingItemId) {
        this.nursingItemId = nursingItemId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}