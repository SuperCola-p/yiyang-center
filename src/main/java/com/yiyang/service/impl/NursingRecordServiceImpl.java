package com.yiyang.service.impl;

import com.yiyang.entity.NursingRecord;
import com.yiyang.repository.NursingRecordRepository;
import com.yiyang.service.NursingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NursingRecordServiceImpl implements NursingRecordService {

    @Autowired
    private NursingRecordRepository repository;

    @Override
    public NursingRecord addRecord(NursingRecord record) {
        if (record.getIsDeleted() == null) {
            record.setIsDeleted(false);
        }
        if (record.getExecQuantity() == null || record.getExecQuantity() <= 0) {
            record.setExecQuantity(1);
        }
        if (record.getNursingTime() == null) {
            record.setNursingTime(LocalDateTime.now());
        }
        return repository.save(record);
    }

    @Override
    public boolean deleteRecord(Long id) {
        Optional<NursingRecord> opt = repository.findById(id);
        if (opt.isPresent()) {
            NursingRecord record = opt.get();
            record.setIsDeleted(true);
            repository.save(record);
            return true;
        }
        return false;
    }

    @Override
    public Optional<NursingRecord> getRecordById(Long id) {
        return repository.findById(id)
                .filter(r -> !Boolean.TRUE.equals(r.getIsDeleted()));
    }

    @Override
    public List<NursingRecord> getAllRecords() {
        return repository.findByDateRange(
                LocalDateTime.of(2000, 1, 1, 0, 0),
                LocalDateTime.of(2099, 12, 31, 23, 59));
    }

    @Override
    public List<NursingRecord> getRecordsByClient(Long clientId) {
        return repository.findByClientIdAndIsDeletedFalseOrderByNursingTimeDesc(clientId);
    }

    @Override
    public List<NursingRecord> getRecordsByHealthAssistant(Long healthAssistantId) {
        return repository.findByHealthAssistantIdAndIsDeletedFalse(healthAssistantId);
    }

    @Override
    public List<NursingRecord> getRecordsByNursingItem(Long nursingItemId) {
        return repository.findByNursingItemIdAndIsDeletedFalse(nursingItemId);
    }

    @Override
    public List<NursingRecord> getRecordsByDateRange(LocalDateTime start, LocalDateTime end) {
        return repository.findByDateRange(start, end);
    }

    @Override
    public List<NursingRecord> getRecordsByClientAndDateRange(Long clientId, LocalDateTime start, LocalDateTime end) {
        return repository.findByClientIdAndDateRange(clientId, start, end);
    }

    @Override
    public long getRecordCountByClient(Long clientId) {
        return repository.countByClientIdAndIsDeletedFalse(clientId);
    }

    @Override
    public long getRecordCountByHealthAssistant(Long healthAssistantId) {
        return repository.countByHealthAssistantIdAndIsDeletedFalse(healthAssistantId);
    }
}
