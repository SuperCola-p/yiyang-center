package java.entity;

import java.Nursing.NursingItemArray;
import java.util.Iterator;

public class NursingLevel implements Iterable<NursingItem> { // 实现接口
    private Long id;
    private String levelName;
    private String status;
    // 类型改为 NursingItemArray
    private NursingItemArray nursingItems = new NursingItemArray();

    public NursingItemArray getNursingItems() {
        return nursingItems;
    }
    public void setNursingItems(NursingItemArray nursingItems) {
        this.nursingItems = nursingItems;
    }

    @Override
    public Iterator<NursingItem> iterator() {
        return nursingItems.iterator();
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