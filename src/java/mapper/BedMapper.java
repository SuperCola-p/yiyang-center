package java.mapper;

import java.entity.Bed;
import java.entity.BedDetails;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BedMapper {
    private Customer customer;
    private ArrayList<Bed> beds;
    private ArrayList<Customer> customers;

    private static final String DEFAULT_BUILDING = "606";

    public BedMapper() {
        this.customer = new Customer();
        this.beds = new ArrayList<>();
        this.customers = new ArrayList<>();
    }

    public BedMapper(Customer customer) {
        this.customer = customer;
        this.beds = new ArrayList<>();
        this.customers = new ArrayList<>();
    }

    public BedMapper(ArrayList<Bed> beds) {
        this.beds = beds;
        this.customer = new Customer();
        this.customers = new ArrayList<>();
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
     * 增加床位
     *
     * @param bed 要添加的床位对象
     * @return 添加成功返回 true
     */
    public boolean addBed(Bed bed) {
        if (bed == null) {
            return false;
        }
        // 如果没有设置楼号，默认使用606
        if (bed.getBuilding() == null || bed.getBuilding().isEmpty()) {
            bed.setBuilding(DEFAULT_BUILDING);
        }
        // 如果没有设置状态，默认设为空闲
        if (bed.getBedStatus() == null) {
            bed.setBedStatus(1);  // 1: 空闲
        }
        beds.add(bed);
        return true;
    }

    /**
     * 删除床位（根据床位ID）
     *
     * @param bedId 要删除的床位ID
     * @return 删除成功返回 true
     */
    public boolean deleteBed(Integer bedId) {
        if (bedId == null) {
            return false;
        }
        // 查找并删除
        for (int i = 0; i < beds.size(); i++) {
            Bed bed = beds.get(i);
            if (bed.getId() != null && bed.getId().equals(bedId)) {
                beds.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * 修改床位信息
     *
     * @param bed 包含更新信息的床位对象（需要有ID）
     * @return 修改成功返回 true
     */
    public boolean updateBed(Bed bed) {
        if (bed == null || bed.getId() == null) {
            return false;
        }

        for (int i = 0; i < beds.size(); i++) {
            Bed existingBed = beds.get(i);
            if (existingBed.getId().equals(bed.getId())) {
                // 更新非空字段
                if (bed.getBuilding() != null && !bed.getBuilding().isEmpty()) {
                    existingBed.setBuilding(bed.getBuilding());
                }
                if (bed.getRoomNo() != null) {
                    existingBed.setRoomNo(bed.getRoomNo());
                }
                if (bed.getBedStatus() != null) {
                    existingBed.setBedStatus(bed.getBedStatus());
                }
                if (bed.getRemarks() != null) {
                    existingBed.setRemarks(bed.getRemarks());
                }
                if (bed.getBedNo() != null && !bed.getBedNo().isEmpty()) {
                    existingBed.setBedNo(bed.getBedNo());
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 显示全部床位及信息
     */
    public void displayAllBeds() {
        if (beds == null || beds.isEmpty()) {
            System.out.println("暂无床位信息");
            return;
        }

        System.out.println("========== 床位信息列表 ==========");
        for (Bed bed : beds) {
            System.out.println("床位ID: " + bed.getId());
            System.out.println("楼号: " + bed.getBuilding());
            System.out.println("房间号: " + bed.getRoomNo());
            System.out.println("床位编号: " + bed.getBedNo());
            System.out.println("状态: " + getStatusDescription(bed.getBedStatus()));
            System.out.println("备注: " + (bed.getRemarks() != null ? bed.getRemarks() : "无"));
            System.out.println("--------------------------");
        }
    }

    /**
     * 获取状态描述
     */
    private String getStatusDescription(Integer status) {
        if (status == null) {
            return "未知";
        }
        switch (status) {
            case 1:
                return "空闲";
            case 2:
                return "有人";
            case 3:
                return "外出";
            default:
                return "未知";
        }
    }
}