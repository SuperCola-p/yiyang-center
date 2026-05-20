package com.yiyang.repository;

import com.yiyang.entity.LeaveApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, String> {

    List<LeaveApplication> findByStatusAndDeletedFalse(String status);

    List<LeaveApplication> findByDeletedFalse();
}
