// NursingRecordService.java
package java.service;

import java.entity.NursingRecord;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NursingRecordService {

    // 记录护理执行
    NursingRecord recordNursing(NursingRecord nursingRecord);

    // 更新护理记录
    NursingRecord updateNursingRecord(Long id, NursingRecord nursingRecord);

    // 逻辑删除护理记录
    boolean deleteNursingRecord(Long id);

    // 根据ID查询护理记录
    Optional<NursingRecord> getNursingRecordById(Long id);

    // 根据客户ID查询护理记录
    List<NursingRecord> getNursingRecordsByClientId(Long clientId);

    // 根据健康管家ID查询护理记录
    List<NursingRecord> getNursingRecordsByAssistantId(Long assistantId);

    // 根据客户ID和时间范围查询护理记录
    List<NursingRecord> getNursingRecordsByClientAndDateRange(Long clientId, LocalDate startDate, LocalDate endDate);

    // 根据护理项目ID查询护理记录
    List<NursingRecord> getNursingRecordsByItemId(Long nursingItemId);

    // 查询时间段内的所有护理记录
    List<NursingRecord> getNursingRecordsByDateRange(LocalDate startDate, LocalDate endDate);

    // 获取客户最近的护理记录
    List<NursingRecord> getRecentNursingRecordsByClientId(Long clientId, int limit);

    // 统计客户护理记录数量
    int countNursingRecordsByClientId(Long clientId);

    // 统计健康管家护理记录数量
    int countNursingRecordsByAssistantId(Long assistantId);

    // 检查护理记录是否存在
    boolean existsNursingRecord(Long clientId, Long nursingItemId, LocalDateTime nursingTime);
}