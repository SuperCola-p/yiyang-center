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

    List<NursingRecord> findByHealthAssistantIdAndIsDeletedFalse(String healthAssistantId);

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

    /**
     * 多条件组合查询（所有参数均为可选，null/空字符串表示不限制该条件）
     */
    @Query("SELECT r FROM NursingRecord r WHERE " +
           "r.isDeleted = false " +
           "AND (:clientId IS NULL OR r.clientId = :clientId) " +
           "AND (:nursingItemId IS NULL OR r.nursingItemId = :nursingItemId) " +
           "AND (:healthAssistantId IS NULL OR r.healthAssistantId = :healthAssistantId OR :healthAssistantId = '') " +
           "AND r.nursingTime >= :startTime AND r.nursingTime <= :endTime " +
           "ORDER BY r.nursingTime DESC")
    List<NursingRecord> findByConditions(
            @Param("clientId") Long clientId,
            @Param("nursingItemId") Long nursingItemId,
            @Param("healthAssistantId") String healthAssistantId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    long countByClientIdAndIsDeletedFalse(Long clientId);

    long countByHealthAssistantIdAndIsDeletedFalse(String healthAssistantId);

    boolean existsByClientIdAndNursingItemIdAndNursingTimeAndIsDeletedFalse(
            Long clientId, Long nursingItemId, LocalDateTime nursingTime);
}
