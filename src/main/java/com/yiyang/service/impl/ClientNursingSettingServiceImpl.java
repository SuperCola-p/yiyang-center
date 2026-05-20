package com.yiyang.service.impl;

import com.yiyang.entity.ClientNursingSetting;
import com.yiyang.repository.ClientNursingSettingRepository;
import com.yiyang.service.ClientNursingSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClientNursingSettingServiceImpl implements ClientNursingSettingService {

    @Autowired
    private ClientNursingSettingRepository repository;

    // ==================== 护理服务查询 ====================

    @Override
    public List<ClientNursingSetting> getClientSettings(Long clientId) {
        return repository.findByClientIdAndIsDeletedFalse(clientId);
    }

    @Override
    public Optional<ClientNursingSetting> getClientSetting(Long clientId, Long nursingItemId) {
        return repository.findByClientIdAndNursingItemIdAndIsDeletedFalse(clientId, nursingItemId);
    }

    @Override
    public List<ClientNursingSetting> getAllSettings() {
        return repository.findByIsDeletedFalse();
    }

    @Override
    public List<ClientNursingSetting> getSettingsByStatus(String serviceStatus) {
        return repository.findByServiceStatusAndIsDeletedFalse(serviceStatus);
    }

    @Override
    public List<ClientNursingSetting> getExpiringServices(LocalDate beforeDate) {
        return repository.findExpiringServices(beforeDate);
    }

    // ==================== 护理服务购买/消费/续费 ====================

    @Override
    public ClientNursingSetting purchaseService(Long clientId, Long nursingItemId,
                                                Integer quantity) {
        // 检查是否已有该服务的记录（防止重复购买时创建多条）
        Optional<ClientNursingSetting> existing =
                repository.findByClientIdAndNursingItemIdAndIsDeletedFalse(clientId, nursingItemId);

        if (existing.isPresent()) {
            ClientNursingSetting setting = existing.get();
            // 追加数量
            setting.setTotalQuantity(setting.getTotalQuantity() + quantity);
            setting.setRemainingQuantity(setting.getRemainingQuantity() + quantity);
            // 更新到期日
            if (setting.getServiceDueDate() != null) {
                setting.setServiceDueDate(setting.getServiceDueDate().plusMonths(1));
            } else {
                setting.setServiceDueDate(LocalDate.now().plusMonths(1));
            }
            setting.setServiceStatus("ACTIVE");
            return repository.save(setting);
        }

        // 创建新记录
        ClientNursingSetting setting = ClientNursingSetting.builder()
                .clientId(clientId)
                .nursingItemId(nursingItemId)
                .purchaseDate(LocalDate.now())
                .totalQuantity(quantity)
                .remainingQuantity(quantity)
                .serviceDueDate(LocalDate.now().plusMonths(1))
                .serviceStatus("ACTIVE")
                .isDeleted(false)
                .build();
        return repository.save(setting);
    }

    @Override
    public ClientNursingSetting consumeService(Long clientId, Long nursingItemId,
                                               Integer quantity) {
        ClientNursingSetting setting = repository
                .findByClientIdAndNursingItemIdAndIsDeletedFalse(clientId, nursingItemId)
                .orElseThrow(() -> new RuntimeException("该老人没有购买此护理服务"));

        int remaining = setting.getRemainingQuantity();
        if (remaining < quantity) {
            throw new RuntimeException("剩余次数不足，剩余: " + remaining + "，需要: " + quantity);
        }

        setting.setRemainingQuantity(remaining - quantity);

        if (setting.getRemainingQuantity() == 0) {
            setting.setServiceStatus("EXPIRED");
        }

        return repository.save(setting);
    }

    @Override
    public ClientNursingSetting renewService(Long clientId, Long nursingItemId,
                                             Integer additionalQuantity,
                                             LocalDate newDueDate) {
        ClientNursingSetting setting = repository
                .findByClientIdAndNursingItemIdAndIsDeletedFalse(clientId, nursingItemId)
                .orElseThrow(() -> new RuntimeException("该老人没有购买此护理服务"));

        // 追加次数
        setting.setTotalQuantity(setting.getTotalQuantity() + additionalQuantity);
        setting.setRemainingQuantity(setting.getRemainingQuantity() + additionalQuantity);

        // 更新到期日，如果传入新日期则使用新日期，否则顺延一个月
        if (newDueDate != null) {
            setting.setServiceDueDate(newDueDate);
        } else if (setting.getServiceDueDate() != null) {
            setting.setServiceDueDate(setting.getServiceDueDate().plusMonths(1));
        } else {
            setting.setServiceDueDate(LocalDate.now().plusMonths(1));
        }

        setting.setServiceStatus("ACTIVE");
        return repository.save(setting);
    }

    // ==================== 护理服务管理 ====================

    @Override
    public boolean updateServiceStatus(Long id, String status) {
        Optional<ClientNursingSetting> settingOpt = repository.findById(id);
        if (settingOpt.isPresent()) {
            ClientNursingSetting setting = settingOpt.get();
            setting.setServiceStatus(status);
            repository.save(setting);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteServiceSetting(Long id) {
        Optional<ClientNursingSetting> settingOpt = repository.findById(id);
        if (settingOpt.isPresent()) {
            ClientNursingSetting setting = settingOpt.get();
            setting.setIsDeleted(true);
            repository.save(setting);
            return true;
        }
        return false;
    }
}
