package com.yiyang.service.impl;

import com.yiyang.entity.NursingLevel;
import com.yiyang.entity.NursingLevelItem;
import com.yiyang.repository.NursingLevelItemRepository;
import com.yiyang.repository.NursingLevelRepository;
import com.yiyang.service.NursingLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NursingLevelServiceImpl implements NursingLevelService {

    @Autowired
    private NursingLevelRepository nursingLevelRepository;

    @Autowired
    private NursingLevelItemRepository nursingLevelItemRepository;

    // ==================== 护理等级 CRUD ====================

    @Override
    public NursingLevel addNursingLevel(NursingLevel nursingLevel) {
        // 验证等级名称唯一性
        Optional<NursingLevel> existing = nursingLevelRepository.findByLevelNameAndDeletedFalse(nursingLevel.getLevelName());
        if (existing.isPresent()) {
            throw new RuntimeException("护理等级名称已存在: " + nursingLevel.getLevelName());
        }

        // 设置默认值
        if (nursingLevel.getStatus() == null) {
            nursingLevel.setStatus("ENABLED");
        }
        if (nursingLevel.getDeleted() == null) {
            nursingLevel.setDeleted(false);
        }

        return nursingLevelRepository.save(nursingLevel);
    }

    @Override
    public NursingLevel updateNursingLevel(Long id, NursingLevel nursingLevel) {
        NursingLevel existing = nursingLevelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("护理等级不存在，ID: " + id));

        if (nursingLevel.getLevelName() != null && !existing.getLevelName().equals(nursingLevel.getLevelName())) {
            Optional<NursingLevel> sameName = nursingLevelRepository.findByLevelNameAndDeletedFalse(nursingLevel.getLevelName());
            if (sameName.isPresent()) {
                throw new RuntimeException("护理等级名称已被使用: " + nursingLevel.getLevelName());
            }
            existing.setLevelName(nursingLevel.getLevelName());
        }

        if (nursingLevel.getStatus() != null) {
            existing.setStatus(nursingLevel.getStatus());
        }

        return nursingLevelRepository.save(existing);
    }

    @Override
    public boolean deleteNursingLevel(Long id) {
        Optional<NursingLevel> itemOpt = nursingLevelRepository.findById(id);
        if (itemOpt.isPresent()) {
            NursingLevel level = itemOpt.get();
            level.setDeleted(true);
            nursingLevelRepository.save(level);
            return true;
        }
        return false;
    }

    @Override
    public Optional<NursingLevel> getNursingLevelById(Long id) {
        return nursingLevelRepository.findById(id)
                .filter(item -> !Boolean.TRUE.equals(item.getDeleted()));
    }

    @Override
    public Optional<NursingLevel> getNursingLevelByName(String levelName) {
        return nursingLevelRepository.findByLevelNameAndDeletedFalse(levelName);
    }

    @Override
    public List<NursingLevel> getAllNursingLevels() {
        return nursingLevelRepository.findByDeletedFalse();
    }

    @Override
    public List<NursingLevel> getNursingLevelsByStatus(String status) {
        return nursingLevelRepository.findByStatusAndDeletedFalse(status);
    }

    @Override
    public boolean updateNursingLevelStatus(Long id, String status) {
        Optional<NursingLevel> itemOpt = nursingLevelRepository.findById(id);
        if (itemOpt.isPresent()) {
            NursingLevel level = itemOpt.get();
            level.setStatus(status);
            nursingLevelRepository.save(level);
            return true;
        }
        return false;
    }

    // ==================== 等级-项目关联管理 ====================

    @Override
    public List<NursingLevelItem> getItemsByLevelId(Long levelId) {
        return nursingLevelItemRepository.findByLevelId(levelId);
    }

    @Override
    public List<NursingLevelItem> getLevelsByItemId(Long itemId) {
        return nursingLevelItemRepository.findByItemId(itemId);
    }

    @Override
    public boolean addItemToLevel(Long levelId, Long itemId) {
        Optional<NursingLevelItem> existing = nursingLevelItemRepository.findByLevelIdAndItemId(levelId, itemId);
        if (existing.isPresent()) {
            return false; // 已存在关联
        }
        NursingLevelItem relation = NursingLevelItem.builder()
                .levelId(levelId)
                .itemId(itemId)
                .build();
        nursingLevelItemRepository.save(relation);
        return true;
    }

    @Override
    public void addItemsToLevel(Long levelId, List<Long> itemIds) {
        for (Long itemId : itemIds) {
            addItemToLevel(levelId, itemId);
        }
    }

    @Override
    public boolean removeItemFromLevel(Long levelId, Long itemId) {
        Optional<NursingLevelItem> existing = nursingLevelItemRepository.findByLevelIdAndItemId(levelId, itemId);
        if (existing.isPresent()) {
            nursingLevelItemRepository.delete(existing.get());
            return true;
        }
        return false;
    }

    @Override
    public void removeItemsFromLevel(Long levelId, List<Long> itemIds) {
        for (Long itemId : itemIds) {
            nursingLevelItemRepository.deleteByLevelIdAndItemId(levelId, itemId);
        }
    }
}
