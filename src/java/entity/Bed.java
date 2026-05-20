package java.entity;
import java.io.Serializable;

/**
 * 床位表 (bed)
 * 存储房间床位信息
 */
public class Bed implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 楼号
     */
    private String building;

    /**
     * 房间编号
     */
    private Integer roomNo;

    /**
     * 房间状态 1：空闲 2：有人 3：外出
     */
    private Integer bedStatus;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 床位编号
     */
    private String bedNo;

    // -------------------- Getter & Setter --------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public Integer getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(Integer roomNo) {
        this.roomNo = roomNo;
    }

    public Integer getBedStatus() {
        return bedStatus;
    }

    public void setBedStatus(Integer bedStatus) {
        this.bedStatus = bedStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getBedNo() {
        return bedNo;
    }

    public void setBedNo(String bedNo) {
        this.bedNo = bedNo;
    }

    @Override
    public String toString() {
        return "Bed{" +
                "id=" + id +
                ", building='" + building + '\'' +
                ", roomNo=" + roomNo +
                ", bedStatus=" + bedStatus +
                ", remarks='" + remarks + '\'' +
                ", bedNo='" + bedNo + '\'' +
                '}';
    }
}