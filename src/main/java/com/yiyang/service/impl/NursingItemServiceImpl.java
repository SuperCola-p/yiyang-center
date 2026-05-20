package com.yiyang.service.impl;

import com.yiyang.entity.NursingItem;
import com.yiyang.repository.NursingItemRepository;
import com.yiyang.service.NursingItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class NursingItemServiceImpl implements NursingItemService {

    @Autowired
    private NursingItemRepository nursingItemRepository;

    @Override
    public NursingItem addNursingItem(NursingItem nursingItem) {
        // 验证编号唯一性
        Optional<NursingItem> existingItem = nursingItemRepository.findByCodeAndIsDeletedFalse(nursingItem.getCode());
        if (existingItem.isPresent()) {
            throw new RuntimeException("护理项目编号已存在: " + nursingItem.getCode());
        }

        // 设置默认值
        if (nursingItem.getStatus() == null) {
            nursingItem.setStatus("ENABLED");
        }

        if (nursingItem.getIsDeleted() == null) {
            nursingItem.setIsDeleted(false);
        }

        if (nursingItem.getExecQuantity() == null || nursingItem.getExecQuantity() <= 0) {
            nursingItem.setExecQuantity(1);
        }

        return nursingItemRepository.save(nursingItem);
    }

    @Override
    public NursingItem updateNursingItem(Long id, NursingItem nursingItem) {
        Optional<NursingItem> existingOpt = nursingItemRepository.findById(id);
        if (!existingOpt.isPresent()) {
            throw new RuntimeException("护理项目不存在，ID: " + id);
        }

        NursingItem existing = existingOpt.get();

        // 如果修改了编号，需要检查新编号是否被其他项目使用
        if (nursingItem.getCode() != null && !existing.getCode().equals(nursingItem.getCode())) {
            Optional<NursingItem> sameCodeItem = nursingItemRepository.findByCodeAndIsDeletedFalse(nursingItem.getCode());
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
            existing.setStatus(nursingItem.getStatus());
        }
        if (nursingItem.getExecPeriod() != null) {
            existing.setExecPeriod(nursingItem.getExecPeriod());
        }
        if (nursingItem.getExecQuantity() != null) {
            existing.setExecQuantity(nursingItem.getExecQuantity());
        }
        if (nursingItem.getDescription() != null) {
            existing.setDescription(nursingItem.getDescription());
        }

        return nursingItemRepository.save(existing);
    }

    @Override
    public boolean deleteNursingItem(Long id) {
        Optional<NursingItem> itemOpt = nursingItemRepository.findById(id);
        if (itemOpt.isPresent()) {
            NursingItem item = itemOpt.get();
            item.setIsDeleted(true);
            nursingItemRepository.save(item);
            return true;
        }
        return false;
    }

    @Override
    public Optional<NursingItem> getNursingItemById(Long id) {
        return nursingItemRepository.findById(id)
                .filter(item -> !Boolean.TRUE.equals(item.getIsDeleted()));
    }

    @Override
    public Optional<NursingItem> getNursingItemByCode(String code) {
        return nursingItemRepository.findByCodeAndIsDeletedFalse(code);
    }

    @Override
    public List<NursingItem> getAllNursingItems() {
        return nursingItemRepository.findByIsDeletedFalse();
    }

    @Override
    public List<NursingItem> getNursingItemsByStatus(String status) {
        return nursingItemRepository.findByStatusAndIsDeletedFalse(status);
    }

    @Override
    public List<NursingItem> searchNursingItemsByName(String name) {
        return nursingItemRepository.findByNameContainingAndIsDeletedFalse(name);
    }

    @Override
    public boolean updateNursingItemStatus(Long id, String status) {
        Optional<NursingItem> itemOpt = nursingItemRepository.findById(id);
        if (itemOpt.isPresent()) {
            NursingItem item = itemOpt.get();
            item.setStatus(status);
            nursingItemRepository.save(item);
            return true;
        }
        return false;
    }

    @Override
    public List<NursingItem> getNursingItemsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return nursingItemRepository.findByIsDeletedFalse().stream()
                .filter(item -> item.getPrice() != null)
                .filter(item -> item.getPrice().compareTo(minPrice) >= 0 &&
                        item.getPrice().compareTo(maxPrice) <= 0)
                .collect(Collectors.toList());
    }

}
