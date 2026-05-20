package com.yiyang.repository;

import com.yiyang.entity.Bed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BedRepository extends JpaRepository<Bed, Long> {

    List<Bed> findByBuildingAndRoomNo(String building, Integer roomNo);

    Optional<Bed> findByBuildingAndRoomNoAndBedNo(String building, Integer roomNo, String bedNo);

    List<Bed> findByBedStatus(Integer bedStatus);

    List<Bed> findByBuildingAndRoomNoAndBedStatus(String building, Integer roomNo, Integer bedStatus);
}
