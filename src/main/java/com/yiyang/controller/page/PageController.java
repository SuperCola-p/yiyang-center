package com.yiyang.controller.page;

import com.yiyang.dto.ApiResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
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
    public String home(Model model) {
        model.addAttribute("title", "首页 - 东软颐养中心");
        return "home";
    }

    // ========== 护理项目（仅 ADMIN）==========

    @GetMapping("/nursing-items")
    public String nursingItems(Model model, HttpSession session, HttpServletResponse response) throws Exception {
        if (!isAdmin(session)) { response.sendRedirect("/page/home"); return null; }
        model.addAttribute("title", "护理项目管理 - 东软颐养中心");
        return "nursing/list";
    }

    @GetMapping("/nursing-items/add")
    public String nursingItemAdd(Model model, HttpSession session, HttpServletResponse response) throws Exception {
        if (!isAdmin(session)) { response.sendRedirect("/page/home"); return null; }
        model.addAttribute("title", "新增护理项目 - 东软颐养中心");
        return "nursing/add";
    }

    @GetMapping("/nursing-items/edit")
    public String nursingItemEdit(Model model, HttpSession session, HttpServletResponse response) throws Exception {
        if (!isAdmin(session)) { response.sendRedirect("/page/home"); return null; }
        model.addAttribute("title", "编辑护理项目 - 东软颐养中心");
        return "nursing/edit";
    }

    // ========== 护理等级（仅 ADMIN）==========

    @GetMapping("/nursing-levels")
    public String nursingLevels(Model model, HttpSession session, HttpServletResponse response) throws Exception {
        if (!isAdmin(session)) { response.sendRedirect("/page/home"); return null; }
        model.addAttribute("title", "护理等级管理 - 东软颐养中心");
        return "level/list";
    }

    @GetMapping("/nursing-levels/add")
    public String nursingLevelAdd(Model model, HttpSession session, HttpServletResponse response) throws Exception {
        if (!isAdmin(session)) { response.sendRedirect("/page/home"); return null; }
        model.addAttribute("title", "新增护理等级 - 东软颐养中心");
        return "level/add";
    }

    @GetMapping("/nursing-levels/edit")
    public String nursingLevelEdit(Model model, HttpSession session, HttpServletResponse response) throws Exception {
        if (!isAdmin(session)) { response.sendRedirect("/page/home"); return null; }
        model.addAttribute("title", "编辑护理等级 - 东软颐养中心");
        return "level/edit";
    }

    // ========== 床位管理（仅 ADMIN）==========

    @GetMapping("/beds")
    public String beds(Model model, HttpSession session, HttpServletResponse response) throws Exception {
        if (!isAdmin(session)) { response.sendRedirect("/page/home"); return null; }
        model.addAttribute("title", "床位管理 - 东软颐养中心");
        return "bed/list";
    }

    @GetMapping("/beds/add")
    public String bedAdd(Model model, HttpSession session, HttpServletResponse response) throws Exception {
        if (!isAdmin(session)) { response.sendRedirect("/page/home"); return null; }
        model.addAttribute("title", "新增床位 - 东软颐养中心");
        return "bed/add";
    }

    // ========== 老人管理（仅 ADMIN）==========

    @GetMapping("/clients")
    public String clients(Model model, HttpSession session, HttpServletResponse response) throws Exception {
        if (!isAdmin(session)) { response.sendRedirect("/page/home"); return null; }
        model.addAttribute("title", "老人管理 - 东软颐养中心");
        return "client/list";
    }

    @GetMapping("/clients/add")
    public String clientAdd(Model model, HttpSession session, HttpServletResponse response) throws Exception {
        if (!isAdmin(session)) { response.sendRedirect("/page/home"); return null; }
        model.addAttribute("title", "新增老人 - 东软颐养中心");
        return "client/add";
    }

    @GetMapping("/clients/edit")
    public String clientEdit(Model model, HttpSession session, HttpServletResponse response) throws Exception {
        if (!isAdmin(session)) { response.sendRedirect("/page/home"); return null; }
        model.addAttribute("title", "编辑老人信息 - 东软颐养中心");
        return "client/edit";
    }

    @GetMapping("/clients/nursing")
    public String clientNursing(Model model, HttpSession session, HttpServletResponse response) throws Exception {
        if (!isAdmin(session)) { response.sendRedirect("/page/home"); return null; }
        model.addAttribute("title", "老人护理服务设置 - 东软颐养中心");
        return "client/nursing";
    }

    // ========== 护理记录（STAFF/ADMIN 均可）==========

    @GetMapping("/nursing-records")
    public String nursingRecords(Model model) {
        model.addAttribute("title", "护理记录管理 - 东软颐养中心");
        return "record/list";
    }

    @GetMapping("/nursing-records/add")
    public String nursingRecordAdd(Model model) {
        model.addAttribute("title", "新增护理记录 - 东软颐养中心");
        return "record/add";
    }

    // ========== 退住申请（STAFF/ADMIN 均可查看和提交；审批仅 ADMIN）==========

    @GetMapping("/check-out-applications")
    public String checkOutApplications(Model model) {
        model.addAttribute("title", "退住申请管理 - 东软颐养中心");
        return "checkout/list";
    }

    @GetMapping("/check-out-applications/add")
    public String checkOutApplicationAdd(Model model) {
        model.addAttribute("title", "新增退住申请 - 东软颐养中心");
        return "checkout/add";
    }

    // ========== 请假（STAFF/ADMIN 均可查看和提交；审批仅 ADMIN）==========

    @GetMapping("/leave-applications")
    public String leaveApplications(Model model) {
        model.addAttribute("title", "请假管理 - 东软颐养中心");
        return "leave/list";
    }

    @GetMapping("/leave-applications/add")
    public String leaveApplicationAdd(Model model) {
        model.addAttribute("title", "新增请假 - 东软颐养中心");
        return "leave/add";
    }

    // ========== 操作员（仅 ADMIN）==========

    @GetMapping("/operators")
    public String operators(Model model, HttpSession session, HttpServletResponse response) throws Exception {
        if (!isAdmin(session)) { response.sendRedirect("/page/home"); return null; }
        model.addAttribute("title", "操作员管理 - 东软颐养中心");
        return "operator/list";
    }

    // ========== 工具方法 ==========

    @SuppressWarnings("unchecked")
    private boolean isAdmin(HttpSession session) {
        if (session == null) return false;
        Map<String, Object> loginUser = (Map<String, Object>) session.getAttribute("loginUser");
        if (loginUser == null) return false;
        return "ADMIN".equals(loginUser.get("operatorType"));
    }
}
