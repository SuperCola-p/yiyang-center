package com.yiyang.controller;

import com.yiyang.dto.ApiResponse;
import com.yiyang.dto.AuditRequest;
import com.yiyang.entity.LeaveApplication;
import com.yiyang.service.LeaveApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/leave-applications")
public class LeaveApplicationController {

    @Autowired
    private LeaveApplicationService service;

    @PostMapping
    public ApiResponse<LeaveApplication> create(@RequestBody LeaveApplication application) {
        try {
            LeaveApplication result = service.createApplication(application);
            return ApiResponse.success("提交请假申请成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/audit")
    public ApiResponse<LeaveApplication> audit(@PathVariable String id, @RequestBody AuditRequest request) {
        try {
            String status;
            String auditor = request.getAuditor();

            // 前端传 approve 布尔值，转为英文状态
            if (request.getApprove() != null) {
                status = request.getApprove() ? "APPROVED" : "REJECTED";
            } else if (request.getStatus() != null) {
                // 兼容直接传 status 的情况
                status = request.getStatus();
            } else {
                return ApiResponse.error("审核状态不能为空");
            }

            LeaveApplication result = service.auditApplication(id, status, auditor);
            return ApiResponse.success("审核请假申请成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/return")
    public ApiResponse<LeaveApplication> recordReturn(@PathVariable String id,
                                                      @RequestBody(required = false) LeaveApplication body) {
        try {
            LocalDateTime returnTime;
            if (body != null && body.getActualReturnTime() != null) {
                returnTime = body.getActualReturnTime();
            } else {
                returnTime = LocalDateTime.now();
            }
            LeaveApplication result = service.recordReturn(id, returnTime);
            return ApiResponse.success("销假成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<LeaveApplication> getById(@PathVariable String id) {
        try {
            return service.getApplicationById(id)
                    .map(a -> ApiResponse.success(a))
                    .orElseGet(() -> ApiResponse.error(404, "请假申请不存在"));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping
    public ApiResponse<List<LeaveApplication>> getAll() {
        try {
            return ApiResponse.success(service.getAllApplications());
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/status/{status}")
    public ApiResponse<List<LeaveApplication>> getByStatus(@PathVariable String status) {
        try {
            return ApiResponse.success(service.getApplicationsByStatus(status));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        try {
            boolean success = service.deleteApplication(id);
            if (success) {
                return ApiResponse.success("删除请假申请成功", null);
            }
            return ApiResponse.error(404, "请假申请不存在");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
