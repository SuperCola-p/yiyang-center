package com.yiyang.service.impl;

import com.yiyang.entity.CheckOutApplication;
import com.yiyang.repository.CheckOutApplicationRepository;
import com.yiyang.service.CheckOutApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CheckOutApplicationServiceImpl implements CheckOutApplicationService {

    @Autowired
    private CheckOutApplicationRepository repository;

    @Override
    public CheckOutApplication createApplication(CheckOutApplication application) {
        if (application.getId() == null || application.getId().trim().isEmpty()) {
            application.setId("CO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        if (application.getStatus() == null) {
            application.setStatus("已提交");
        }
        if (application.getDeleted() == null) {
            application.setDeleted(false);
        }
        if (application.getTime() == null) {
            application.setTime(LocalDateTime.now());
        }
        return repository.save(application);
    }

    @Override
    public CheckOutApplication auditApplication(String id, String status, String auditor) {
        CheckOutApplication app = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("退住申请不存在，ID: " + id));

        app.setStatus(status);
        app.setAuditor(auditor);
        app.setAuditTime(LocalDateTime.now());
        return repository.save(app);
    }

    @Override
    public Optional<CheckOutApplication> getApplicationById(String id) {
        return repository.findById(id)
                .filter(a -> !Boolean.TRUE.equals(a.getDeleted()));
    }

    @Override
    public List<CheckOutApplication> getAllApplications() {
        return repository.findByDeletedFalse();
    }

    @Override
    public List<CheckOutApplication> getApplicationsByStatus(String status) {
        return repository.findByStatusAndDeletedFalse(status);
    }

    @Override
    public List<CheckOutApplication> getApplicationsByType(String type) {
        return repository.findByTypeAndDeletedFalse(type);
    }

    @Override
    public boolean deleteApplication(String id) {
        Optional<CheckOutApplication> opt = repository.findById(id);
        if (opt.isPresent()) {
            CheckOutApplication app = opt.get();
            app.setDeleted(true);
            repository.save(app);
            return true;
        }
        return false;
    }
}
