package java.service.impl;

import java.entity.NursingItem;
import java.DAO.NursingItemDAO;
import java.service.NursingItemService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class NursingItemServiceImpl implements NursingItemService {

    private final NursingItemDAO nursingItemDAO = NursingItemDAO.getInstance();

    @Override
    public NursingItem addNursingItem(NursingItem nursingItem) {
        // 验证编号唯一性
        Optional<NursingItem> existingItem = nursingItemDAO.findByCode(nursingItem.getCode());
        if (existingItem.isPresent()) {
            throw new RuntimeException("护理项目编号已存在: " + nursingItem.getCode());
        }

        // 设置默认值
        if (nursingItem.getStatus() == null) {
            nursingItem.setStatus("启用");
        }

        if (nursingItem.getDeleted() == null) {
            nursingItem.setDeleted(false);
        }

        return nursingItemDAO.save(nursingItem);
    }

    @Override
    public NursingItem updateNursingItem(Long id, NursingItem nursingItem) {
        // 先查找是否存在
        Optional<NursingItem> existingOpt = nursingItemDAO.findById(id);
        if (!existingOpt.isPresent()) {
            throw new RuntimeException("护理项目不存在，ID: " + id);
        }

        NursingItem existing = existingOpt.get();

        // 如果修改了编号，需要检查新编号是否被其他项目使用
        if (nursingItem.getCode() != null && !existing.getCode().equals(nursingItem.getCode())) {
            Optional<NursingItem> sameCodeItem = nursingItemDAO.findByCode(nursingItem.getCode());
            if (sameCodeItem.isPresent()) {
                throw new RuntimeException("护理项目编号已被使用: " + nursingItem.getCode());
            }
        }

        // 更新字段
        if (nursingItem.getCode() != null) {
            existing.setCode(nursingItem.getCode());
        }

        if (nursingItem.getName() != null) {
            existing.setName(nursingItem.getName());
        }

        if (nursingItem.getPrice() != null) {
            existing.setPrice(nursingItem.getPrice());
        }

        if (nursingItem.getStatus() != null) {
            // 如果状态改为停用，需要处理相关逻辑
            if ("停用".equals(nursingItem.getStatus()) && !"停用".equals(existing.getStatus())) {
                handleItemDisable(id);
            }
            existing.setStatus(nursingItem.getStatus());
        }

        if (nursingItem.getExecPeriod() != null) {
            existing.setExecPeriod(nursingItem.getExecPeriod());
        }

        if (nursingItem.getExecTimes() != null) {
            existing.setExecTimes(nursingItem.getExecTimes());
        }

        if (nursingItem.getDescription() != null) {
            existing.setDescription(nursingItem.getDescription());
        }

        return nursingItemDAO.update(existing);
    }

    @Override
    public boolean deleteNursingItem(Long id) {
        return nursingItemDAO.softDelete(id);
    }

    @Override
    public Optional<NursingItem> getNursingItemById(Long id) {
        return nursingItemDAO.findById(id);
    }

    @Override
    public Optional<NursingItem> getNursingItemByCode(String code) {
        return nursingItemDAO.findByCode(code);
    }

    @Override
    public List<NursingItem> getAllNursingItems() {
        return nursingItemDAO.findAll();
    }

    @Override
    public List<NursingItem> getNursingItemsByStatus(String status) {
        return nursingItemDAO.findByStatus(status);
    }

    @Override
    public List<NursingItem> searchNursingItemsByName(String name) {
        return nursingItemDAO.findByNameLike(name);
    }

    @Override
    public boolean updateNursingItemStatus(Long id, String status) {
        Optional<NursingItem> itemOpt = nursingItemDAO.findById(id);
        if (itemOpt.isPresent()) {
            NursingItem item = itemOpt.get();
            item.setStatus(status);

            // 如果状态改为停用，需要处理相关逻辑
            if ("停用".equals(status) && !"停用".equals(item.getStatus())) {
                handleItemDisable(id);
            }

            nursingItemDAO.update(item);
            return true;
        }
        return false;
    }

    @Override
    public List<NursingItem> getNursingItemsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return nursingItemDAO.findByCondition(item -> {
            if (item.getPrice() == null) return false;
            return item.getPrice().compareTo(minPrice) >= 0 &&
                    item.getPrice().compareTo(maxPrice) <= 0;
        });
    }

    // 停用护理项目时的额外处理
    private void handleItemDisable(Long itemId) {
        // 这里可以添加停用护理项目时的额外逻辑
        // 例如：从所有护理级别中移除该项目
        // 这个逻辑需要调用NursingLevelService来实现
        // 目前先保留为空，后续可以扩展
    }
}