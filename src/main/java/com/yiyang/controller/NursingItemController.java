package com.yiyang.controller;

import com.yiyang.dto.ApiResponse;
import com.yiyang.dto.NursingItemStatusRequest;
import com.yiyang.dto.PriceRangeRequest;
import com.yiyang.entity.NursingItem;
import com.yiyang.service.NursingItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nursing-items")
public class NursingItemController {

    @Autowired
    private NursingItemService nursingItemService;

    /**
     * 添加护理项目
     */
    @PostMapping
    public ApiResponse<NursingItem> add(@RequestBody NursingItem nursingItem) {
        try {
            NursingItem result = nursingItemService.addNursingItem(nursingItem);
            return ApiResponse.success("添加护理项目成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 更新护理项目
     */
    @PutMapping("/{id}")
    public ApiResponse<NursingItem> update(@PathVariable Long id, @RequestBody NursingItem nursingItem) {
        try {
            NursingItem result = nursingItemService.updateNursingItem(id, nursingItem);
            return ApiResponse.success("更新护理项目成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 删除护理项目（逻辑删除）
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        try {
            boolean success = nursingItemService.deleteNursingItem(id);
            if (success) {
                return ApiResponse.success("删除护理项目成功", null);
            }
            return ApiResponse.error("护理项目不存在，ID: " + id);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 根据ID查询护理项目
     */
    @GetMapping("/{id}")
    public ApiResponse<NursingItem> getById(@PathVariable Long id) {
        try {
            return nursingItemService.getNursingItemById(id)
                    .map(item -> ApiResponse.success(item))
                    .orElseGet(() -> ApiResponse.error(404, "护理项目不存在，ID: " + id));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 根据编号查询护理项目
     */
    @GetMapping("/code/{code}")
    public ApiResponse<NursingItem> getByCode(@PathVariable String code) {
        try {
            return nursingItemService.getNursingItemByCode(code)
                    .map(item -> ApiResponse.success(item))
                    .orElseGet(() -> ApiResponse.error(404, "护理项目不存在，编号: " + code));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 查询所有护理项目
     */
    @GetMapping
    public ApiResponse<List<NursingItem>> getAll() {
        try {
            List<NursingItem> list = nursingItemService.getAllNursingItems();
            return ApiResponse.success(list);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 根据状态查询护理项目
     */
    @GetMapping("/status/{status}")
    public ApiResponse<List<NursingItem>> getByStatus(@PathVariable String status) {
        try {
            List<NursingItem> list = nursingItemService.getNursingItemsByStatus(status);
            return ApiResponse.success(list);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 根据名称模糊查询
     */
    @GetMapping("/search")
    public ApiResponse<List<NursingItem>> searchByName(@RequestParam String name) {
        try {
            List<NursingItem> list = nursingItemService.searchNursingItemsByName(name);
            return ApiResponse.success(list);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 更新护理项目状态（启用/停用）
     */
    @PatchMapping("/{id}/status")
    public ApiResponse<Void> updateStatus(@PathVariable Long id, @RequestBody NursingItemStatusRequest request) {
        try {
            boolean success = nursingItemService.updateNursingItemStatus(id, request.getStatus());
            if (success) {
                return ApiResponse.success("更新状态成功", null);
            }
            return ApiResponse.error(404, "护理项目不存在，ID: " + id);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 根据价格范围查询
     */
    @PostMapping("/price-range")
    public ApiResponse<List<NursingItem>> getByPriceRange(@RequestBody PriceRangeRequest request) {
        try {
            List<NursingItem> list = nursingItemService.getNursingItemsByPriceRange(
                    request.getMinPrice(), request.getMaxPrice());
            return ApiResponse.success(list);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
