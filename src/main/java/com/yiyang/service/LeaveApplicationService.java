package com.yiyang.service;

import com.yiyang.entity.LeaveApplication;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LeaveApplicationService {

    LeaveApplication createApplication(LeaveApplication application);

    LeaveApplication auditApplication(String id, String status, String auditor);

    LeaveApplication recordReturn(String id, LocalDateTime actualReturnTime);

    Optional<LeaveApplication> getApplicationById(String id);

    List<LeaveApplication> getAllApplications();

    List<LeaveApplication> getApplicationsByStatus(String status);

    boolean deleteApplication(String id);
}
