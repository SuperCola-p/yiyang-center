package java.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class LeaveApplication implements Serializable {
    private String id;
    private Integer clientId;
    private String clientName;
    private String reason;
    private LocalDateTime leaveTime;
    private LocalDateTime predictedEnd;
    private LocalDateTime actualReturnTime;
    private String statue = "已提交";
    private String auditor;
    private LocalDateTime auditTime;
    private Boolean deleted = false;

    public LeaveApplication() {
    }

    public LeaveApplication(Integer clientId, String clientName, String reason,
                            LocalDateTime leaveTime, LocalDateTime predictedEnd) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.reason = reason;
        this.leaveTime = leaveTime;
        this.predictedEnd = predictedEnd;
        this.id = clientId + "_" + leaveTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getTime() {
        return leaveTime;
    }

    public void setTime(LocalDateTime leaveTime) {
        this.leaveTime = leaveTime;
    }

    public LocalDateTime getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(LocalDateTime leaveTime) {
        this.leaveTime = leaveTime;
    }

    public LocalDateTime getPredictedEnd() {
        return predictedEnd;
    }

    public void setPredictedEnd(LocalDateTime predictedEnd) {
        this.predictedEnd = predictedEnd;
    }

    public LocalDateTime getActualReturnTime() {
        return actualReturnTime;
    }

    public void setActualReturnTime(LocalDateTime actualReturnTime) {
        this.actualReturnTime = actualReturnTime;
    }

    public String getStatue() {
        return statue;
    }

    public void setStatue(String statue) {
        this.statue = statue;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public LocalDateTime getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(LocalDateTime auditTime) {
        this.auditTime = auditTime;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
