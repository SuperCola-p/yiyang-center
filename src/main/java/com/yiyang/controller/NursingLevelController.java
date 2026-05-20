package com.yiyang.controller;

import com.yiyang.dto.ApiResponse;
import com.yiyang.dto.NursingLevelItemRequest;
import com.yiyang.dto.NursingLevelStatusRequest;
import com.yiyang.entity.NursingLevel;
import com.yiyang.entity.NursingLevelItem;
import com.yiyang.service.NursingLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nursing-levels")
public class NursingLevelController {

    @Autowired
    private NursingLevelService nursingLevelService;

    // ==================== 护理等级 CRUD ====================

    @PostMapping
    public ApiResponse<NursingLevel> add(@RequestBody NursingLevel nursingLevel) {
        try {
            NursingLevel result = nursingLevelService.addNursingLevel(nursingLevel);
            return ApiResponse.success("添加护理等级成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<NursingLevel> update(@PathVariable Long id, @RequestBody NursingLevel nursingLevel) {
        try {
            NursingLevel result = nursingLevelService.updateNursingLevel(id, nursingLevel);
            return ApiResponse.success("更新护理等级成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        try {
            boolean success = nursingLevelService.deleteNursingLevel(id);
            if (success) {
                return ApiResponse.success("删除护理等级成功", null);
            }
            return ApiResponse.error(404, "护理等级不存在，ID: " + id);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<NursingLevel> getById(@PathVariable Long id) {
        try {
            return nursingLevelService.getNursingLevelById(id)
                    .map(item -> ApiResponse.success(item))
                    .orElseGet(() -> ApiResponse.error(404, "护理等级不存在，ID: " + id));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/name/{name}")
    public ApiResponse<NursingLevel> getByName(@PathVariable String name) {
        try {
            return nursingLevelService.getNursingLevelByName(name)
                    .map(item -> ApiResponse.success(item))
                    .orElseGet(() -> ApiResponse.error(404, "护理等级不存在，名称: " + name));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping
    public ApiResponse<List<NursingLevel>> getAll() {
        try {
            List<NursingLevel> list = nursingLevelService.getAllNursingLevels();
            return ApiResponse.success(list);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/status/{status}")
    public ApiResponse<List<NursingLevel>> getByStatus(@PathVariable String status) {
        try {
            List<NursingLevel> list = nursingLevelService.getNursingLevelsByStatus(status);
            return ApiResponse.success(list);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Void> updateStatus(@PathVariable Long id, @RequestBody NursingLevelStatusRequest request) {
        try {
            boolean success = nursingLevelService.updateNursingLevelStatus(id, request.getStatus());
            if (success) {
                return ApiResponse.success("更新状态成功", null);
            }
            return ApiResponse.error(404, "护理等级不存在，ID: " + id);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    // ==================== 等级-项目关联管理 ====================

    @GetMapping("/{id}/items")
    public ApiResponse<List<NursingLevelItem>> getItems(@PathVariable Long id) {
        try {
            List<NursingLevelItem> items = nursingLevelService.getItemsByLevelId(id);
            return ApiResponse.success(items);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/items")
    public ApiResponse<Void> addItems(@PathVariable Long id, @RequestBody NursingLevelItemRequest request) {
        try {
            nursingLevelService.addItemsToLevel(id, request.getItemIds());
            return ApiResponse.success("添加护理项目成功", null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/items/{itemId}")
    public ApiResponse<Void> removeItem(@PathVariable Long id, @PathVariable Long itemId) {
        try {
            boolean success = nursingLevelService.removeItemFromLevel(id, itemId);
            if (success) {
                return ApiResponse.success("移除护理项目成功", null);
            }
            return ApiResponse.error(404, "关联不存在");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/items")
    public ApiResponse<Void> removeItems(@PathVariable Long id, @RequestBody NursingLevelItemRequest request) {
        try {
            nursingLevelService.removeItemsFromLevel(id, request.getItemIds());
            return ApiResponse.success("移除护理项目成功", null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
