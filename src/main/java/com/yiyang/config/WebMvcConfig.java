package com.yiyang.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置 - 注册登录拦截器
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录拦截器：所有 /page/** 路由需要登录
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/page/**")
                .excludePathPatterns("/page/login", "/page/logout");

        // 角色权限拦截器：API 写操作按 ADMIN/STAFF 分权
        registry.addInterceptor(new RoleInterceptor())
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/operators/login");
    }
}
