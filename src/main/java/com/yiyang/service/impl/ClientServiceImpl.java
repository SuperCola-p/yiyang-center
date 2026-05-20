package com.yiyang.service.impl;

import com.yiyang.entity.Client;
import com.yiyang.entity.ClientNursingSetting;
import com.yiyang.entity.NursingLevel;
import com.yiyang.entity.NursingLevelItem;
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

    // ==================== 老人基本信息 CRUD ====================

    @Override
    public Client addClient(Client client) {
        if (client.getDeleted() == null) {
            client.setDeleted(false);
        }
        return clientRepository.save(client);
    }

    @Override
    public Client updateClient(Integer id, Client client) {
        Client existing = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("老人信息不存在，ID: " + id));

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

        return clientRepository.save(existing);
    }

    @Override
    public boolean deleteClient(Integer id) {
        Optional<Client> clientOpt = clientRepository.findById(id);
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
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
