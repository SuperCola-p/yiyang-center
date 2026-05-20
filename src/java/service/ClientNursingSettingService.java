// ClientNursingSettingService.java
package java.service;

import java.entity.ClientNursingSetting;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ClientNursingSettingService {

    // 为客户设置护理服务
    ClientNursingSetting setClientNursingService(ClientNursingSetting setting);

    // 更新客户护理服务
    ClientNursingSetting updateClientNursingService(Long id, ClientNursingSetting setting);

    // 删除客户的护理服务设置
    boolean deleteClientNursingService(Long id);

    // 根据ID查询客户护理设置
    Optional<ClientNursingSetting> getClientNursingSettingById(Long id);

    // 根据客户ID查询所有护理服务
    List<ClientNursingSetting> getClientNursingServicesByClientId(Long clientId);

    // 根据客户ID和护理项目ID查询
    Optional<ClientNursingSetting> getClientNursingServiceByClientAndItem(Long clientId, Long nursingItemId);

    // 根据护理级别为客户设置护理项目
    List<ClientNursingSetting> setNursingItemsByLevel(Long clientId, Long nursingLevelId);

    // 消耗护理服务（执行护理时调用）
    boolean consumeNursingService(Long id, Integer quantity);

    // 续费/增加护理服务数量
    ClientNursingSetting renewNursingService(Long id, Integer additionalQuantity, LocalDate newDueDate);

    // 查询即将到期的护理服务
    List<ClientNursingSetting> getExpiringServices(LocalDate beforeDate);

    // 查询欠费或到期的护理服务
    List<ClientNursingSetting> getServicesByStatus(String status);

    // 检查服务是否可用（数量足够且未到期）
    boolean isServiceAvailable(Long id);

    // 更新服务状态
    boolean updateServiceStatus(Long id, String status);
}