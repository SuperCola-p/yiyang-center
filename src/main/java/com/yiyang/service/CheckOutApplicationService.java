package com.yiyang.service;

import com.yiyang.entity.CheckOutApplication;

import java.util.List;
import java.util.Optional;

public interface CheckOutApplicationService {

    CheckOutApplication createApplication(CheckOutApplication application);

    CheckOutApplication auditApplication(String id, String status, String auditor);

    Optional<CheckOutApplication> getApplicationById(String id);

    List<CheckOutApplication> getAllApplications();

    List<CheckOutApplication> getApplicationsByStatus(String status);

    List<CheckOutApplication> getApplicationsByType(String type);

    boolean deleteApplication(String id);
}
