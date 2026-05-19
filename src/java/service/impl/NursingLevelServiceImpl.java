package java.service.impl;

import java.entity.NursingItem;
import java.entity.NursingLevel;
import java.DAO.NursingLevelDAO;
import java.service.NursingLevelService;
import java.service.NursingItemService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NursingLevelServiceImpl implements NursingLevelService {

    private final NursingLevelDAO nursingLevelDAO = NursingLevelDAO.getInstance();
    private final NursingItemService nursingItemService = new NursingItemServiceImpl();

    @Override
    public NursingLevel addNursingLevel(NursingLevel nursingLevel) {
        // 验证级别名称唯一性
        Optional<NursingLevel> existingLevel = nursingLevelDAO.findByLevelName(nursingLevel.getLevelName());
        if (existingLevel.isPresent()) {
            throw new RuntimeException("护理级别名称已存在: " + nursingLevel.getLevelName());
        }

        // 设置默认值
        if (nursingLevel.getStatus() == null) {
            nursingLevel.setStatus("启用");
        }

        if (nursingLevel.getNursingItems() == null) {
            nursingLevel.setNursingItems(new java.entity.NursingItemArray());
        }

        return nursingLevelDAO.save(nursingLevel);
    }

    @Override
    public NursingLevel updateNursingLevel(Long id, NursingLevel nursingLevel) {
        // 先查找是否存在
        Optional<NursingLevel> existingOpt = nursingLevelDAO.findById(id);
        if (!existingOpt.isPresent()) {
            throw new RuntimeException("护理级别不存在，ID: " + id);
        }

        NursingLevel existing = existingOpt.get();

        // 如果修改了级别名称，需要检查新名称是否被其他级别使用
        if (nursingLevel.getLevelName() != null &&
                !existing.getLevelName().equals(nursingLevel.getLevelName())) {
            Optional<NursingLevel> sameNameLevel = nursingLevelDAO.findByLevelName(nursingLevel.getLevelName());
            if (sameNameLevel.isPresent()) {
                throw new RuntimeException("护理级别名称已被使用: " + nursingLevel.getLevelName());
            }
        }

        // 更新字段
        if (nursingLevel.getLevelName() != null) {
            existing.setLevelName(nursingLevel.getLevelName());
        }

        if (nursingLevel.getStatus() != null) {
            existing.setStatus(nursingLevel.getStatus());
        }

        if (nursingLevel.getNursingItems() != null) {
            existing.setNursingItems(nursingLevel.getNursingItems());
        }

        return nursingLevelDAO.update(existing);
    }

    @Override
    public boolean deleteNursingLevel(Long id) {
        return nursingLevelDAO.softDelete(id);
    }

    @Override
    public Optional<NursingLevel> getNursingLevelById(Long id) {
        return nursingLevelDAO.findById(id);
    }

    @Override
    public List<NursingLevel> getAllNursingLevels() {
        return nursingLevelDAO.findAll();
    }

    @Override
    public List<NursingLevel> getNursingLevelsByStatus(String status) {
        return nursingLevelDAO.findByStatus(status);
    }

    @Override
    public Optional<NursingLevel> getNursingLevelByName(String levelName) {
        return nursingLevelDAO.findByLevelName(levelName);
    }

    @Override
    public boolean updateNursingLevelStatus(Long id, String status) {
        Optional<NursingLevel> levelOpt = nursingLevelDAO.findById(id);
        if (levelOpt.isPresent()) {
            NursingLevel level = levelOpt.get();
            level.setStatus(status);
            nursingLevelDAO.update(level);
            return true;
        }
        return false;
    }

    @Override
    public boolean addNursingItemToLevel(Long levelId, Long itemId) {
        Optional<NursingLevel> levelOpt = nursingLevelDAO.findById(levelId);
        Optional<NursingItem> itemOpt = nursingItemService.getNursingItemById(itemId);

        if (!levelOpt.isPresent()) {
            throw new RuntimeException("护理级别不存在，ID: " + levelId);
        }

        if (!itemOpt.isPresent()) {
            throw new RuntimeException("护理项目不存在，ID: " + itemId);
        }

        NursingLevel level = levelOpt.get();
        NursingItem item = itemOpt.get();

        // 检查项目是否已停用
        if ("停用".equals(item.getStatus())) {
            throw new RuntimeException("无法添加已停用的护理项目: " + item.getName());
        }

        // 检查项目是否已存在
        java.entity.NursingItemArray items = level.getNursingItems();
        if (items.findItem(item)) {
            throw new RuntimeException("护理项目已存在于该级别中: " + item.getName());
        }

        items.addItem(item);
        level.setNursingItems(items);

        nursingLevelDAO.update(level);
        return true;
    }

    @Override
    public boolean removeNursingItemFromLevel(Long levelId, Long itemId) {
        Optional<NursingLevel> levelOpt = nursingLevelDAO.findById(levelId);
        Optional<NursingItem> itemOpt = nursingItemService.getNursingItemById(itemId);

        if (!levelOpt.isPresent()) {
            throw new RuntimeException("护理级别不存在，ID: " + levelId);
        }

        if (!itemOpt.isPresent()) {
            throw new RuntimeException("护理项目不存在，ID: " + itemId);
        }

        NursingLevel level = levelOpt.get();
        NursingItem item = itemOpt.get();

        java.entity.NursingItemArray items = level.getNursingItems();
        items.deleteItem(item);
        level.setNursingItems(items);

        nursingLevelDAO.update(level);
        return true;
    }

    @Override
    public List<Long> getNursingItemsByLevel(Long levelId) {
        Optional<NursingLevel> levelOpt = nursingLevelDAO.findById(levelId);
        if (levelOpt.isPresent()) {
            NursingLevel level = levelOpt.get();
            java.entity.NursingItemArray items = level.getNursingItems();

            // 提取项目ID列表
            List<Long> itemIds = new ArrayList<>();
            for (NursingItem item : items) {
                itemIds.add(item.getId());
            }
            return itemIds;
        }
        return new ArrayList<>();
    }

    // 批量添加护理项目到级别
    public boolean addNursingItemsToLevel(Long levelId, List<Long> itemIds) {
        for (Long itemId : itemIds) {
            if (!addNursingItemToLevel(levelId, itemId)) {
                return false;
            }
        }
        return true;
    }
}