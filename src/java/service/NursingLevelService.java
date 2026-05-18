package java.service;

import java.entity.NursingLevel;
import java.util.List;
import java.util.Optional;

public interface NursingLevelService {

    // 添加护理级别
    NursingLevel addNursingLevel(NursingLevel nursingLevel);

    // 更新护理级别
    NursingLevel updateNursingLevel(Long id, NursingLevel nursingLevel);

    // 逻辑删除护理级别
    boolean deleteNursingLevel(Long id);

    // 根据ID查询护理级别
    Optional<NursingLevel> getNursingLevelById(Long id);

    // 查询所有护理级别
    List<NursingLevel> getAllNursingLevels();

    // 根据状态查询护理级别
    List<NursingLevel> getNursingLevelsByStatus(String status);

    // 根据级别名称查询
    Optional<NursingLevel> getNursingLevelByName(String levelName);

    // 更新护理级别状态
    boolean updateNursingLevelStatus(Long id, String status);

    // 为护理级别添加护理项目
    boolean addNursingItemToLevel(Long levelId, Long itemId);

    // 从护理级别移除护理项目
    boolean removeNursingItemFromLevel(Long levelId, Long itemId);

    // 获取护理级别的所有护理项目
    List<Long> getNursingItemsByLevel(Long levelId);
}