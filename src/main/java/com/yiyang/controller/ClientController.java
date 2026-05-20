package com.yiyang.controller;

import com.yiyang.dto.ApiResponse;
import com.yiyang.dto.SetByLevelRequest;
import com.yiyang.entity.Client;
import com.yiyang.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    // ==================== 老人基本信息 CRUD ====================

    @PostMapping
    public ApiResponse<Client> add(@RequestBody Client client) {
        try {
            Client result = clientService.addClient(client);
            return ApiResponse.success("添加老人信息成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<Client> update(@PathVariable Integer id, @RequestBody Client client) {
        try {
            Client result = clientService.updateClient(id, client);
            return ApiResponse.success("更新老人信息成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Integer id) {
        try {
            boolean success = clientService.deleteClient(id);
            if (success) {
                return ApiResponse.success("删除老人信息成功", null);
            }
            return ApiResponse.error(404, "老人信息不存在，ID: " + id);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<Client> getById(@PathVariable Integer id) {
        try {
            return clientService.getClientById(id)
                    .map(client -> ApiResponse.success(client))
                    .orElseGet(() -> ApiResponse.error(404, "老人信息不存在，ID: " + id));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping
    public ApiResponse<List<Client>> getAll() {
        try {
            List<Client> list = clientService.getAllClients();
            return ApiResponse.success(list);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/count")
    public ApiResponse<Long> getCount() {
        try {
            long count = clientService.getClientCount();
            return ApiResponse.success(count);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    // ==================== 查询/搜索 ====================

    /**
     * 高级搜索（支持按姓名和类型进行筛选）
     */
    @GetMapping("/search")
    public ApiResponse<List<Client>> search(@RequestParam(required = false) String name,
                                            @RequestParam(required = false) String type) {
        try {
            List<Client> list = clientService.searchClients(name, type);
            return ApiResponse.success(list);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 按类型查询
     */
    @GetMapping("/type/{type}")
    public ApiResponse<List<Client>> getByType(@PathVariable String type) {
        try {
            List<Client> list = clientService.getClientsByType(type);
            return ApiResponse.success(list);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 按护理等级查询
     */
    @GetMapping("/nursing-level/{nursingLevel}")
    public ApiResponse<List<Client>> getByNursingLevel(@PathVariable String nursingLevel) {
        try {
            List<Client> list = clientService.getClientsByNursingLevel(nursingLevel);
            return ApiResponse.success(list);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 按床位查询入住老人
     */
    @GetMapping("/bed")
    public ApiResponse<Client> getByBed(@RequestParam String buildingNo,
                                        @RequestParam String roomNo,
                                        @RequestParam String bedNo) {
        try {
            return clientService.getClientByBed(buildingNo, roomNo, bedNo)
                    .map(client -> ApiResponse.success(client))
                    .orElseGet(() -> ApiResponse.error(404, "该床位暂无老人入住"));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    // ==================== 护理等级设定 ====================

    /**
     * 按等级批量设置老人的护理项目
     */
    @PostMapping("/assign-level")
    public ApiResponse<Void> assignNursingLevel(@RequestBody SetByLevelRequest request) {
        try {
            clientService.assignNursingLevel(request.getClientId(), request.getNursingLevelId());
            return ApiResponse.success("按等级设置护理项目成功", null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
