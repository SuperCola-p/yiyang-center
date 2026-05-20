package java.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class NursingRecord implements Serializable {
    private Long id;
    private Long clientId;
    private Long nursingItemId;
    private Long healthAssistantId;
    private LocalDateTime nursingTime;
    private String remarks;
    private Integer execQuantity;
    private Boolean isDeleted = false;

    public NursingRecord() {
    }

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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
