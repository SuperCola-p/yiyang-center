package com.yiyang.repository;

import com.yiyang.entity.ClientNursingSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientNursingSettingRepository extends JpaRepository<ClientNursingSetting, Long> {

    List<ClientNursingSetting> findByClientIdAndIsDeletedFalse(Long clientId);

    Optional<ClientNursingSetting> findByClientIdAndNursingItemIdAndIsDeletedFalse(Long clientId, Long nursingItemId);

    List<ClientNursingSetting> findByServiceStatusAndIsDeletedFalse(String serviceStatus);

    List<ClientNursingSetting> findByIsDeletedFalse();

    @Query("SELECT s FROM ClientNursingSetting s WHERE " +
           "s.isDeleted = false AND s.serviceDueDate IS NOT NULL AND " +
           "s.serviceDueDate < :beforeDate AND " +
           "s.remainingQuantity > 0")
    List<ClientNursingSetting> findExpiringServices(@Param("beforeDate") LocalDate beforeDate);
}
