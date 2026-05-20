package com.yiyang.repository;

import com.yiyang.entity.BedDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BedDetailsRepository extends JpaRepository<BedDetails, Integer> {

    List<BedDetails> findByCustomerId(Integer customerId);

    List<BedDetails> findByBedId(Integer bedId);
}
