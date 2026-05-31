package com.yiyang.service;

import com.yiyang.entity.ClientNursingSetting;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ClientNursingSettingService {

    // ==================== 护理服务查询 ====================

    List<ClientNursingSetting> getClientSettings(Long clientId);

    Optional<ClientNursingSetting> getClientSetting(Long clientId, Long nursingItemId);

    List<ClientNursingSetting> getAllSettings();

    List<ClientNursingSetting> getSettingsByStatus(String serviceStatus);

    List<ClientNursingSetting> getExpiringServices(LocalDate beforeDate);

    // ==================== 护理服务购买/消费/续费 ====================

    ClientNursingSetting purchaseService(Long clientId, Long nursingItemId,
                                         Integer quantity);

    /**
     * 购买护理服务（完整版，含护理等级和到期日期）
     */
    ClientNursingSetting purchaseService(Long clientId, Long nursingItemId,
                                         Long nursingLevelId,
                                         Integer totalQuantity,
                                         LocalDate serviceDueDate);

    ClientNursingSetting consumeService(Long clientId, Long nursingItemId,
                                        Integer quantity);

    /**
     * 通过设置ID消费护理服务（扣减剩余次数）
     */
    ClientNursingSetting consumeService(Long settingId, Integer quantity);

    ClientNursingSetting renewService(Long clientId, Long nursingItemId,
                                      Integer additionalQuantity,
                                      LocalDate newDueDate);

    /**
     * 通过设置ID续费护理服务
     */
    ClientNursingSetting renewService(Long settingId, Integer additionalQuantity,
                                      LocalDate newDueDate);

    // ==================== 护理服务管理 ====================

    boolean updateServiceStatus(Long id, String status);

    boolean deleteServiceSetting(Long id);
}
