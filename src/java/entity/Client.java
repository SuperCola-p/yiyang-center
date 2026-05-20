package java.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class Client implements Serializable {
    private Integer id;
    private String name;
    private Integer age;
    private String gender;
    private String bloodType;
    private String phone;
    private String familyContact;
    private String idCard;
    private String buildingNo;
    private String roomNo;
    private String bedNo;
    private LocalDate birthday;
    private LocalDate checkInDate;
    private LocalDate contractExpireDate;
    private String nursingLevel;
    private String nurse;
    private String healthStatus;
    private String type;
    private Boolean deleted = false;
    private final List<String> checkOutApplicationIds = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        if (birthday != null) {
            return Period.between(birthday, LocalDate.now()).getYears();
        }
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFamilyContact() {
        return familyContact;
    }

    public void setFamilyContact(String familyContact) {
        this.familyContact = familyContact;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getBuildingNo() {
        return buildingNo;
    }

    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getBedNo() {
        return bedNo;
    }

    public void setBedNo(String bedNo) {
        this.bedNo = bedNo;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
        if (birthday != null) {
            this.age = Period.between(birthday, LocalDate.now()).getYears();
        }
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getContractExpireDate() {
        return contractExpireDate;
    }

    public void setContractExpireDate(LocalDate contractExpireDate) {
        this.contractExpireDate = contractExpireDate;
    }

    public String getNursingLevel() {
        return nursingLevel;
    }

    public void setNursingLevel(String nursingLevel) {
        this.nursingLevel = nursingLevel;
    }

    public String getNurse() {
        return nurse;
    }

    public void setNurse(String nurse) {
        this.nurse = nurse;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public List<String> getCheckOutApplicationIds() {
        return checkOutApplicationIds;
    }

    public void addCheckOutApplication(checkOutApplication application) {
        if (application != null && application.getId() != null) {
            checkOutApplicationIds.add(application.getId());
        }
    }

    public boolean hasActiveBed() {
        return buildingNo != null && roomNo != null && bedNo != null;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + getAge() +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                ", buildingNo='" + buildingNo + '\'' +
                ", roomNo='" + roomNo + '\'' +
                ", bedNo='" + bedNo + '\'' +
                ", checkInDate=" + checkInDate +
                ", contractExpireDate=" + contractExpireDate +
                ", nursingLevel='" + nursingLevel + '\'' +
                ", nurse='" + nurse + '\'' +
                ", type='" + type + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
