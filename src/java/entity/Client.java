package java.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class Client implements Serializable {
    private Integer id;                 // 序号（主键ID）
    private String name;                // 客户姓名
    private String gender;              // 性别
    private String bloodType;           // 血型
    private String phone;               // 联系电话
    private String familyContact;      // 家属联系人
    private String idCard;              // 身份证号
    private String buildingNo;          // 楼号
    private String roomNo;              // 房间号
    private String bedNo;               // 床号
    private String birthday;              // 出生日期
    private String checkInDate;           // 入住时间
    private String nursingLevel;        // 护理级别
    private String nurse;               // 护工
    private String healthStatus;        // 身心状况
    private String type;                // 老人类型：自理老人/护理老人
    private ArrayList<checkOutApplication> checkOutApplications=new ArrayList<>();

    // 无参构造
    public Client() {}

    // 全参构造
    public Client(Integer id, String name, String gender, String bloodType, String phone,
                  String familyContact, String idCard, String buildingNo, String roomNo,
                  String bedNo, String birthday, String checkInDate, String nursingLevel,
                  String nurse, String healthStatus, String type) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.bloodType = bloodType;
        this.phone = phone;
        this.familyContact = familyContact;
        this.idCard = idCard;
        this.buildingNo = buildingNo;
        this.roomNo = roomNo;
        this.bedNo = bedNo;
        this.birthday = birthday;
        this.checkInDate = checkInDate;
        this.nursingLevel = nursingLevel;
        this.nurse = nurse;
        this.healthStatus = healthStatus;
        this.type = type;
    }

    // Getter/Setter 方法
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getBloodType() { return bloodType; }
    public void setBloodType(String bloodType) { this.bloodType = bloodType; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getFamilyContact() { return familyContact; }
    public void setFamilyContact(String familyContact) { this.familyContact = familyContact; }

    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }

    public String getBuildingNo() { return buildingNo; }
    public void setBuildingNo(String buildingNo) { this.buildingNo = buildingNo; }

    public String getRoomNo() { return roomNo; }
    public void setRoomNo(String roomNo) { this.roomNo = roomNo; }

    public String getBedNo() { return bedNo; }
    public void setBedNo(String bedNo) { this.bedNo = bedNo; }

    public String getBirthday() { return birthday; }
    public void setBirthday(String birthday) { this.birthday = birthday; }

    public String getCheckInDate() { return checkInDate; }
    public void setCheckInDate(String checkInDate) { this.checkInDate = checkInDate; }

    public String getNursingLevel() { return nursingLevel; }
    public void setNursingLevel(String nursingLevel) { this.nursingLevel = nursingLevel; }

    public String getNurse() { return nurse; }
    public void setNurse(String nurse) { this.nurse = nurse; }

    public String getHealthStatus() { return healthStatus; }
    public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public void addCheckOutApplication(checkOutApplication checkOutApplication){
        checkOutApplications.add(checkOutApplication);
    }
    public void showAddCheckOutApplication(){
        for(int i=0;i<checkOutApplications.size();i++){
            System.out.println(checkOutApplications.get(i).toString());
        }
    }

    // 重写toString（方便打印和调试）
    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", bloodType='" + bloodType + '\'' +
                ", phone='" + phone + '\'' +
                ", familyContact='" + familyContact + '\'' +
                ", idCard='" + idCard + '\'' +
                ", buildingNo='" + buildingNo + '\'' +
                ", roomNo='" + roomNo + '\'' +
                ", bedNo='" + bedNo + '\'' +
                ", birthday=" + birthday +
                ", checkInDate=" + checkInDate +
                ", nursingLevel='" + nursingLevel + '\'' +
                ", nurse='" + nurse + '\'' +
                ", healthStatus='" + healthStatus + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
