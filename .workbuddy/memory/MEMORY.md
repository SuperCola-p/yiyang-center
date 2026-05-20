# MEMORY.md - 长期记忆

## 项目概览
- **项目**：yiyang-center（东软颐养中心管理系统）
- **类型**：Spring Boot 2.7 + Thymeleaf + Bootstrap 4 + Spring Shell 双入口（网页+控制台）
- **技术栈**：Spring Data JPA / MySQL 8 / Lombok / SpringDoc OpenAPI
- **启动类**：`com.yiyang.Application`
- **数据库**：`yiyang_center`，MySQL root/root，端口 3306
- **应用端口**：8080
- **Java 版本**：1.8

## 页面路由
- 所有 Thymeleaf 页面路由在 `PageController.java`（`controller/page/` 包）
- 公共布局模板：`templates/fragments/layout.html`（Bootstrap 4 导航栏）
- 登录拦截器拦截 `/page/**` 路径（排除 login/logout）
- 22 个页面文件分布在 8 个模块目录

## 控制台菜单
- `shell/MainMenuCommand.java` — 约 790 行，9 级交互式菜单（主菜单 + 8 个子菜单）
- 使用 `menu` 命令进入主菜单
- Repository 方法名注意：不使用 `findByIsDeletedFalse()`，使用 `findAll().stream().filter()` 手动过滤

## 开发阶段配置
- `ddl-auto: update`（自动建表）
- Thymeleaf 缓存关闭（`cache: false`）
- 管理员默认账号：admin / 123456
- 默认密码是 BCrypt 加密的 `$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy`

## 工作记忆
- 2026-05-20: 完整前端开发完成（Thymeleaf 22 页面 + Spring Shell 控制台菜单 + 静态资源），编译通过
- 2026-05-20: 生成 README.md，记录完整操作说明
- 2026-05-20: 修复 3 个 P0 问题（Session注入漏洞、BCrypt密码加密、护理等级分配前后端不匹配）
  - OperatorController.login 注入 HttpSession 直接设 loginUser，移除前端 setSession 回调
  - OperatorServiceImpl 引入 BCryptPasswordEncoder，登录改为 findById+matches() 方式
  - client/list.html 护理等级 API 从 PUT /{id}/nursing-level 改为 POST /assign-level，传 nursingLevelId
