package com.yiyang.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bed")
public class Bed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10)
    private String building;

    @Column(name = "room_no")
    private Integer roomNo;

    @Column(name = "bed_no", length = 10)
    private String bedNo;

    @Column(name = "bed_status")
    private Integer bedStatus = 1;

    @Column(length = 255)
    private String remarks;
}
