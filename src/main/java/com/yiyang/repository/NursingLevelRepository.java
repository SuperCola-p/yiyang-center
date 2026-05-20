package com.yiyang.repository;

import com.yiyang.entity.NursingLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NursingLevelRepository extends JpaRepository<NursingLevel, Long> {

    Optional<NursingLevel> findByLevelNameAndDeletedFalse(String levelName);

    List<NursingLevel> findByStatusAndDeletedFalse(String status);

    List<NursingLevel> findByDeletedFalse();
}
