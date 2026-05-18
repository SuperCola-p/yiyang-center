package java.BED;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ControlBed {
    private Customer customer;
    private ArrayList<Bed> beds;
    private ArrayList<Customer> customers;
    
    private static final String DEFAULT_BUILDING = "606";

    public ControlBed() {
        this.customer = new Customer();
        this.beds = new ArrayList<>();
        this.customers = new ArrayList<>();
    }

    public ControlBed(Customer customer) {
        this.customer = customer;
        this.beds = new ArrayList<>();
        this.customers = new ArrayList<>();
    }

    public ControlBed(ArrayList<Bed> beds) {
        this.beds = beds;
        this.customer = new Customer();
        this.customers = new ArrayList<>();
    }

    public void controlBedDisplay() {
        System.out.println("choice");
        System.out.println("1.查询床位信息");
        System.out.println("2.修改床位信息");
        System.out.println("3.床位调换");
    }

    public enum UsageStatus {
        CURRENT,
        HISTORY
    }

    /**
     * 查询客户床位使用详情列表（默认查询正在使用的）
     *
     * @param customerName 客户姓名（模糊匹配）
     * @param checkInDate  入住日期
     * @param status       使用状态（null表示默认查询正在使用的）
     * @return 匹配的床位使用详情列表
     */
    public List<BedDetails> queryBedDetails(String customerName, Date checkInDate, UsageStatus status) {
        UsageStatus currentStatus = status;
        if (currentStatus == null) {
            currentStatus = UsageStatus.CURRENT;
        }

        final UsageStatus effectiveStatus = currentStatus;

        return customers.stream()
                .flatMap(c -> {
                    ArrayList<BedDetails> detailsList = c.getBedDetailsList();
                    if (detailsList == null || detailsList.isEmpty()) {
                        return List.<BedDetails>of().stream();
                    }
                    return detailsList.stream();
                })
                .filter(details -> {
                    Date now = new Date();
                    boolean isCurrent = details.getEndDate() == null || details.getEndDate().after(now);

                    if (effectiveStatus == UsageStatus.CURRENT && !isCurrent) {
                        return false;
                    }
                    if (effectiveStatus == UsageStatus.HISTORY && isCurrent) {
                        return false;
                    }
                    return true;
                })
                .filter(details -> {
                    if (customerName == null || customerName.isEmpty()) {
                        return true;
                    }
                    for (Customer c : customers) {
                        ArrayList<BedDetails> list = c.getBedDetailsList();
                        if (list != null) {
                            for (BedDetails bd : list) {
                                if (bd.getId() != null && bd.getId().equals(details.getId())) {
                                    String name = c.getName();
                                    return name != null && name.contains(customerName);
                                }
                            }
                        }
                    }
                    return false;
                })
                .filter(details -> {
                    if (checkInDate == null) {
                        return true;
                    }
                    Date startDate = details.getStartDate();
                    return startDate != null && startDate.equals(checkInDate);
                })
                .collect(Collectors.toList());
    }

    /**
     * 修改床位详情：只能修改床位使用的结束时间
     *
     * @param bedDetailsId 床位详情ID
     * @param newEndDate   新的结束时间
     * @return 是否修改成功
     */
    public boolean modifyBedEndDate(Integer bedDetailsId, Date newEndDate) {
        for (Customer c : customers) {
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

        Customer targetCustomer = null;
        for (Customer c : customers) {
            if (c.getName() != null && c.getName().equals(customerName)) {
                targetCustomer = c;
                break;
            }
        }
        if (targetCustomer == null) {
            return false;
        }

        BedDetails currentDetails = targetCustomer.getCurrentBedDetails();
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
            if (bed.getId() != null && bed.getId().equals(currentDetails.getBedId())) {
                oldBed = bed;
                break;
            }
        }

        currentDetails.setEndDate(now);

        BedDetails newDetails = new BedDetails();
        newDetails.setStartDate(now);
        newDetails.setCustomerId(currentDetails.getCustomerId());
        newDetails.setBedId(newBed.getId());
        targetCustomer.addBedDetails(newDetails);

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
            building = DEFAULT_BUILDING;
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
            building = DEFAULT_BUILDING;
        }
        return beds.stream()
                .filter(bed -> building.equals(bed.getBuilding()))
                .filter(bed -> roomNo.equals(bed.getRoomNo()))
                .filter(bed -> bed.getBedStatus() != null && bed.getBedStatus() == 1)
                .collect(Collectors.toList());
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ArrayList<Bed> getBeds() {
        return beds;
    }

    public void setBeds(ArrayList<Bed> beds) {
        this.beds = beds;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(ArrayList<Customer> customers) {
        this.customers = customers;
    }
}