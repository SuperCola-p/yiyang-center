package com.yiyang.service;

import com.yiyang.entity.NursingItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface NursingItemService {

    // 添加护理项目
    NursingItem addNursingItem(NursingItem nursingItem);

    // 更新护理项目
    NursingItem updateNursingItem(Long id, NursingItem nursingItem);

    // 逻辑删除护理项目
    boolean deleteNursingItem(Long id);

    // 根据ID查询护理项目
    Optional<NursingItem> getNursingItemById(Long id);

    // 根据编号查询护理项目
    Optional<NursingItem> getNursingItemByCode(String code);

    // 查询所有护理项目（未删除）
    List<NursingItem> getAllNursingItems();

    // 根据状态查询护理项目
    List<NursingItem> getNursingItemsByStatus(String status);

    // 根据名称模糊查询
    List<NursingItem> searchNursingItemsByName(String name);

    // 更新护理项目状态（启用/停用）
    boolean updateNursingItemStatus(Long id, String status);

    // 根据价格范围查询
    List<NursingItem> getNursingItemsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
}
