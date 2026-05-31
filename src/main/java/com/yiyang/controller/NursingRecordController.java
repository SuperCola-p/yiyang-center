package com.yiyang.controller;

import com.yiyang.dto.ApiResponse;
import com.yiyang.entity.NursingRecord;
import com.yiyang.service.NursingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/nursing-records")
public class NursingRecordController {

    @Autowired
    private NursingRecordService service;

    @PostMapping
    public ApiResponse<NursingRecord> add(@RequestBody NursingRecord record) {
        try {
            NursingRecord result = service.addRecord(record);
            return ApiResponse.success("添加护理记录成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        try {
            boolean success = service.deleteRecord(id);
            if (success) {
                return ApiResponse.success("删除护理记录成功", null);
            }
            return ApiResponse.error(404, "护理记录不存在");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<NursingRecord> getById(@PathVariable Long id) {
        try {
            return service.getRecordById(id)
                    .map(r -> ApiResponse.success(r))
                    .orElseGet(() -> ApiResponse.error(404, "护理记录不存在"));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping
    public ApiResponse<List<NursingRecord>> getAll() {
        try {
            return ApiResponse.success(service.getAllRecords());
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 多条件组合查询
     * GET /api/nursing-records/search?clientId=1&nursingItemId=2&healthAssistantId=nurse01&startTime=2026-01-01T00:00&endTime=2026-12-31T23:59
     * 所有参数均为可选
     */
    @GetMapping("/search")
    public ApiResponse<List<NursingRecord>> search(
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) Long nursingItemId,
            @RequestParam(required = false) String healthAssistantId,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        try {
            LocalDateTime start = startTime != null ? LocalDateTime.parse(startTime) : null;
            LocalDateTime end = endTime != null ? LocalDateTime.parse(endTime) : null;
            List<NursingRecord> records = service.searchRecords(
                    clientId, nursingItemId, healthAssistantId, start, end);
            return ApiResponse.success(records);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/client/{clientId}")
    public ApiResponse<List<NursingRecord>> getByClient(@PathVariable Long clientId) {
        try {
            return ApiResponse.success(service.getRecordsByClient(clientId));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/assistant/{healthAssistantId}")
    public ApiResponse<List<NursingRecord>> getByAssistant(@PathVariable String healthAssistantId) {
        try {
            return ApiResponse.success(service.getRecordsByHealthAssistant(healthAssistantId));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/item/{nursingItemId}")
    public ApiResponse<List<NursingRecord>> getByItem(@PathVariable Long nursingItemId) {
        try {
            return ApiResponse.success(service.getRecordsByNursingItem(nursingItemId));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/date-range")
    public ApiResponse<List<NursingRecord>> getByDateRange(
            @RequestParam String start, @RequestParam String end) {
        try {
            return ApiResponse.success(service.getRecordsByDateRange(
                    LocalDateTime.parse(start), LocalDateTime.parse(end)));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/client/{clientId}/date-range")
    public ApiResponse<List<NursingRecord>> getByClientAndDateRange(
            @PathVariable Long clientId,
            @RequestParam String start, @RequestParam String end) {
        try {
            return ApiResponse.success(service.getRecordsByClientAndDateRange(
                    clientId, LocalDateTime.parse(start), LocalDateTime.parse(end)));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/client/{clientId}/count")
    public ApiResponse<Long> countByClient(@PathVariable Long clientId) {
        try {
            return ApiResponse.success(service.getRecordCountByClient(clientId));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
