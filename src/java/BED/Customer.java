package java.BED;

import java.entity.Bed;
import java.entity.admin;
import java.util.ArrayList;
import java.util.Date;

public class Customer {
    /**
     * 客户姓名
     */
    private String name;

    /**
     * 当前床位
     */
    private Bed bed;

    /**
     * 床位使用详情列表（一个客户可以有多个床位使用记录）
     */
    private ArrayList<admin.BedDetails> bedDetailsList;

    public Customer() {
        bed = new Bed();
        bedDetailsList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bed getBed() {
        return bed;
    }

    public void setBed(Bed bed) {
        this.bed = bed;
    }

    public ArrayList<admin.BedDetails> getBedDetailsList() {
        return bedDetailsList;
    }

    public void setBedDetailsList(ArrayList<admin.BedDetails> bedDetailsList) {
        this.bedDetailsList = bedDetailsList;
    }

    /**
     * 获取当前正在使用的床位详情
     */
    public admin.BedDetails getCurrentBedDetails() {
        Date now = new Date();
        for (admin.BedDetails details : bedDetailsList) {
            if (details.getEndDate() == null || details.getEndDate().after(now)) {
                return details;
            }
        }
        return null;
    }

    /**
     * 添加床位使用详情
     */
    public void addBedDetails(admin.BedDetails bedDetails) {
        this.bedDetailsList.add(bedDetails);
    }
}