package java.service.impl;

import java.entity.NursingRecord;
import java.DAO.NursingRecordDAO;
import java.service.NursingRecordService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class NursingRecordServiceImpl implements NursingRecordService {

    private final NursingRecordDAO nursingRecordDAO = NursingRecordDAO.getInstance();

    @Override
    public NursingRecord recordNursing(NursingRecord nursingRecord) {
        // 验证必填字段
        if (nursingRecord.getClientId() == null) {
            throw new RuntimeException("客户ID不能为空");
        }

        if (nursingRecord.getNursingItemId() == null) {
            throw new RuntimeException("护理项目ID不能为空");
        }

        if (nursingRecord.getHealthAssistantId() == null) {
            throw new RuntimeException("健康管家ID不能为空");
        }

        // 设置默认值
        if (nursingRecord.getNursingTime() == null) {
            nursingRecord.setNursingTime(LocalDateTime.now());
        }

        if (nursingRecord.getExecQuantity() == null || nursingRecord.getExecQuantity() <= 0) {
            nursingRecord.setExecQuantity(1);
        }

        if (nursingRecord.getDeleted() == null) {
            nursingRecord.setDeleted(false);
        }

        return nursingRecordDAO.save(nursingRecord);
    }

    @Override
    public NursingRecord updateNursingRecord(Long id, NursingRecord nursingRecord) {
        // 先查找是否存在
        Optional<NursingRecord> existingOpt = nursingRecordDAO.findById(id);
        if (!existingOpt.isPresent()) {
            throw new RuntimeException("护理记录不存在，ID: " + id);
        }

        NursingRecord existing = existingOpt.get();

        // 护理记录一般只允许更新部分字段
        if (nursingRecord.getExecQuantity() != null && nursingRecord.getExecQuantity() > 0) {
            existing.setExecQuantity(nursingRecord.getExecQuantity());
        }

        if (nursingRecord.getNursingTime() != null) {
            existing.setNursingTime(nursingRecord.getNursingTime());
        }

        return nursingRecordDAO.update(existing);
    }

    @Override
    public boolean deleteNursingRecord(Long id) {
        return nursingRecordDAO.softDelete(id);
    }

    @Override
    public Optional<NursingRecord> getNursingRecordById(Long id) {
        return nursingRecordDAO.findById(id);
    }

    @Override
    public List<NursingRecord> getNursingRecordsByClientId(Long clientId) {
        return nursingRecordDAO.findAll().stream()
                .filter(record -> clientId.equals(record.getClientId()) &&
                        (record.getDeleted() == null || !record.getDeleted()))
                .collect(Collectors.toList());
    }

    @Override
    public List<NursingRecord> getNursingRecordsByAssistantId(Long assistantId) {
        return nursingRecordDAO.findAll().stream()
                .filter(record -> assistantId.equals(record.getHealthAssistantId()) &&
                        (record.getDeleted() == null || !record.getDeleted()))
                .collect(Collectors.toList());
    }

    @Override
    public List<NursingRecord> getNursingRecordsByClientAndDateRange(Long clientId, LocalDate startDate, LocalDate endDate) {
        return nursingRecordDAO.findAll().stream()
                .filter(record -> {
                    if (!clientId.equals(record.getClientId())) {
                        return false;
                    }

                    LocalDateTime nursingTime = record.getNursingTime();
                    if (nursingTime == null) {
                        return false;
                    }

                    LocalDate recordDate = nursingTime.toLocalDate();
                    return !recordDate.isBefore(startDate) && !recordDate.isAfter(endDate) &&
                            (record.getDeleted() == null || !record.getDeleted());
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<NursingRecord> getNursingRecordsByItemId(Long nursingItemId) {
        return nursingRecordDAO.findAll().stream()
                .filter(record -> nursingItemId.equals(record.getNursingItemId()) &&
                        (record.getDeleted() == null || !record.getDeleted()))
                .collect(Collectors.toList());
    }

    @Override
    public List<NursingRecord> getNursingRecordsByDateRange(LocalDate startDate, LocalDate endDate) {
        return nursingRecordDAO.findAll().stream()
                .filter(record -> {
                    LocalDateTime nursingTime = record.getNursingTime();
                    if (nursingTime == null) {
                        return false;
                    }

                    LocalDate recordDate = nursingTime.toLocalDate();
                    return !recordDate.isBefore(startDate) && !recordDate.isAfter(endDate) &&
                            (record.getDeleted() == null || !record.getDeleted());
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<NursingRecord> getRecentNursingRecordsByClientId(Long clientId, int limit) {
        List<NursingRecord> allRecords = getNursingRecordsByClientId(clientId);

        // 按时间倒序排序
        allRecords.sort((r1, r2) -> {
            if (r1.getNursingTime() == null && r2.getNursingTime() == null) return 0;
            if (r1.getNursingTime() == null) return 1;
            if (r2.getNursingTime() == null) return -1;
            return r2.getNursingTime().compareTo(r1.getNursingTime());
        });

        // 限制数量
        if (allRecords.size() > limit) {
            return allRecords.subList(0, limit);
        }
        return allRecords;
    }

    @Override
    public int countNursingRecordsByClientId(Long clientId) {
        return (int) getNursingRecordsByClientId(clientId).stream().count();
    }

    @Override
    public int countNursingRecordsByAssistantId(Long assistantId) {
        return (int) getNursingRecordsByAssistantId(assistantId).stream().count();
    }

    @Override
    public boolean existsNursingRecord(Long clientId, Long nursingItemId, LocalDateTime nursingTime) {
        return nursingRecordDAO.findAll().stream()
                .anyMatch(record -> clientId.equals(record.getClientId()) &&
                        nursingItemId.equals(record.getNursingItemId()) &&
                        nursingTime.equals(record.getNursingTime()) &&
                        (record.getDeleted() == null || !record.getDeleted()));
    }
}