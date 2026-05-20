package com.yiyang.service;

import com.yiyang.entity.Bed;
import com.yiyang.entity.BedDetails;

import java.util.List;
import java.util.Optional;

public interface BedService {

    // ==================== 床位 CRUD ====================

    Bed addBed(Bed bed);

    Bed updateBed(Long id, Bed bed);

    boolean deleteBed(Long id);

    Optional<Bed> getBedById(Long id);

    List<Bed> getAllBeds();

    List<Bed> getBedsByBuildingAndRoom(String building, Integer roomNo);

    Optional<Bed> getBedByLocation(String building, Integer roomNo, String bedNo);

    List<Bed> getBedsByStatus(Integer bedStatus);

    boolean updateBedStatus(Long id, Integer bedStatus);

    List<Bed> getAvailableBedsByBuildingAndRoom(String building, Integer roomNo);

    // ==================== 床位入住/换床/退住 ====================

    List<BedDetails> getBedHistory(Integer bedId);

    List<BedDetails> getCustomerBedHistory(Integer customerId);

    BedDetails assignBedToCustomer(Integer customerId, Integer bedId);

    BedDetails swapBed(SwapBedParams params);

    boolean checkoutCustomerFromBed(Integer bedId, Integer customerId);

    // ==================== 换床参数内部类 ====================

    class SwapBedParams {
        private String building;
        private Integer roomNo;
        private String oldBedNo;
        private String newBedNo;
        private Integer clientId;

        public SwapBedParams() {}

        public SwapBedParams(String building, Integer roomNo, String oldBedNo, String newBedNo, Integer clientId) {
            this.building = building;
            this.roomNo = roomNo;
            this.oldBedNo = oldBedNo;
            this.newBedNo = newBedNo;
            this.clientId = clientId;
        }

        public String getBuilding() { return building; }
        public void setBuilding(String building) { this.building = building; }

        public Integer getRoomNo() { return roomNo; }
        public void setRoomNo(Integer roomNo) { this.roomNo = roomNo; }

        public String getOldBedNo() { return oldBedNo; }
        public void setOldBedNo(String oldBedNo) { this.oldBedNo = oldBedNo; }

        public String getNewBedNo() { return newBedNo; }
        public void setNewBedNo(String newBedNo) { this.newBedNo = newBedNo; }

        public Integer getClientId() { return clientId; }
        public void setClientId(Integer clientId) { this.clientId = clientId; }
    }
}
