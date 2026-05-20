package com.yiyang.controller;

import com.yiyang.dto.ApiResponse;
import com.yiyang.dto.LoginRequest;
import com.yiyang.dto.LoginResponse;
import com.yiyang.entity.Operator;
import com.yiyang.service.OperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/operators")
public class OperatorController {

    @Autowired
    private OperatorService service;

    @PostMapping
    public ApiResponse<Operator> add(@RequestBody Operator operator) {
        try {
            Operator result = service.addOperator(operator);
            return ApiResponse.success("添加操作员成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PutMapping("/{loginCode}")
    public ApiResponse<Operator> update(@PathVariable String loginCode, @RequestBody Operator operator) {
        try {
            Operator result = service.updateOperator(loginCode, operator);
            return ApiResponse.success("更新操作员成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{loginCode}")
    public ApiResponse<Void> delete(@PathVariable String loginCode) {
        try {
            boolean success = service.deleteOperator(loginCode);
            if (success) {
                return ApiResponse.success("删除操作员成功", null);
            }
            return ApiResponse.error(404, "操作员不存在");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/{loginCode}")
    public ApiResponse<Operator> getByLoginCode(@PathVariable String loginCode) {
        try {
            return service.getOperatorByLoginCode(loginCode)
                    .map(op -> ApiResponse.success(op))
                    .orElseGet(() -> ApiResponse.error(404, "操作员不存在"));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping
    public ApiResponse<List<Operator>> getAll() {
        try {
            return ApiResponse.success(service.getAllOperators());
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request, HttpSession session) {
        try {
            LoginResponse response = service.login(request.getLoginCode(), request.getPassword());
            // 登录成功，直接设置 Session
            Map<String, Object> loginUser = new HashMap<>();
            loginUser.put("loginCode", response.getLoginCode());
            loginUser.put("realName", response.getRealName());
            loginUser.put("operatorType", response.getOperatorType());
            loginUser.put("token", response.getToken());
            session.setAttribute("loginUser", loginUser);
            return ApiResponse.success("登录成功", response);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
