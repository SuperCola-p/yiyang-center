package java.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class checkOutApplication implements Serializable {
    private String id;
    private Integer clientId;
    private String clientName;
    private String type;
    private String reason;
    private LocalDateTime time;
    private String statue = "已提交";
    private String auditor;
    private LocalDateTime auditTime;
    private Boolean deleted = false;

    public checkOutApplication() {
    }

    public checkOutApplication(Integer clientId, String clientName, LocalDateTime time,
                               String type, String reason, String statue) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.time = time;
        this.type = type;
        this.reason = reason;
        this.statue = statue;
        this.id = clientId + "_" + time + "_" + type;
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

    public String getStatue() {
        return statue;
    }

    public void setStatue(String statue) {
        this.statue = statue;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    @Override
    public String toString() {
        return "checkOutApplication{" +
                "id='" + id + '\'' +
                ", clientId=" + clientId +
                ", clientName='" + clientName + '\'' +
                ", type='" + type + '\'' +
                ", reason='" + reason + '\'' +
                ", time=" + time +
                ", statue='" + statue + '\'' +
                '}';
    }
}
