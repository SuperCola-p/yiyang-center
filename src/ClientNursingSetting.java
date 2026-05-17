// ClientNursingSetting.java
import java.time.LocalDate;

public class ClientNursingSetting {
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
    // 服务状态: 正常、欠费、到期、未到期 (参考`5.1.8 服务关注`)
    private String serviceStatus;
    // 逻辑删除标记
    private Boolean isDeleted = false;

}