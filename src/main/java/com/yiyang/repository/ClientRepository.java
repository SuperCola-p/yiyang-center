package com.yiyang.repository;

import com.yiyang.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer>, JpaSpecificationExecutor<Client> {

    List<Client> findByNameContainingAndDeletedFalse(String name);

    List<Client> findByTypeAndDeletedFalse(String type);

    List<Client> findByDeletedFalse();

    Optional<Client> findByIdAndDeletedFalse(Integer id);

    Optional<Client> findByBuildingNoAndRoomNoAndBedNoAndDeletedFalse(
            String buildingNo, String roomNo, String bedNo);

    long countByDeletedFalse();

    @Query("SELECT c FROM Client c WHERE " +
           "(:name IS NULL OR c.name LIKE %:name%) AND " +
           "(:type IS NULL OR c.type = :type) AND " +
           "c.deleted = false")
    List<Client> searchClients(@Param("name") String name, @Param("type") String type);
}
