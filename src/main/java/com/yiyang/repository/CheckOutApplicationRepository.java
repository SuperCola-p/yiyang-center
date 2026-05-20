package com.yiyang.repository;

import com.yiyang.entity.CheckOutApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckOutApplicationRepository extends JpaRepository<CheckOutApplication, String> {

    List<CheckOutApplication> findByStatusAndDeletedFalse(String status);

    List<CheckOutApplication> findByTypeAndDeletedFalse(String type);

    List<CheckOutApplication> findByDeletedFalse();
}
