package com.yiyang.controller;

import com.yiyang.dto.ApiResponse;
import com.yiyang.dto.AuditRequest;
import com.yiyang.entity.CheckOutApplication;
import com.yiyang.service.CheckOutApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/check-out-applications")
public class CheckOutApplicationController {

    @Autowired
    private CheckOutApplicationService service;

    @PostMapping
    public ApiResponse<CheckOutApplication> create(@RequestBody CheckOutApplication application) {
        try {
            CheckOutApplication result = service.createApplication(application);
            return ApiResponse.success("提交退住申请成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/audit")
    public ApiResponse<CheckOutApplication> audit(@PathVariable String id, @RequestBody AuditRequest request) {
        try {
            CheckOutApplication result = service.auditApplication(id, request.getStatus(), request.getAuditor());
            return ApiResponse.success("审核退住申请成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<CheckOutApplication> getById(@PathVariable String id) {
        try {
            return service.getApplicationById(id)
                    .map(a -> ApiResponse.success(a))
                    .orElseGet(() -> ApiResponse.error(404, "退住申请不存在"));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping
    public ApiResponse<List<CheckOutApplication>> getAll() {
        try {
            return ApiResponse.success(service.getAllApplications());
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/status/{status}")
    public ApiResponse<List<CheckOutApplication>> getByStatus(@PathVariable String status) {
        try {
            return ApiResponse.success(service.getApplicationsByStatus(status));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/type/{type}")
    public ApiResponse<List<CheckOutApplication>> getByType(@PathVariable String type) {
        try {
            return ApiResponse.success(service.getApplicationsByType(type));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        try {
            boolean success = service.deleteApplication(id);
            if (success) {
                return ApiResponse.success("删除退住申请成功", null);
            }
            return ApiResponse.error(404, "退住申请不存在");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
