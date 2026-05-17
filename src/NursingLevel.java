// NursingLevel.java
import java.util.List;

public class NursingLevel {
    private Long id;
    // 护理级别名称
    private String levelName;
    // 状态: 启用/停用
    private String status;
    // 该级别下配置的护理项目列表
    private List<NursingItem> nursingItems;

    public List<NursingItem> getNursingItems() {
        return nursingItems;
    }

    public void setNursingItems(List<NursingItem> nursingItems) {
        this.nursingItems = nursingItems;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}