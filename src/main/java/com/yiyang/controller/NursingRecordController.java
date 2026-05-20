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

    @GetMapping("/client/{clientId}")
    public ApiResponse<List<NursingRecord>> getByClient(@PathVariable Long clientId) {
        try {
            return ApiResponse.success(service.getRecordsByClient(clientId));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/assistant/{healthAssistantId}")
    public ApiResponse<List<NursingRecord>> getByAssistant(@PathVariable Long healthAssistantId) {
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
