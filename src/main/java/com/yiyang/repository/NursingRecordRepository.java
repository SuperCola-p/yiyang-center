package com.yiyang.repository;

import com.yiyang.entity.NursingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NursingRecordRepository extends JpaRepository<NursingRecord, Long> {

    List<NursingRecord> findByClientIdAndIsDeletedFalseOrderByNursingTimeDesc(Long clientId);

    List<NursingRecord> findByHealthAssistantIdAndIsDeletedFalse(Long healthAssistantId);

    List<NursingRecord> findByNursingItemIdAndIsDeletedFalse(Long nursingItemId);

    @Query("SELECT r FROM NursingRecord r WHERE " +
           "r.clientId = :clientId AND r.isDeleted = false AND " +
           "r.nursingTime >= :start AND r.nursingTime <= :end " +
           "ORDER BY r.nursingTime DESC")
    List<NursingRecord> findByClientIdAndDateRange(
            @Param("clientId") Long clientId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("SELECT r FROM NursingRecord r WHERE " +
           "r.isDeleted = false AND " +
           "r.nursingTime >= :start AND r.nursingTime <= :end " +
           "ORDER BY r.nursingTime DESC")
    List<NursingRecord> findByDateRange(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    long countByClientIdAndIsDeletedFalse(Long clientId);

    long countByHealthAssistantIdAndIsDeletedFalse(Long healthAssistantId);

    boolean existsByClientIdAndNursingItemIdAndNursingTimeAndIsDeletedFalse(
            Long clientId, Long nursingItemId, LocalDateTime nursingTime);
}
