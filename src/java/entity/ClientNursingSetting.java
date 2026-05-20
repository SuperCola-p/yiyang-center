package java.entity;// java.entity.ClientNursingSetting.java
import java.io.Serializable;
import java.time.LocalDate;

public class ClientNursingSetting implements Serializable {
    private Long id;
    // 客户ID
    private Long clientId;
    // 护理项目ID (具体购买的服务)
    private Long nursingItemId;
    // 护理级别ID (如果通过级别批量设置，可关联)
    private Long nursingLevelId;
    // 购买服务日期
    private LocalDate purchaseDate;
    // 总购买数量
    private Integer totalQuantity;
    // 剩余数量 (随护理执行而减少)
    private Integer remainingQuantity;
    // 服务到期日期
    private LocalDate serviceDueDate;
    // 服务状态: 正常、欠费、到期、未到期
    private String serviceStatus;
    // 逻辑删除标记
    private Boolean isDeleted = false;

    public ClientNursingSetting() {
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

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public LocalDate getServiceDueDate() {
        return serviceDueDate;
    }

    public void setServiceDueDate(LocalDate serviceDueDate) {
        this.serviceDueDate = serviceDueDate;
    }

    public Integer getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(Integer remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Long getNursingLevelId() {
        return nursingLevelId;
    }

    public void setNursingLevelId(Long nursingLevelId) {
        this.nursingLevelId = nursingLevelId;
    }


}