package com.yiyang.controller;

import com.yiyang.dto.ApiResponse;
import com.yiyang.dto.BedStatusRequest;
import com.yiyang.dto.SwapBedRequest;
import com.yiyang.entity.Bed;
import com.yiyang.entity.BedDetails;
import com.yiyang.service.BedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beds")
public class BedController {

    @Autowired
    private BedService bedService;

    // ==================== 床位 CRUD ====================

    @PostMapping
    public ApiResponse<Bed> add(@RequestBody Bed bed) {
        try {
            Bed result = bedService.addBed(bed);
            return ApiResponse.success("添加床位成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<Bed> update(@PathVariable Long id, @RequestBody Bed bed) {
        try {
            Bed result = bedService.updateBed(id, bed);
            return ApiResponse.success("更新床位成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        try {
            boolean success = bedService.deleteBed(id);
            if (success) {
                return ApiResponse.success("删除床位成功", null);
            }
            return ApiResponse.error(404, "床位不存在，ID: " + id);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<Bed> getById(@PathVariable Long id) {
        try {
            return bedService.getBedById(id)
                    .map(bed -> ApiResponse.success(bed))
                    .orElseGet(() -> ApiResponse.error(404, "床位不存在，ID: " + id));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping
    public ApiResponse<List<Bed>> getAll() {
        try {
            List<Bed> list = bedService.getAllBeds();
            return ApiResponse.success(list);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 按楼栋和房间查询床位
     */
    @GetMapping("/room")
    public ApiResponse<List<Bed>> getByRoom(@RequestParam String building, @RequestParam Integer roomNo) {
        try {
            List<Bed> list = bedService.getBedsByBuildingAndRoom(building, roomNo);
            return ApiResponse.success(list);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 按位置精确查询床位
     */
    @GetMapping("/location")
    public ApiResponse<Bed> getByLocation(@RequestParam String building,
                                          @RequestParam Integer roomNo,
                                          @RequestParam String bedNo) {
        try {
            return bedService.getBedByLocation(building, roomNo, bedNo)
                    .map(bed -> ApiResponse.success(bed))
                    .orElseGet(() -> ApiResponse.error(404, "床位不存在"));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 按状态查询床位
     */
    @GetMapping("/status/{bedStatus}")
    public ApiResponse<List<Bed>> getByStatus(@PathVariable Integer bedStatus) {
        try {
            List<Bed> list = bedService.getBedsByStatus(bedStatus);
            return ApiResponse.success(list);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 更新床位状态
     */
    @PatchMapping("/{id}/status")
    public ApiResponse<Void> updateStatus(@PathVariable Long id, @RequestBody BedStatusRequest request) {
        try {
            boolean success = bedService.updateBedStatus(id, request.getBedStatus());
            if (success) {
                return ApiResponse.success("更新床位状态成功", null);
            }
            return ApiResponse.error(404, "床位不存在，ID: " + id);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 查询某房间的空闲床位
     */
    @GetMapping("/available")
    public ApiResponse<List<Bed>> getAvailable(@RequestParam String building, @RequestParam Integer roomNo) {
        try {
            List<Bed> list = bedService.getAvailableBedsByBuildingAndRoom(building, roomNo);
            return ApiResponse.success(list);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    // ==================== 入住/换床/退住 ====================

    /**
     * 分配床位给老人
     */
    @PostMapping("/assign")
    public ApiResponse<BedDetails> assignBed(@RequestParam Integer customerId, @RequestParam Integer bedId) {
        try {
            BedDetails result = bedService.assignBedToCustomer(customerId, bedId);
            return ApiResponse.success("分配床位成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 换床
     */
    @PostMapping("/swap")
    public ApiResponse<BedDetails> swapBed(@RequestBody SwapBedRequest request) {
        try {
            BedService.SwapBedParams params = new BedService.SwapBedParams(
                    request.getBuilding(), request.getRoomNo(),
                    request.getOldBedNo(), request.getNewBedNo(),
                    request.getClientId());
            BedDetails result = bedService.swapBed(params);
            return ApiResponse.success("换床成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 退住
     */
    @PostMapping("/checkout")
    public ApiResponse<Void> checkout(@RequestParam Integer bedId, @RequestParam Integer customerId) {
        try {
            boolean success = bedService.checkoutCustomerFromBed(bedId, customerId);
            if (success) {
                return ApiResponse.success("退住成功", null);
            }
            return ApiResponse.error("退住失败，床位不存在");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 查询床位的入住历史
     */
    @GetMapping("/{id}/history")
    public ApiResponse<List<BedDetails>> getHistory(@PathVariable Integer id) {
        try {
            List<BedDetails> history = bedService.getBedHistory(id);
            return ApiResponse.success(history);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 查询老人的入住历史
     */
    @GetMapping("/customer/{customerId}/history")
    public ApiResponse<List<BedDetails>> getCustomerHistory(@PathVariable Integer customerId) {
        try {
            List<BedDetails> history = bedService.getCustomerBedHistory(customerId);
            return ApiResponse.success(history);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
