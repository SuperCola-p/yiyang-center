// NursingRecordDAO.java
package java.DAO;

import java.entity.NursingRecord;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class NursingRecordDAO extends BaseFileDAO<NursingRecord, Long> {

    private static NursingRecordDAO instance;

    private NursingRecordDAO() {
        super(NursingRecord.class);
    }

    public static synchronized NursingRecordDAO getInstance() {
        if (instance == null) {
            instance = new NursingRecordDAO();
        }
        return instance;
    }

    @Override
    protected Long getId(NursingRecord entity) {
        return entity.getId();
    }

    @Override
    protected void setId(NursingRecord entity, Long id) {
        entity.setId(id);
    }

    public List<NursingRecord> findByClientId(Long clientId) {
        return findAll().stream()
                .filter(record -> clientId.equals(record.getClientId()) &&
                        (record.getDeleted() == null || !record.getDeleted()))
                .collect(Collectors.toList());
    }

    public List<NursingRecord> findByAssistantId(Long assistantId) {
        return findAll().stream()
                .filter(record -> assistantId.equals(record.getHealthAssistantId()) &&
                        (record.getDeleted() == null || !record.getDeleted()))
                .collect(Collectors.toList());
    }
}