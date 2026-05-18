import java.time.LocalDate;

public class Customer {
    private String name;
    //用户姓名
    private String id;
    // 客户ID
    private String clientId;
    // 护理项目ID (具体购买的服务)
    private String nursingItemId;
    // 护理级别ID (如果通过级别批量设置，可关联)
    private String nursingLevelId;
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
    public Customer(String id,String clientId,String name,String nursingItemId,String nursingLevelId,
                    LocalDate purchaseDate,LocalDate serviceDueDate,
                    Integer remainingQuantity,Integer totalQuantity,String serviceStatus){
        this.id=id;
        this.name=name;
        this.clientId=clientId;
        this.nursingLevelId=nursingLevelId;
        this.nursingItemId=nursingItemId;
        this.purchaseDate=purchaseDate;
        this.serviceDueDate=serviceDueDate;
        this.remainingQuantity=remainingQuantity;
        this.totalQuantity=totalQuantity;
        this.serviceStatus=serviceStatus;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getNursingItemId() {
        return nursingItemId;
    }

    public void setNursingItemId(String nursingItemId) {
        this.nursingItemId = nursingItemId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getNursingLevelId() {
        return nursingLevelId;
    }

    public void setNursingLevelId(String nursingLevelId) {
        this.nursingLevelId = nursingLevelId;
    }

    public String toString() {
        return getName()+"_"+getId()+"_"+getClientId();
    }
}