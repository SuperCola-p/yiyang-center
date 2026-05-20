package com.yiyang.repository;

import com.yiyang.entity.NursingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NursingItemRepository extends JpaRepository<NursingItem, Long>, JpaSpecificationExecutor<NursingItem> {

    Optional<NursingItem> findByCodeAndIsDeletedFalse(String code);

    List<NursingItem> findByStatusAndIsDeletedFalse(String status);

    List<NursingItem> findByNameContainingAndIsDeletedFalse(String name);

    List<NursingItem> findByIsDeletedFalse();
}
