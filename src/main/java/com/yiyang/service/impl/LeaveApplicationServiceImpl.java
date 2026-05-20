package com.yiyang.service.impl;

import com.yiyang.entity.LeaveApplication;
import com.yiyang.repository.LeaveApplicationRepository;
import com.yiyang.service.LeaveApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class LeaveApplicationServiceImpl implements LeaveApplicationService {

    @Autowired
    private LeaveApplicationRepository repository;

    @Override
    public LeaveApplication createApplication(LeaveApplication application) {
        if (application.getId() == null || application.getId().trim().isEmpty()) {
            application.setId("LV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        if (application.getStatus() == null) {
            application.setStatus("已提交");
        }
        if (application.getDeleted() == null) {
            application.setDeleted(false);
        }
        return repository.save(application);
    }

    @Override
    public LeaveApplication auditApplication(String id, String status, String auditor) {
        LeaveApplication app = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("请假申请不存在，ID: " + id));

        app.setStatus(status);
        app.setAuditor(auditor);
        app.setAuditTime(LocalDateTime.now());
        return repository.save(app);
    }

    @Override
    public LeaveApplication recordReturn(String id, LocalDateTime actualReturnTime) {
        LeaveApplication app = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("请假申请不存在，ID: " + id));

        app.setActualReturnTime(actualReturnTime);
        app.setStatus("已销假");
        return repository.save(app);
    }

    @Override
    public Optional<LeaveApplication> getApplicationById(String id) {
        return repository.findById(id)
                .filter(a -> !Boolean.TRUE.equals(a.getDeleted()));
    }

    @Override
    public List<LeaveApplication> getAllApplications() {
        return repository.findByDeletedFalse();
    }

    @Override
    public List<LeaveApplication> getApplicationsByStatus(String status) {
        return repository.findByStatusAndDeletedFalse(status);
    }

    @Override
    public boolean deleteApplication(String id) {
        Optional<LeaveApplication> opt = repository.findById(id);
        if (opt.isPresent()) {
            LeaveApplication app = opt.get();
            app.setDeleted(true);
            repository.save(app);
            return true;
        }
        return false;
    }
}
