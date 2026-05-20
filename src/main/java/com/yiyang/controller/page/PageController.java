package com.yiyang.controller.page;

import com.yiyang.dto.ApiResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 页面路由 Controller - 负责所有 Thymeleaf 页面的跳转
 */
@Controller
@RequestMapping("/page")
public class PageController {

    // ========== 登录 ==========

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/page/login";
    }

    /**
     * 设置用户 Session
     * 注意：此方法已废弃，登录后由 OperatorController.login 直接设置 Session。
     * 保留仅用于兼容旧前端，且走拦截器校验，只有已登录用户才能调用。
     */
    @Deprecated
    @ResponseBody
    @PostMapping("/api/set-session")
    public ApiResponse<String> setSession(@RequestBody Map<String, Object> user, HttpSession session) {
        session.setAttribute("loginUser", user);
        return ApiResponse.success("登录成功", "ok");
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    // ========== 护理项目 ==========

    @GetMapping("/nursing-items")
    public String nursingItems() {
        return "nursing/list";
    }

    @GetMapping("/nursing-items/add")
    public String nursingItemAdd() {
        return "nursing/add";
    }

    @GetMapping("/nursing-items/edit")
    public String nursingItemEdit() {
        return "nursing/edit";
    }

    // ========== 护理等级 ==========

    @GetMapping("/nursing-levels")
    public String nursingLevels() {
        return "level/list";
    }

    @GetMapping("/nursing-levels/add")
    public String nursingLevelAdd() {
        return "level/add";
    }

    @GetMapping("/nursing-levels/edit")
    public String nursingLevelEdit() {
        return "level/edit";
    }

    // ========== 床位管理 ==========

    @GetMapping("/beds")
    public String beds() {
        return "bed/list";
    }

    @GetMapping("/beds/add")
    public String bedAdd() {
        return "bed/add";
    }

    // ========== 老人管理 ==========

    @GetMapping("/clients")
    public String clients() {
        return "client/list";
    }

    @GetMapping("/clients/add")
    public String clientAdd() {
        return "client/add";
    }

    @GetMapping("/clients/edit")
    public String clientEdit() {
        return "client/edit";
    }

    @GetMapping("/clients/nursing")
    public String clientNursing() {
        return "client/nursing";
    }

    // ========== 护理记录 ==========

    @GetMapping("/nursing-records")
    public String nursingRecords() {
        return "record/list";
    }

    @GetMapping("/nursing-records/add")
    public String nursingRecordAdd() {
        return "record/add";
    }

    // ========== 退住申请 ==========

    @GetMapping("/check-out-applications")
    public String checkOutApplications() {
        return "checkout/list";
    }

    @GetMapping("/check-out-applications/add")
    public String checkOutApplicationAdd() {
        return "checkout/add";
    }

    // ========== 请假 ==========

    @GetMapping("/leave-applications")
    public String leaveApplications() {
        return "leave/list";
    }

    @GetMapping("/leave-applications/add")
    public String leaveApplicationAdd() {
        return "leave/add";
    }

    // ========== 操作员 ==========

    @GetMapping("/operators")
    public String operators() {
        return "operator/list";
    }
}
