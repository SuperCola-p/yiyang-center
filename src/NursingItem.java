// NursingItem.java
import java.math.BigDecimal;

public class NursingItem {
    private Long id;
    // 编号
    private String code;
    // 名称
    private String name;
    // 价格
    private BigDecimal price;
    // 状态: 启用/停用
    private String status;
    // 执行周期 (如：每日、每周)
    private String execPeriod;
    // 执行次数 (周期内次数)
    private Integer execTimes;
    // 描述
    private String description;
    // 逻辑删除标记
    private Boolean isDeleted = false;

    // 构造方法、Getter和Setter省略...
}