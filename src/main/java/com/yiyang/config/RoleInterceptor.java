package com.yiyang.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 角色权限拦截器
 * <p>
 * ADMIN：全部权限
 * STAFF：仅允许
 *   - 护理记录（GET/POST/PUT/DELETE /api/nursing-records/**）
 *   - 归院登记（PUT /api/leave-applications/{id}/return）
 *   - 提交请假申请（POST /api/leave-applications）
 *   - 提交退住申请（POST /api/check-out-applications）
 *   - 所有只读 GET 请求（查询、列表）
 * </p>
 * STAFF 被禁止：
 *   - 审批接口 (/audit)
 *   - 老人管理写操作 (POST/PUT/DELETE /api/clients/**, /api/clients/assign-level)
 *   - 操作员管理写操作 (POST/PUT/DELETE /api/operators)
 *   - 护理项目写操作 (POST/PUT/DELETE /api/nursing-items/**)
 *   - 护理等级写操作 (POST/PUT/DELETE /api/nursing-levels/**)
 *   - 床位管理写操作 (POST/PUT/DELETE /api/beds/**)
 *   - 护理服务设置写操作 (POST/PUT/DELETE /api/client-nursing-settings/**)
 */
public class RoleInterceptor implements HandlerInterceptor {

    @Override
    @SuppressWarnings("unchecked")
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession session = request.getSession(false);
        if (session == null) return true; // 交给 LoginInterceptor 处理

        Map<String, Object> loginUser = (Map<String, Object>) session.getAttribute("loginUser");
        if (loginUser == null) return true;

        String operatorType = (String) loginUser.get("operatorType");
        // ADMIN 全放行
        if ("ADMIN".equals(operatorType)) return true;

        // STAFF 限制检查
        String method = request.getMethod().toUpperCase();
        String path = request.getServletPath();

        // GET 请求全部允许
        if ("GET".equals(method)) return true;

        // ========== STAFF 允许的写操作 ==========

        // 1. 护理记录 - 全部允许（新增记录是 STAFF 核心业务）
        if (path.startsWith("/api/nursing-records")) return true;

        // 2. 提交请假申请 (POST /api/leave-applications)
        if ("POST".equals(method) && "/api/leave-applications".equals(path)) return true;

        // 3. 归院登记 (PUT /api/leave-applications/{id}/return)
        if ("PUT".equals(method) && path.matches("/api/leave-applications/[^/]+/return")) return true;

        // 4. 提交退住申请 (POST /api/check-out-applications)
        if ("POST".equals(method) && "/api/check-out-applications".equals(path)) return true;

        // ========== STAFF 禁止的操作，返回 403 ==========

        // 审批操作（退住/请假）
        if (path.contains("/audit")) {
            sendForbidden(response, "仅管理员可执行审批操作");
            return false;
        }

        // 老人管理写操作
        if (path.startsWith("/api/clients")) {
            sendForbidden(response, "仅管理员可管理老人信息");
            return false;
        }

        // 操作员管理写操作
        if (path.startsWith("/api/operators")) {
            sendForbidden(response, "仅管理员可管理操作员");
            return false;
        }

        // 护理项目写操作
        if (path.startsWith("/api/nursing-items")) {
            sendForbidden(response, "仅管理员可管理护理项目");
            return false;
        }

        // 护理等级写操作
        if (path.startsWith("/api/nursing-levels")) {
            sendForbidden(response, "仅管理员可管理护理等级");
            return false;
        }

        // 床位管理写操作
        if (path.startsWith("/api/beds")) {
            sendForbidden(response, "仅管理员可管理床位信息");
            return false;
        }

        // 护理服务设置写操作
        if (path.startsWith("/api/client-nursing-settings")) {
            sendForbidden(response, "仅管理员可管理护理服务设置");
            return false;
        }

        // 其他 POST/PUT/DELETE 默认允许（兜底）
        return true;
    }

    private void sendForbidden(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(
                "{\"code\":403,\"success\":false,\"message\":\"权限不足：" + message + "\"}"
        );
    }
}
