package com.yiyang.controller;

import com.yiyang.dto.ApiResponse;
import com.yiyang.dto.ConsumeNursingRequest;
import com.yiyang.dto.RenewNursingRequest;
import com.yiyang.entity.ClientNursingSetting;
import com.yiyang.service.ClientNursingSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/client-nursing-settings")
public class ClientNursingSettingController {

    @Autowired
    private ClientNursingSettingService service;

    // ==================== 查询 ====================

    /**
     * 查询某位老人的所有护理服务
     */
    @GetMapping("/client/{clientId}")
    public ApiResponse<List<ClientNursingSetting>> getByClient(@PathVariable Long clientId) {
        try {
            List<ClientNursingSetting> list = service.getClientSettings(clientId);
            return ApiResponse.success(list);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 查询老人特定护理服务的设置
     */
    @GetMapping("/client/{clientId}/item/{nursingItemId}")
    public ApiResponse<ClientNursingSetting> getByClientAndItem(
            @PathVariable Long clientId, @PathVariable Long nursingItemId) {
        try {
            return service.getClientSetting(clientId, nursingItemId)
                    .map(s -> ApiResponse.success(s))
                    .orElseGet(() -> ApiResponse.error(404, "未找到该护理服务记录"));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping
    public ApiResponse<List<ClientNursingSetting>> getAll() {
        try {
            List<ClientNursingSetting> list = service.getAllSettings();
            return ApiResponse.success(list);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/status/{serviceStatus}")
    public ApiResponse<List<ClientNursingSetting>> getByStatus(@PathVariable String serviceStatus) {
        try {
            List<ClientNursingSetting> list = service.getSettingsByStatus(serviceStatus);
            return ApiResponse.success(list);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 查询即将到期/已过期的服务
     * @param beforeDate 在此日期之前的服务被视为临近到期
     */
    @GetMapping("/expiring")
    public ApiResponse<List<ClientNursingSetting>> getExpiring(@RequestParam(required = false) String beforeDate) {
        try {
            LocalDate date = (beforeDate == null || beforeDate.isEmpty()) ? LocalDate.now() : LocalDate.parse(beforeDate);
            List<ClientNursingSetting> list = service.getExpiringServices(date);
            return ApiResponse.success(list);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    // ==================== 购买/消费/续费 ====================

    /**
     * 购买护理服务
     */
    @PostMapping("/purchase")
    public ApiResponse<ClientNursingSetting> purchase(@RequestParam Long clientId,
                                                      @RequestParam Long nursingItemId,
                                                      @RequestParam Integer quantity) {
        try {
            ClientNursingSetting result = service.purchaseService(clientId, nursingItemId, quantity);
            return ApiResponse.success("购买护理服务成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 消费护理服务（扣减剩余次数）
     */
    @PostMapping("/consume")
    public ApiResponse<ClientNursingSetting> consume(@RequestParam Long clientId,
                                                     @RequestParam Long nursingItemId,
                                                     @RequestBody ConsumeNursingRequest request) {
        try {
            ClientNursingSetting result = service.consumeService(
                    clientId, nursingItemId, request.getQuantity());
            return ApiResponse.success("消费护理服务成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 续费护理服务
     */
    @PostMapping("/renew")
    public ApiResponse<ClientNursingSetting> renew(@RequestParam Long clientId,
                                                   @RequestParam Long nursingItemId,
                                                   @RequestBody RenewNursingRequest request) {
        try {
            ClientNursingSetting result = service.renewService(
                    clientId, nursingItemId,
                    request.getAdditionalQuantity(), request.getNewDueDate());
            return ApiResponse.success("续费护理服务成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    // ==================== 管理 ====================

    @PatchMapping("/{id}/status")
    public ApiResponse<Void> updateStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            boolean success = service.updateServiceStatus(id, status);
            if (success) {
                return ApiResponse.success("更新服务状态成功", null);
            }
            return ApiResponse.error(404, "服务记录不存在");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        try {
            boolean success = service.deleteServiceSetting(id);
            if (success) {
                return ApiResponse.success("删除服务记录成功", null);
            }
            return ApiResponse.error(404, "服务记录不存在");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
