package com.yiyang.service.impl;

import com.yiyang.entity.Bed;
import com.yiyang.entity.Client;
import com.yiyang.entity.ClientNursingSetting;
import com.yiyang.entity.NursingLevel;
import com.yiyang.entity.NursingLevelItem;
import com.yiyang.repository.BedRepository;
import com.yiyang.repository.ClientNursingSettingRepository;
import com.yiyang.repository.ClientRepository;
import com.yiyang.repository.NursingLevelItemRepository;
import com.yiyang.repository.NursingLevelRepository;
import com.yiyang.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private NursingLevelRepository nursingLevelRepository;

    @Autowired
    private NursingLevelItemRepository nursingLevelItemRepository;

    @Autowired
    private ClientNursingSettingRepository clientNursingSettingRepository;

    @Autowired
    private BedRepository bedRepository;

    // ==================== 老人基本信息 CRUD ====================

    @Override
    public Client addClient(Client client) {
        if (client.getDeleted() == null) {
            client.setDeleted(false);
        }
        
        // ===== 联动：新增老人时自动分配床位 =====
        String building = client.getBuildingNo();
        String roomNo = client.getRoomNo();
        String bedNo = client.getBedNo();
        
        boolean hasBed = building != null && !building.isEmpty()
                      && roomNo != null && !roomNo.isEmpty()
                      && bedNo != null && !bedNo.isEmpty();
        
        if (hasBed) {
            try {
                Integer roomInt = Integer.parseInt(roomNo);
                Optional<Bed> bedOpt = bedRepository.findByBuildingAndRoomNoAndBedNo(building, roomInt, bedNo);
                
                if (!bedOpt.isPresent()) {
                    throw new RuntimeException("目标床位不存在：" + building + "-" + roomNo + "-" + bedNo
                            + "，请先在床位管理中创建该床位");
                }
                
                Bed bed = bedOpt.get();
                if (bed.getBedStatus() != 1) {
                    throw new RuntimeException("目标床位已被占用：" + building + "-" + roomNo + "-" + bedNo);
                }
                
                // 先保存老人，获取ID和姓名
                Client savedClient = clientRepository.save(client);
                
                // 占用床位，备注老人姓名
                bed.setBedStatus(2); // 占用
                bed.setRemarks(savedClient.getName() + "(入住中)");
                bedRepository.save(bed);
                
                return savedClient;
            } catch (NumberFormatException e) {
                throw new RuntimeException("房间号必须为数字：" + roomNo);
            }
        }
        
        return clientRepository.save(client);
    }

    @Override
    public Client updateClient(Integer id, Client client) {
        Client existing = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("老人信息不存在，ID: " + id));

        // ===== 联动：检测床位是否变化 =====
        String oldBuilding = existing.getBuildingNo();
        String oldRoomNo   = existing.getRoomNo();
        String oldBedNo    = existing.getBedNo();

        boolean bedChanging = false;
        String newBuilding = (client.getBuildingNo() != null) ? client.getBuildingNo() : oldBuilding;
        String newRoomNo   = (client.getRoomNo()    != null) ? client.getRoomNo()    : oldRoomNo;
        String newBedNo    = (client.getBedNo()     != null) ? client.getBedNo()     : oldBedNo;

        // 判断床位信息是否实际发生了变化
        boolean oldHasBed = oldBuilding != null && !oldBuilding.isEmpty()
                         && oldRoomNo   != null && !oldRoomNo.isEmpty()
                         && oldBedNo    != null && !oldBedNo.isEmpty();
        boolean newHasBed = newBuilding != null && !newBuilding.isEmpty()
                         && newRoomNo   != null && !newRoomNo.isEmpty()
                         && newBedNo    != null && !newBedNo.isEmpty();

        if (oldHasBed && newHasBed) {
            bedChanging = !(oldBuilding.equals(newBuilding)
                         && oldRoomNo.equals(newRoomNo)
                         && oldBedNo.equals(newBedNo));
        } else if (!oldHasBed && newHasBed) {
            bedChanging = true; // 新分配
        } else if (oldHasBed && !newHasBed) {
            bedChanging = true; // 清除床位
        }

        // 更新老人基本信息
        if (client.getName() != null) existing.setName(client.getName());
        if (client.getAge() != null) existing.setAge(client.getAge());
        if (client.getGender() != null) existing.setGender(client.getGender());
        if (client.getBloodType() != null) existing.setBloodType(client.getBloodType());
        if (client.getPhone() != null) existing.setPhone(client.getPhone());
        if (client.getFamilyContact() != null) existing.setFamilyContact(client.getFamilyContact());
        if (client.getIdCard() != null) existing.setIdCard(client.getIdCard());
        if (client.getBuildingNo() != null) existing.setBuildingNo(client.getBuildingNo());
        if (client.getRoomNo() != null) existing.setRoomNo(client.getRoomNo());
        if (client.getBedNo() != null) existing.setBedNo(client.getBedNo());
        if (client.getBirthday() != null) existing.setBirthday(client.getBirthday());
        if (client.getCheckInDate() != null) existing.setCheckInDate(client.getCheckInDate());
        if (client.getContractExpireDate() != null) existing.setContractExpireDate(client.getContractExpireDate());
        if (client.getNursingLevel() != null) existing.setNursingLevel(client.getNursingLevel());
        if (client.getNurse() != null) existing.setNurse(client.getNurse());
        if (client.getHealthStatus() != null) existing.setHealthStatus(client.getHealthStatus());
        if (client.getType() != null) existing.setType(client.getType());

        // ===== 联动：同步更新床位状态 & 备注 =====
        if (bedChanging) {
            String clientName = existing.getName(); // 当前老人名字
            // 释放旧床位
            if (oldHasBed) {
                try {
                    Integer oldRoomInt = Integer.parseInt(oldRoomNo);
                    bedRepository.findByBuildingAndRoomNoAndBedNo(oldBuilding, oldRoomInt, oldBedNo)
                            .ifPresent(oldBed -> {
                                oldBed.setBedStatus(1); // 空闲
                                oldBed.setRemarks(null);  // 清空备注
                                bedRepository.save(oldBed);
                            });
                } catch (NumberFormatException ignored) {}
            }
            // 占用新床位：先校验床位是否存在
            if (newHasBed) {
                try {
                    Integer newRoomInt = Integer.parseInt(newRoomNo);
                    Optional<Bed> targetBedOpt = bedRepository.findByBuildingAndRoomNoAndBedNo(
                            newBuilding, newRoomInt, newBedNo);
                    if (!targetBedOpt.isPresent()) {
                        throw new RuntimeException("目标床位不存在：" + newBuilding + "-" + newRoomNo + "-" + newBedNo
                                + "，请先在床位管理中创建该床位");
                    }
                    Bed targetBed = targetBedOpt.get();
                    if (targetBed.getBedStatus() != 1) {
                        throw new RuntimeException("目标床位已被占用：" + newBuilding + "-" + newRoomNo + "-" + newBedNo);
                    }
                    targetBed.setBedStatus(2); // 占用
                    targetBed.setRemarks(clientName + "(入住中)"); // 备注为老人名字
                    bedRepository.save(targetBed);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("房间号必须为数字：" + newRoomNo);
                }
            }
        }

        return clientRepository.save(existing);
    }

    @Override
    public boolean deleteClient(Integer id) {
        Optional<Client> clientOpt = clientRepository.findById(id);
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            
            // ===== 联动：删除老人时释放床位 =====
            String building = client.getBuildingNo();
            String roomNo = client.getRoomNo();
            String bedNo = client.getBedNo();
            
            boolean hasBed = building != null && !building.isEmpty()
                          && roomNo != null && !roomNo.isEmpty()
                          && bedNo != null && !bedNo.isEmpty();
            
            if (hasBed) {
                try {
                    Integer roomInt = Integer.parseInt(roomNo);
                    bedRepository.findByBuildingAndRoomNoAndBedNo(building, roomInt, bedNo)
                            .ifPresent(bed -> {
                                bed.setBedStatus(1); // 空闲
                                bed.setRemarks(null); // 清空备注
                                bedRepository.save(bed);
                            });
                } catch (NumberFormatException ignored) {
                    // 房间号解析失败，跳过床位释放
                }
            }
            
            client.setDeleted(true);
            clientRepository.save(client);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Client> getClientById(Integer id) {
        return clientRepository.findByIdAndDeletedFalse(id);
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findByDeletedFalse();
    }

    @Override
    public long getClientCount() {
        return clientRepository.countByDeletedFalse();
    }

    // ==================== 查询/搜索 ====================

    @Override
    public List<Client> searchClients(String name, String type) {
        return clientRepository.searchClients(name, type);
    }

    @Override
    public List<Client> searchClients(String name, String buildingNo, String roomNo) {
        return clientRepository.searchClients(name, buildingNo, roomNo);
    }

    @Override
    public List<Client> getClientsByType(String type) {
        return clientRepository.findByTypeAndDeletedFalse(type);
    }

    @Override
    public List<Client> getClientsByNursingLevel(String nursingLevel) {
        // 使用 Repository 的 searchClients 方法模糊匹配
        return clientRepository.searchClients(null, null).stream()
                .filter(c -> c.getNursingLevel() != null && c.getNursingLevel().contains(nursingLevel))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Client> getClientByBed(String buildingNo, String roomNo, String bedNo) {
        return clientRepository.findByBuildingNoAndRoomNoAndBedNoAndDeletedFalse(buildingNo, roomNo, bedNo);
    }

    // ==================== 护理等级设定 ====================

    @Override
    public void assignNursingLevel(Integer clientId, Long nursingLevelId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("老人信息不存在，ID: " + clientId));

        NursingLevel level = nursingLevelRepository.findById(nursingLevelId)
                .orElseThrow(() -> new RuntimeException("护理等级不存在，ID: " + nursingLevelId));

        // 更新老人的护理等级名称
        client.setNursingLevel(level.getLevelName());
        clientRepository.save(client);

        // 获取该等级关联的所有护理项目
        List<NursingLevelItem> levelItems = nursingLevelItemRepository.findByLevelId(nursingLevelId);

        // 为老人创建或更新护理服务记录
        for (NursingLevelItem levelItem : levelItems) {
            Optional<ClientNursingSetting> existingSetting =
                    clientNursingSettingRepository.findByClientIdAndNursingItemIdAndIsDeletedFalse(
                            clientId.longValue(), levelItem.getItemId());

            if (existingSetting.isEmpty()) {
                ClientNursingSetting setting = ClientNursingSetting.builder()
                        .clientId(clientId.longValue())
                        .nursingItemId(levelItem.getItemId())
                        .nursingLevelId(nursingLevelId)
                        .purchaseDate(LocalDate.now())
                        .totalQuantity(0)
                        .remainingQuantity(0)
                        .serviceStatus("ACTIVE")
                        .isDeleted(false)
                        .build();
                clientNursingSettingRepository.save(setting);
            }
        }
    }
}
