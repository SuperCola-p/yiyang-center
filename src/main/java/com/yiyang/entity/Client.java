package com.yiyang.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50)
    private String name;

    private Integer age;

    @Column(length = 10)
    private String gender;

    @Column(name = "blood_type", length = 10)
    private String bloodType;

    @Column(length = 20)
    private String phone;

    @Column(name = "family_contact", length = 100)
    private String familyContact;

    @Column(name = "id_card", length = 18)
    private String idCard;

    @Column(name = "building_no", length = 10)
    private String buildingNo;

    @Column(name = "room_no", length = 10)
    private String roomNo;

    @Column(name = "bed_no", length = 10)
    private String bedNo;

    private LocalDate birthday;

    @Column(name = "check_in_date")
    private LocalDate checkInDate;

    @Column(name = "contract_expire_date")
    private LocalDate contractExpireDate;

    @Column(name = "nursing_level", length = 50)
    private String nursingLevel;

    @Column(length = 50)
    private String nurse;

    @Column(name = "health_status", length = 100)
    private String healthStatus;

    @Column(length = 20)
    private String type;

    @Column(nullable = false)
    private Boolean deleted = false;
}
