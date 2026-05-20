package com.yiyang.service;

import com.yiyang.entity.NursingRecord;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NursingRecordService {

    // ==================== 记录管理 ====================

    NursingRecord addRecord(NursingRecord record);

    boolean deleteRecord(Long id);

    Optional<NursingRecord> getRecordById(Long id);

    List<NursingRecord> getAllRecords();

    // ==================== 查询 ====================

    List<NursingRecord> getRecordsByClient(Long clientId);

    List<NursingRecord> getRecordsByHealthAssistant(Long healthAssistantId);

    List<NursingRecord> getRecordsByNursingItem(Long nursingItemId);

    List<NursingRecord> getRecordsByDateRange(LocalDateTime start, LocalDateTime end);

    List<NursingRecord> getRecordsByClientAndDateRange(Long clientId, LocalDateTime start, LocalDateTime end);

    long getRecordCountByClient(Long clientId);

    long getRecordCountByHealthAssistant(Long healthAssistantId);
}
