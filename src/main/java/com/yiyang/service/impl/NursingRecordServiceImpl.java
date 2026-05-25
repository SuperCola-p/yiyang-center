package com.yiyang.service.impl;

import com.yiyang.entity.Client;
import com.yiyang.entity.NursingItem;
import com.yiyang.entity.NursingRecord;
import com.yiyang.entity.Operator;
import com.yiyang.repository.ClientRepository;
import com.yiyang.repository.NursingItemRepository;
import com.yiyang.repository.NursingRecordRepository;
import com.yiyang.repository.OperatorRepository;
import com.yiyang.service.NursingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class NursingRecordServiceImpl implements NursingRecordService {

    @Autowired
    private NursingRecordRepository repository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private NursingItemRepository nursingItemRepository;

    @Autowired
    private OperatorRepository operatorRepository;

    /**
     * 填充 NursingRecord 的 transient 关联字段（clientName, nursingItemName, healthAssistantName）
     */
    private NursingRecord enrichRecord(NursingRecord record) {
        if (record == null) return null;

        // 填充 clientName
        if (record.getClientId() != null) {
            Optional<Client> clientOpt = clientRepository.findByIdAndDeletedFalse(
                    record.getClientId().intValue());
            clientOpt.ifPresent(c -> record.setClientName(c.getName()));
        }

        // 填充 nursingItemName
        if (record.getNursingItemId() != null) {
            Optional<NursingItem> itemOpt = nursingItemRepository.findById(
                    record.getNursingItemId());
            itemOpt.ifPresent(item -> record.setNursingItemName(item.getName()));
        }

        // 填充 healthAssistantName
        if (record.getHealthAssistantId() != null) {
            try {
                List<Operator> operators = operatorRepository.findAll();
                String hid = String.valueOf(record.getHealthAssistantId());
                for (Operator op : operators) {
                    if (hid.equals(op.getLoginCode()) || hid.equals(op.getRealName())) {
                        record.setHealthAssistantName(op.getRealName());
                        break;
                    }
                }
                if (record.getHealthAssistantName() == null) {
                    record.setHealthAssistantName(hid);
                }
            } catch (Exception e) {
                record.setHealthAssistantName(String.valueOf(record.getHealthAssistantId()));
            }
        }

        return record;
    }

    /**
     * 批量填充关联字段
     */
    private List<NursingRecord> enrichRecords(List<NursingRecord> records) {
        if (records == null) return null;
        return records.stream()
                .map(this::enrichRecord)
                .collect(Collectors.toList());
    }

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
        NursingRecord saved = repository.save(record);
        return enrichRecord(saved);
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
                .filter(r -> !Boolean.TRUE.equals(r.getIsDeleted()))
                .map(this::enrichRecord);
    }

    @Override
    public List<NursingRecord> getAllRecords() {
        List<NursingRecord> records = repository.findByDateRange(
                LocalDateTime.of(2000, 1, 1, 0, 0),
                LocalDateTime.of(2099, 12, 31, 23, 59));
        return enrichRecords(records);
    }

    @Override
    public List<NursingRecord> getRecordsByClient(Long clientId) {
        List<NursingRecord> records = repository.findByClientIdAndIsDeletedFalseOrderByNursingTimeDesc(clientId);
        return enrichRecords(records);
    }

    @Override
    public List<NursingRecord> getRecordsByHealthAssistant(Long healthAssistantId) {
        List<NursingRecord> records = repository.findByHealthAssistantIdAndIsDeletedFalse(healthAssistantId);
        return enrichRecords(records);
    }

    @Override
    public List<NursingRecord> getRecordsByNursingItem(Long nursingItemId) {
        List<NursingRecord> records = repository.findByNursingItemIdAndIsDeletedFalse(nursingItemId);
        return enrichRecords(records);
    }

    @Override
    public List<NursingRecord> getRecordsByDateRange(LocalDateTime start, LocalDateTime end) {
        List<NursingRecord> records = repository.findByDateRange(start, end);
        return enrichRecords(records);
    }

    @Override
    public List<NursingRecord> getRecordsByClientAndDateRange(Long clientId, LocalDateTime start, LocalDateTime end) {
        List<NursingRecord> records = repository.findByClientIdAndDateRange(clientId, start, end);
        return enrichRecords(records);
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