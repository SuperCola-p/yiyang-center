package com.yiyang.repository;

import com.yiyang.entity.NursingLevelItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NursingLevelItemRepository extends JpaRepository<NursingLevelItem, Long> {

    List<NursingLevelItem> findByLevelId(Long levelId);

    List<NursingLevelItem> findByItemId(Long itemId);

    Optional<NursingLevelItem> findByLevelIdAndItemId(Long levelId, Long itemId);

    void deleteByLevelIdAndItemId(Long levelId, Long itemId);
}
