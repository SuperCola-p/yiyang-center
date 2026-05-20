package com.yiyang.service.impl;

import com.yiyang.dto.LoginResponse;
import com.yiyang.entity.Operator;
import com.yiyang.repository.OperatorRepository;
import com.yiyang.service.OperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class OperatorServiceImpl implements OperatorService {

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    @Autowired
    private OperatorRepository repository;

    @Override
    public Operator addOperator(Operator operator) {
        if (operator.getOperatorType() == null) {
            operator.setOperatorType("ADMIN");
        }
        if (operator.getDeleted() == null) {
            operator.setDeleted(false);
        }
        // 密码 BCrypt 加密存储
        if (operator.getPassword() != null) {
            operator.setPassword(ENCODER.encode(operator.getPassword()));
        }
        return repository.save(operator);
    }

    @Override
    public Operator updateOperator(String loginCode, Operator operator) {
        Operator existing = repository.findById(loginCode)
                .orElseThrow(() -> new RuntimeException("操作员不存在，登录码: " + loginCode));

        if (operator.getPassword() != null) {
            existing.setPassword(ENCODER.encode(operator.getPassword()));
        }
        if (operator.getRealName() != null) {
            existing.setRealName(operator.getRealName());
        }
        if (operator.getOperatorType() != null) {
            existing.setOperatorType(operator.getOperatorType());
        }

        return repository.save(existing);
    }

    @Override
    public boolean deleteOperator(String loginCode) {
        Optional<Operator> opt = repository.findById(loginCode);
        if (opt.isPresent()) {
            Operator op = opt.get();
            op.setDeleted(true);
            repository.save(op);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Operator> getOperatorByLoginCode(String loginCode) {
        return repository.findById(loginCode)
                .filter(o -> !Boolean.TRUE.equals(o.getDeleted()));
    }

    @Override
    public List<Operator> getAllOperators() {
        return repository.findAll().stream()
                .filter(o -> !Boolean.TRUE.equals(o.getDeleted()))
                .collect(Collectors.toList());
    }

    @Override
    public LoginResponse login(String loginCode, String password) {
        Operator operator = repository.findById(loginCode)
                .filter(o -> !Boolean.TRUE.equals(o.getDeleted()))
                .orElseThrow(() -> new RuntimeException("登录码或密码错误"));

        if (!ENCODER.matches(password, operator.getPassword())) {
            throw new RuntimeException("登录码或密码错误");
        }

        LoginResponse response = new LoginResponse();
        response.setLoginCode(operator.getLoginCode());
        response.setRealName(operator.getRealName());
        response.setOperatorType(operator.getOperatorType());
        response.setToken(UUID.randomUUID().toString().replaceAll("-", ""));
        return response;
    }
}
