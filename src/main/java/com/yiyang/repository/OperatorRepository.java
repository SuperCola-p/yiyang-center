package com.yiyang.repository;

import com.yiyang.entity.Operator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OperatorRepository extends JpaRepository<Operator, String> {

    Optional<Operator> findByLoginCodeAndPasswordAndDeletedFalse(String loginCode, String password);

    Optional<Operator> findByRealNameAndDeletedFalse(String realName);

    @Query("SELECT o FROM Operator o WHERE o.loginCode = :loginCode AND o.deleted = false")
    Optional<Operator> findByLoginCodeAndNotDeleted(@Param("loginCode") String loginCode);
}
