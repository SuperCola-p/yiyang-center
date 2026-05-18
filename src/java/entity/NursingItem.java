package java.entity;// java.entity.NursingItem.java
import java.io.Serializable;
import java.math.BigDecimal;

public class NursingItem implements Serializable {
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


    public Boolean getDeleted() {
        return isDeleted;
    }

    @Override
    public String toString() {
        return "java.entity.NursingItem{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", status='" + status + '\'' +
                ", execPeriod='" + execPeriod + '\'' +
                ", execTimes=" + execTimes +
                ", description='" + description + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getExecTimes() {
        return execTimes;
    }

    public void setExecTimes(Integer execTimes) {
        this.execTimes = execTimes;
    }

    public String getExecPeriod() {
        return execPeriod;
    }

    public void setExecPeriod(String execPeriod) {
        this.execPeriod = execPeriod;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    // 构造方法

}