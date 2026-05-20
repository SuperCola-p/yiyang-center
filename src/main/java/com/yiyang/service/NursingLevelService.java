package com.yiyang.service;

import com.yiyang.entity.NursingLevel;
import com.yiyang.entity.NursingLevelItem;

import java.util.List;
import java.util.Optional;

public interface NursingLevelService {

    // ==================== 护理等级 CRUD ====================

    NursingLevel addNursingLevel(NursingLevel nursingLevel);

    NursingLevel updateNursingLevel(Long id, NursingLevel nursingLevel);

    boolean deleteNursingLevel(Long id);

    Optional<NursingLevel> getNursingLevelById(Long id);

    Optional<NursingLevel> getNursingLevelByName(String levelName);

    List<NursingLevel> getAllNursingLevels();

    List<NursingLevel> getNursingLevelsByStatus(String status);

    boolean updateNursingLevelStatus(Long id, String status);

    // ==================== 等级-项目关联管理 ====================

    List<NursingLevelItem> getItemsByLevelId(Long levelId);

    List<NursingLevelItem> getLevelsByItemId(Long itemId);

    boolean addItemToLevel(Long levelId, Long itemId);

    void addItemsToLevel(Long levelId, List<Long> itemIds);

    boolean removeItemFromLevel(Long levelId, Long itemId);

    void removeItemsFromLevel(Long levelId, List<Long> itemIds);
}
