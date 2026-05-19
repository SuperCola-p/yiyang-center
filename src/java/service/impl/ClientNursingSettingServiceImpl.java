package java.service.impl;

import java.entity.ClientNursingSetting;
import java.DAO.ClientNursingSettingDAO;
import java.entity.NursingItem;
import java.service.ClientNursingSettingService;
import java.service.NursingItemService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClientNursingSettingServiceImpl implements ClientNursingSettingService {

    private final ClientNursingSettingDAO clientNursingSettingDAO = ClientNursingSettingDAO.getInstance();
    private final NursingItemService nursingItemService = new NursingItemServiceImpl();

    @Override
    public ClientNursingSetting setClientNursingService(ClientNursingSetting setting) {
        // 验证必填字段
        if (setting.getClientId() == null) {
            throw new RuntimeException("客户ID不能为空");
        }

        if (setting.getNursingItemId() == null) {
            throw new RuntimeException("护理项目ID不能为空");
        }

        // 检查护理项目是否存在
        Optional<NursingItem> itemOpt = nursingItemService.getNursingItemById(setting.getNursingItemId());
        if (!itemOpt.isPresent()) {
            throw new RuntimeException("护理项目不存在，ID: " + setting.getNursingItemId());
        }

        // 检查是否已存在相同的设置
        List<ClientNursingSetting> existingSettings = clientNursingSettingDAO.findAll().stream()
                .filter(s -> setting.getClientId().equals(s.getClientId()) &&
                        setting.getNursingItemId().equals(s.getNursingItemId()) &&
                        (s.getDeleted() == null || !s.getDeleted()))
                .collect(Collectors.toList());

        if (!existingSettings.isEmpty()) {
            throw new RuntimeException("客户已存在相同的护理服务设置");
        }

        // 检查护理项目状态
        NursingItem item = itemOpt.get();
        if ("停用".equals(item.getStatus())) {
            throw new RuntimeException("无法设置已停用的护理项目: " + item.getName());
        }

        // 设置默认值
        if (setting.getPurchaseDate() == null) {
            setting.setPurchaseDate(LocalDate.now());
        }

        if (setting.getTotalQuantity() == null || setting.getTotalQuantity() <= 0) {
            setting.setTotalQuantity(1);
        }

        if (setting.getRemainingQuantity() == null) {
            setting.setRemainingQuantity(setting.getTotalQuantity());
        }

        if (setting.getServiceStatus() == null) {
            setting.setServiceStatus("正常");
        }

        if (setting.getServiceDueDate() == null) {
            setting.setServiceDueDate(setting.getPurchaseDate().plusMonths(1));
        }

        if (setting.getDeleted() == null) {
            setting.setDeleted(false);
        }

        return clientNursingSettingDAO.save(setting);
    }

    @Override
    public ClientNursingSetting updateClientNursingService(Long id, ClientNursingSetting setting) {
        // 先查找是否存在
        Optional<ClientNursingSetting> existingOpt = clientNursingSettingDAO.findById(id);
        if (!existingOpt.isPresent()) {
            throw new RuntimeException("客户护理设置不存在，ID: " + id);
        }

        ClientNursingSetting existing = existingOpt.get();

        // 更新字段
        if (setting.getTotalQuantity() != null && setting.getTotalQuantity() > 0) {
            int oldQuantity = existing.getTotalQuantity();
            int newQuantity = setting.getTotalQuantity();

            if (newQuantity > oldQuantity) {
                // 增加总量，剩余数量也相应增加
                int diff = newQuantity - oldQuantity;
                existing.setRemainingQuantity(existing.getRemainingQuantity() + diff);
            } else if (newQuantity < oldQuantity) {
                // 减少总量，但不能小于已使用数量
                int used = oldQuantity - existing.getRemainingQuantity();
                if (newQuantity < used) {
                    throw new RuntimeException("新的总数量不能小于已使用的数量: " + used);
                }
                existing.setRemainingQuantity(newQuantity - used);
            }

            existing.setTotalQuantity(newQuantity);
        }

        if (setting.getRemainingQuantity() != null && setting.getRemainingQuantity() >= 0) {
            if (setting.getRemainingQuantity() > existing.getTotalQuantity()) {
                throw new RuntimeException("剩余数量不能大于总数量");
            }
            existing.setRemainingQuantity(setting.getRemainingQuantity());
        }

        if (setting.getServiceDueDate() != null) {
            existing.setServiceDueDate(setting.getServiceDueDate());
        }

        if (setting.getServiceStatus() != null) {
            existing.setServiceStatus(setting.getServiceStatus());
        }

        if (setting.getNursingLevelId() != null) {
            existing.setNursingLevelId(setting.getNursingLevelId());
        }

        // 检查并更新服务状态
        checkAndUpdateServiceStatus(existing);

        return clientNursingSettingDAO.update(existing);
    }

    @Override
    public boolean deleteClientNursingService(Long id) {
        return clientNursingSettingDAO.softDelete(id);
    }

    @Override
    public Optional<ClientNursingSetting> getClientNursingSettingById(Long id) {
        return clientNursingSettingDAO.findById(id);
    }

    @Override
    public List<ClientNursingSetting> getClientNursingServicesByClientId(Long clientId) {
        List<ClientNursingSetting> settings = clientNursingSettingDAO.findAll().stream()
                .filter(setting -> clientId.equals(setting.getClientId()) &&
                        (setting.getDeleted() == null || !setting.getDeleted()))
                .collect(Collectors.toList());

        // 检查并更新每个服务的状态
        for (ClientNursingSetting setting : settings) {
            checkAndUpdateServiceStatus(setting);
        }

        return settings;
    }

    @Override
    public Optional<ClientNursingSetting> getClientNursingServiceByClientAndItem(Long clientId, Long nursingItemId) {
        return clientNursingSettingDAO.findAll().stream()
                .filter(setting -> clientId.equals(setting.getClientId()) &&
                        nursingItemId.equals(setting.getNursingItemId()) &&
                        (setting.getDeleted() == null || !setting.getDeleted()))
                .findFirst();
    }

    @Override
    public List<ClientNursingSetting> setNursingItemsByLevel(Long clientId, Long nursingLevelId) {
        // 这个功能需要NursingLevelService的支持
        // 由于我们没有NursingLevelService的完整实现，这里先返回空列表
        // 实际实现应该：
        // 1. 通过nursingLevelService.getNursingItemsByLevel(levelId)获取项目列表
        // 2. 为每个项目创建ClientNursingSetting
        // 3. 保存并返回
        return new ArrayList<>();
    }

    @Override
    public boolean consumeNursingService(Long id, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("消耗数量必须大于0");
        }

        Optional<ClientNursingSetting> settingOpt = clientNursingSettingDAO.findById(id);
        if (!settingOpt.isPresent()) {
            throw new RuntimeException("护理服务设置不存在，ID: " + id);
        }

        ClientNursingSetting setting = settingOpt.get();

        // 检查服务是否可用
        if (!isServiceAvailable(id)) {
            throw new RuntimeException("服务不可用，状态: " + setting.getServiceStatus());
        }

        // 检查剩余数量是否足够
        if (setting.getRemainingQuantity() < quantity) {
            throw new RuntimeException("剩余数量不足，当前剩余: " + setting.getRemainingQuantity());
        }

        // 消耗数量
        setting.setRemainingQuantity(setting.getRemainingQuantity() - quantity);

        // 检查并更新状态
        checkAndUpdateServiceStatus(setting);

        clientNursingSettingDAO.update(setting);
        return true;
    }

    @Override
    public ClientNursingSetting renewNursingService(Long id, Integer additionalQuantity, LocalDate newDueDate) {
        Optional<ClientNursingSetting> settingOpt = clientNursingSettingDAO.findById(id);
        if (!settingOpt.isPresent()) {
            throw new RuntimeException("护理服务设置不存在，ID: " + id);
        }

        ClientNursingSetting setting = settingOpt.get();

        if (additionalQuantity != null && additionalQuantity > 0) {
            // 增加总数量和剩余数量
            setting.setTotalQuantity(setting.getTotalQuantity() + additionalQuantity);
            setting.setRemainingQuantity(setting.getRemainingQuantity() + additionalQuantity);
        }

        if (newDueDate != null) {
            // 如果新到期日期早于当前日期，则设置为明天
            if (newDueDate.isBefore(LocalDate.now())) {
                newDueDate = LocalDate.now().plusDays(1);
            }
            setting.setServiceDueDate(newDueDate);
        } else {
            // 默认延长一个月
            LocalDate currentDueDate = setting.getServiceDueDate();
            if (currentDueDate == null) {
                currentDueDate = LocalDate.now();
            }
            setting.setServiceDueDate(currentDueDate.plusMonths(1));
        }

        // 更新状态为正常
        setting.setServiceStatus("正常");

        return clientNursingSettingDAO.update(setting);
    }

    @Override
    public List<ClientNursingSetting> getExpiringServices(LocalDate beforeDate) {
        List<ClientNursingSetting> allSettings = clientNursingSettingDAO.findAll().stream()
                .filter(setting -> (setting.getDeleted() == null || !setting.getDeleted()))
                .collect(Collectors.toList());

        return allSettings.stream()
                .filter(setting -> {
                    if (setting.getServiceDueDate() == null) {
                        return false;
                    }

                    // 检查是否即将到期（在beforeDate之前）
                    boolean isExpiring = setting.getServiceDueDate().isBefore(beforeDate);

                    // 检查是否还有剩余次数
                    boolean hasRemaining = setting.getRemainingQuantity() != null &&
                            setting.getRemainingQuantity() > 0;

                    return isExpiring && hasRemaining;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ClientNursingSetting> getServicesByStatus(String status) {
        return clientNursingSettingDAO.findAll().stream()
                .filter(setting -> status.equals(setting.getServiceStatus()) &&
                        (setting.getDeleted() == null || !setting.getDeleted()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isServiceAvailable(Long id) {
        Optional<ClientNursingSetting> settingOpt = clientNursingSettingDAO.findById(id);
        if (!settingOpt.isPresent()) {
            return false;
        }

        ClientNursingSetting setting = settingOpt.get();

        // 检查是否逻辑删除
        if (Boolean.TRUE.equals(setting.getDeleted())) {
            return false;
        }

        // 检查剩余数量
        if (setting.getRemainingQuantity() == null || setting.getRemainingQuantity() <= 0) {
            return false;
        }

        // 检查是否到期
        if (setting.getServiceDueDate() != null &&
                setting.getServiceDueDate().isBefore(LocalDate.now())) {
            return false;
        }

        // 检查服务状态
        return "正常".equals(setting.getServiceStatus()) ||
                "将到期".equals(setting.getServiceStatus());
    }

    @Override
    public boolean updateServiceStatus(Long id, String status) {
        Optional<ClientNursingSetting> settingOpt = clientNursingSettingDAO.findById(id);
        if (settingOpt.isPresent()) {
            ClientNursingSetting setting = settingOpt.get();
            setting.setServiceStatus(status);
            clientNursingSettingDAO.update(setting);
            return true;
        }
        return false;
    }

    // 检查并更新服务状态
    private void checkAndUpdateServiceStatus(ClientNursingSetting setting) {
        if (setting.getRemainingQuantity() == null || setting.getRemainingQuantity() <= 0) {
            setting.setServiceStatus("用完");
        } else if (setting.getServiceDueDate() != null &&
                setting.getServiceDueDate().isBefore(LocalDate.now())) {
            setting.setServiceStatus("到期");
        } else if ("欠费".equals(setting.getServiceStatus())) {
            // 保持欠费状态
        } else if ("正常".equals(setting.getServiceStatus()) ||
                "将到期".equals(setting.getServiceStatus())) {
            // 检查是否需要更新为"将到期"
            LocalDate warningDate = LocalDate.now().plusDays(7);
            if (setting.getServiceDueDate() != null &&
                    setting.getServiceDueDate().isBefore(warningDate)) {
                setting.setServiceStatus("将到期");
            } else {
                setting.setServiceStatus("正常");
            }
        }
    }
}