package com.yiyang.service.impl;

import com.yiyang.entity.Bed;
import com.yiyang.entity.BedDetails;
import com.yiyang.entity.Client;
import com.yiyang.repository.BedDetailsRepository;
import com.yiyang.repository.BedRepository;
import com.yiyang.repository.ClientRepository;
import com.yiyang.service.BedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BedServiceImpl implements BedService {

    @Autowired
    private BedRepository bedRepository;

    @Autowired
    private BedDetailsRepository bedDetailsRepository;

    @Autowired
    private ClientRepository clientRepository;

    // ==================== 床位 CRUD ====================

    @Override
    public Bed addBed(Bed bed) {
        // 合理性校验
        if (bed.getBuilding() == null || bed.getBuilding().trim().isEmpty()) {
            throw new RuntimeException("楼栋不能为空");
        }
        if (bed.getRoomNo() == null || bed.getRoomNo() <= 0) {
            throw new RuntimeException("房间号必须为正整数");
        }
        if (bed.getBedNo() == null || bed.getBedNo().trim().isEmpty()) {
            throw new RuntimeException("床位号不能为空");
        }
        if (bed.getBedStatus() != null && (bed.getBedStatus() < 1 || bed.getBedStatus() > 3)) {
            throw new RuntimeException("床位状态无效，必须为1(空闲)、2(已入住)或3(维修)");
        }

        // 检查同一位置是否已有床
        Optional<Bed> existing = bedRepository.findByBuildingAndRoomNoAndBedNo(
                bed.getBuilding(), bed.getRoomNo(), bed.getBedNo());
        if (existing.isPresent()) {
            throw new RuntimeException("该位置已有床位: " +
                    bed.getBuilding() + "-" + bed.getRoomNo() + "号房-" + bed.getBedNo());
        }

        if (bed.getBedStatus() == null) {
            bed.setBedStatus(1); // 1 = 空闲
        }

        return bedRepository.save(bed);
    }

    @Override
    public Bed updateBed(Long id, Bed bed) {
        Bed existing = bedRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("床位不存在，ID: " + id));

        // 合理性校验：只校验前端传过来的非 null 字段
        if (bed.getBuilding() != null && bed.getBuilding().trim().isEmpty()) {
            throw new RuntimeException("楼栋不能为空字符串");
        }
        if (bed.getRoomNo() != null && bed.getRoomNo() <= 0) {
            throw new RuntimeException("房间号必须为正整数");
        }
        if (bed.getBedNo() != null && bed.getBedNo().trim().isEmpty()) {
            throw new RuntimeException("床位号不能为空字符串");
        }
        if (bed.getBedStatus() != null && (bed.getBedStatus() < 1 || bed.getBedStatus() > 3)) {
            throw new RuntimeException("床位状态无效，必须为1(空闲)、2(已入住)或3(维修)");
        }

        if (bed.getBuilding() != null) {
            existing.setBuilding(bed.getBuilding());
        }
        if (bed.getRoomNo() != null) {
            existing.setRoomNo(bed.getRoomNo());
        }
        if (bed.getBedNo() != null) {
            existing.setBedNo(bed.getBedNo());
        }
        if (bed.getBedStatus() != null) {
            existing.setBedStatus(bed.getBedStatus());
        }
        if (bed.getRemarks() != null) {
            existing.setRemarks(bed.getRemarks());
        }

        return bedRepository.save(existing);
    }

    @Override
    public boolean deleteBed(Long id) {
        if (bedRepository.existsById(id)) {
            bedRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Bed> getBedById(Long id) {
        return bedRepository.findById(id);
    }

    @Override
    public List<Bed> getAllBeds() {
        return bedRepository.findAll();
    }

    @Override
    public List<Bed> getBedsByBuildingAndRoom(String building, Integer roomNo) {
        return bedRepository.findByBuildingAndRoomNo(building, roomNo);
    }

    @Override
    public Optional<Bed> getBedByLocation(String building, Integer roomNo, String bedNo) {
        return bedRepository.findByBuildingAndRoomNoAndBedNo(building, roomNo, bedNo);
    }

    @Override
    public List<Bed> getBedsByStatus(Integer bedStatus) {
        return bedRepository.findByBedStatus(bedStatus);
    }

    @Override
    public boolean updateBedStatus(Long id, Integer bedStatus) {
        Optional<Bed> bedOpt = bedRepository.findById(id);
        if (bedOpt.isPresent()) {
            Bed bed = bedOpt.get();
            Integer oldStatus = bed.getBedStatus();
            
            // ===== 联动：床位状态从占用改为空闲时，清空老人信息 =====
            if (oldStatus == 2 && bedStatus == 1) {
                // 查找占用该床位的老人
                Optional<Client> clientOpt = clientRepository.findByBuildingNoAndRoomNoAndBedNoAndDeletedFalse(
                        bed.getBuilding(), 
                        String.valueOf(bed.getRoomNo()), 
                        bed.getBedNo()
                );
                
                if (clientOpt.isPresent()) {
                    Client client = clientOpt.get();
                    client.setBuildingNo(null);
                    client.setRoomNo(null);
                    client.setBedNo(null);
                    clientRepository.save(client);
                }
                
                // 清空床位备注
                bed.setRemarks(null);
            }
            
            // ===== 联动：床位状态改为占用，但没有备注时，尝试关联老人 =====
            if (bedStatus == 2 && (bed.getRemarks() == null || bed.getRemarks().isEmpty())) {
                Optional<Client> clientOpt = clientRepository.findByBuildingNoAndRoomNoAndBedNoAndDeletedFalse(
                        bed.getBuilding(), 
                        String.valueOf(bed.getRoomNo()), 
                        bed.getBedNo()
                );
                
                if (clientOpt.isPresent()) {
                    bed.setRemarks(clientOpt.get().getName() + "(入住中)");
                }
            }
            
            bed.setBedStatus(bedStatus);
            bedRepository.save(bed);
            return true;
        }
        return false;
    }

    @Override
    public List<Bed> getAvailableBedsByBuildingAndRoom(String building, Integer roomNo) {
        return bedRepository.findByBuildingAndRoomNoAndBedStatus(building, roomNo, 1);
    }

    // ==================== 床位入住/换床/退住 ====================

    @Override
    public List<BedDetails> getBedHistory(Integer bedId) {
        return bedDetailsRepository.findByBedId(bedId);
    }

    @Override
    public List<BedDetails> getCustomerBedHistory(Integer customerId) {
        return bedDetailsRepository.findByCustomerId(customerId);
    }

    @Override
    public BedDetails assignBedToCustomer(Integer customerId, Integer bedId) {
        // 检查床位是否存在且空闲
        Optional<Bed> bedOpt = bedRepository.findById(bedId.longValue());
        if (!bedOpt.isPresent()) {
            throw new RuntimeException("床位不存在，ID: " + bedId);
        }

        Bed bed = bedOpt.get();
        if (bed.getBedStatus() != 1) {
            throw new RuntimeException("床位已被占用");
        }

        // 标记床为占用，并写入备注（老人名字）
        bed.setBedStatus(2); // 2 = 占用
        bed.setRemarks(bed.getBuilding() + "-" + bed.getRoomNo() + "号房-" + bed.getBedNo() + "床");
        bedRepository.save(bed);

        // ===== 联动：同步更新老人的床位信息 =====
        Optional<Client> clientOpt = clientRepository.findById(customerId);
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            client.setBuildingNo(bed.getBuilding());
            client.setRoomNo(bed.getRoomNo() != null ? String.valueOf(bed.getRoomNo()) : null);
            client.setBedNo(bed.getBedNo());
            clientRepository.save(client);

            // 同步写入老人名字到床位备注
            bed.setRemarks(client.getName() + "(入住中)");
            bedRepository.save(bed);
        }

        // 创建入住记录
        BedDetails details = BedDetails.builder()
                .customerId(customerId)
                .bedId(bedId)
                .startDate(new Date())
                .isDeleted(0)
                .build();

        return bedDetailsRepository.save(details);
    }

    @Override
    public BedDetails swapBed(SwapBedParams params) {
        // 找到旧床位
        Optional<Bed> oldBedOpt = bedRepository.findByBuildingAndRoomNoAndBedNo(
                params.getBuilding(), params.getRoomNo(), params.getOldBedNo());
        if (!oldBedOpt.isPresent()) {
            throw new RuntimeException("旧床位不存在");
        }

        // 找到新床位
        Optional<Bed> newBedOpt = bedRepository.findByBuildingAndRoomNoAndBedNo(
                params.getBuilding(), params.getRoomNo(), params.getNewBedNo());
        if (!newBedOpt.isPresent()) {
            throw new RuntimeException("新床位不存在");
        }

        Bed newBed = newBedOpt.get();
        if (newBed.getBedStatus() != 1) {
            throw new RuntimeException("新床位已被占用");
        }

        Bed oldBed = oldBedOpt.get();

        // 关闭旧床位的入住记录
        List<BedDetails> activeRecords = bedDetailsRepository.findByBedId(oldBed.getId().intValue());
        for (BedDetails record : activeRecords) {
            if (record.getEndDate() == null && record.getCustomerId().equals(params.getClientId())) {
                record.setEndDate(new Date());
                bedDetailsRepository.save(record);
            }
        }

        // 新床标记占用
        newBed.setBedStatus(2);
        bedRepository.save(newBed);

        // 旧床标记空闲并清空备注
        oldBed.setBedStatus(1);
        oldBed.setRemarks(null);
        bedRepository.save(oldBed);

        // ===== 联动：同步更新老人的床位信息为新床 =====
        if (params.getClientId() != null) {
            Optional<Client> clientOpt = clientRepository.findById(params.getClientId());
            if (clientOpt.isPresent()) {
                Client client = clientOpt.get();
                client.setBuildingNo(newBed.getBuilding());
                client.setRoomNo(newBed.getRoomNo() != null ? String.valueOf(newBed.getRoomNo()) : null);
                client.setBedNo(newBed.getBedNo());
                clientRepository.save(client);

                // 同步写入老人名字到新床位备注
                newBed.setRemarks(client.getName() + "(入住中)");
                bedRepository.save(newBed);
            }
        }

        // 创建新入住记录
        BedDetails newRecord = BedDetails.builder()
                .customerId(params.getClientId())
                .bedId(newBed.getId().intValue())
                .startDate(new Date())
                .isDeleted(0)
                .build();

        return bedDetailsRepository.save(newRecord);
    }

    @Override
    public boolean checkoutCustomerFromBed(Integer bedId, Integer customerId) {
        // 找到该床位的当前入住记录
        List<BedDetails> records = bedDetailsRepository.findByBedId(bedId);
        for (BedDetails record : records) {
            if (record.getEndDate() == null && record.getCustomerId().equals(customerId)) {
                record.setEndDate(new Date());
                bedDetailsRepository.save(record);
            }
        }

        // 床位标记为空闲
        Optional<Bed> bedOpt = bedRepository.findById(bedId.longValue());
        if (bedOpt.isPresent()) {
            Bed bed = bedOpt.get();
            bed.setBedStatus(1);
            bed.setRemarks(null);
            bedRepository.save(bed);

            // ===== 联动：清空老人的床位信息 =====
            Optional<Client> clientOpt = clientRepository.findById(customerId);
            if (clientOpt.isPresent()) {
                Client client = clientOpt.get();
                client.setBuildingNo(null);
                client.setRoomNo(null);
                client.setBedNo(null);
                clientRepository.save(client);
            }

            return true;
        }
        return false;
    }
}
