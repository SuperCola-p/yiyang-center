package com.yiyang.service;

import com.yiyang.dto.LoginResponse;
import com.yiyang.entity.Operator;

import java.util.List;
import java.util.Optional;

public interface OperatorService {

    Operator addOperator(Operator operator);

    Operator updateOperator(String loginCode, Operator operator);

    boolean deleteOperator(String loginCode);

    Optional<Operator> getOperatorByLoginCode(String loginCode);

    List<Operator> getAllOperators();

    LoginResponse login(String loginCode, String password);
}
