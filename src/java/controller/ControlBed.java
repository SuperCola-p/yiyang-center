package java.controller;

import java.entity.Client;
import java.entity.Bed;
import java.entity.BedDetails;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ControlBed {
    private Client customer;
    private ArrayList<Bed> beds;
    private ArrayList<Client> customers;
    
    private static final String DEFAULT_BUILDING = "606";

    public ControlBed() {
        this.customer = new Client();
        this.beds = new ArrayList<>();
        this.customers = new ArrayList<>();
    }

    public ControlBed(Client customer) {
        this.customer = customer;
        this.beds = new ArrayList<>();
        this.customers = new ArrayList<>();
    }

    public ControlBed(ArrayList<Bed> beds) {
        this.beds = beds;
        this.customer = new Client();
        this.customers = new ArrayList<>();
    }

  

    public enum UsageStatus {
        CURRENT,
        HISTORY
    }

    

    /**
     * 修改床位详情：只能修改床位使用的结束时间
     *
     * @param bedDetailsId 床位详情ID
     * @param newEndDate   新的结束时间
     * @return 是否修改成功
     */
    public boolean modifyBedEndDate(Integer bedDetailsId, Date newEndDate) {
        for (Client c : customers) {
            ArrayList<BedDetails> detailsList = c.getBedDetailsList();
            if (detailsList != null) {
                for (BedDetails details : detailsList) {
                    if (details.getId() != null && details.getId().equals(bedDetailsId)) {
                        details.setEndDate(newEndDate);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 床位调换：管理员根据客户要求进行床位调换，只能当天调换当天办理
     *
     * @param customerName 客户姓名
     * @param newBedNo     新床位编号
     * @return 是否调换成功
     */
    public boolean swapBed(String customerName, String newBedNo) {
        Date now = new Date();

        Client targetCustomer = null;
        for (Client c : customers) {
            if (c.getName() != null && c.getName().equals(customerName)) {
                targetCustomer = c;
                break;
            }
        }
        if (targetCustomer == null) {
            return false;
        }

        Client targetClient = null;
        for (Client c : customers) {
            if (c.getName() != null && c.getName().equals(customerName)) {
                targetClient = c;
                break;
            }
        }
        if (targetClient == null) {
            return false;
        }

        BedDetails currentDetails = targetClient.getCurrentBedDetails();
        if (currentDetails == null) {
            return false;
        }

        Bed newBed = null;
        for (Bed bed : beds) {
            if (bed.getBedNo() != null && bed.getBedNo().equals(newBedNo)
                    && bed.getBedStatus() != null && bed.getBedStatus() == 1) {
                newBed = bed;
                break;
            }
        }
        if (newBed == null) {
            return false;
        }

        Bed oldBed = null;
        for (Bed bed : beds) {
            if (bed.getBuilding() != null && bed.getBuilding().equals(DEFAULT_BUILDING)
                    && bed.getId() != null && bed.getId().equals(currentDetails.getBedId())) {
                oldBed = bed;
                break;
            }
        }

        currentDetails.setEndDate(now);

        BedDetails newDetails = new BedDetails();
        newDetails.setStartDate(now);
        newDetails.setCustomerId(currentDetails.getCustomerId());
        newDetails.setBedId(newBed.getId().intValue());
        targetCustomer.addNewBedDetails(newDetails);

        if (oldBed != null) {
            oldBed.setBedStatus(1);
        }
        newBed.setBedStatus(2);

        targetCustomer.setBed(newBed);

        return true;
    }

    /**
     * 获取所有空闲床位
     */
    public List<Bed> getAvailableBeds() {
        return beds.stream()
                .filter(bed -> bed.getBedStatus() != null && bed.getBedStatus() == 1)
                .collect(Collectors.toList());
    }

    /**
     * 根据楼号获取空闲床位
     */
    public List<Bed> getAvailableBedsByBuilding(String building) {
        return beds.stream()
                .filter(bed -> building.equals(bed.getBuilding()))
                .filter(bed -> bed.getBedStatus() != null && bed.getBedStatus() == 1)
                .collect(Collectors.toList());
    }

    /**
     * 获取所有楼层信息（固定606）
     */
    public List<String> getAllBuildings() {
        List<String> result = new ArrayList<>();
        result.add(DEFAULT_BUILDING);
        return result;
    }

    /**
     * 获取所有楼层的房间号（下拉列表分组）
     * 返回格式：Map<楼号, List<房间号>>
     */
    public Map<String, List<Integer>> getBuildingsGroupedByRoom() {
        Map<String, List<Integer>> result = new HashMap<>();
        for (Bed bed : beds) {
            String building = bed.getBuilding();
            if (building == null) {
                building = DEFAULT_BUILDING;
            }
            if (bed.getRoomNo() != null) {
                result.computeIfAbsent(building, k -> new ArrayList<>());
                if (!result.get(building).contains(bed.getRoomNo())) {
                    result.get(building).add(bed.getRoomNo());
                }
            }
        }
        for (List<Integer> roomNos : result.values()) {
            roomNos.sort(Integer::compareTo);
        }
        return result;
    }

    /**
     * 根据楼号和房间号获取床位列表
     */
    public List<Bed> getBedsByBuildingAndRoomNo(String building, Integer roomNo) {
        if (building == null) {
            final String finalBuilding = DEFAULT_BUILDING;
            final String finalBuildingForFilter = finalBuilding;
            final String effectiveBuilding = finalBuilding;
        }
        return beds.stream()
                .filter(bed -> building.equals(bed.getBuilding()))

                .filter(bed -> roomNo.equals(bed.getRoomNo()))
                .collect(Collectors.toList());
    }

    /**
     * 根据房间号联动获取房间内的空闲床位
     *
     * @param roomNo 房间号
     * @return 该房间内的空闲床位列表
     */
    public List<Bed> getAvailableBedsByRoomNo(Integer roomNo) {
        return beds.stream()
                .filter(bed -> roomNo.equals(bed.getRoomNo()))
                .filter(bed -> bed.getBedStatus() != null && bed.getBedStatus() == 1)
                .collect(Collectors.toList());
    }

    /**
     * 根据楼号、房间号获取空闲床位
     *
     * @param building 楼号
     * @param roomNo   房间号
     * @return 该房间内的空闲床位列表
     */
    public List<Bed> getAvailableBedsByBuildingAndRoomNo(String building, Integer roomNo) {
        if (building == null) {
            final String effectiveBuilding = DEFAULT_BUILDING;
        }
        return beds.stream()
                .filter(bed -> building.equals(bed.getBuilding()))
                .filter(bed -> roomNo.equals(bed.getRoomNo()))
                .filter(bed -> bed.getBedStatus() != null && bed.getBedStatus() == 1)
                .collect(Collectors.toList());
    }

    public Client getCustomer() {
        return customer;
    }

    public void setCustomer(Client customer) {
        this.customer = customer;
    }

    public ArrayList<Bed> getBeds() {
        return beds;
    }

    public void setBeds(ArrayList<Bed> beds) {
        this.beds = beds;
    }

    public ArrayList<Client> getCustomers() {
        return customers;
    }

    public void setCustomers(ArrayList<Client> customers) {
        this.customers = customers;
    }
}