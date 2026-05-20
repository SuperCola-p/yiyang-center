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
- 2026-05-20: 修复"点击分选项后进入空白页"核心bug（layout.html中${title}变量未设置导致Thymeleaf渲染异常）
  - layout.html 使用 `${title} ?:` 默认值
  - PageController 所有方法添加 model.addAttribute("title", ...)
- 2026-05-20: 全面UI美化（仿login渐变、圆角、阴影风格）
  - style.css 重写：渐变导航栏、圆角12px卡片、渐变按钮/badge、表单聚焦光影、悬浮效果
  - layout.html 升级：导航栏渐变背景+阴影、下拉菜单美化
  - 所有22个管理页面使用 .page-title / .search-bar / card-header图标 / modal-header图标
- 2026-05-20: common.js 新增 showLoading() / showEmpty() 工具函数
- **样式约定**：全局CSS中包含 .page-title(渐变下划线边框) / .search-bar(白色卡片微阴影) / .loading-state(居中灰色图标) / .empty-state(居中大图标托底) / .quick-entry-card(悬浮上移动画) — 所有页面统一使用
- 2026-05-20: 全面优化排版与背景
  - body.login-page 改为全屏 flex 居中容器（display:flex），渐变背景直接设在 body 上
  - 登录页装饰性浮动气泡使用 body::before/::after 伪元素（原 login-wrapper 伪元素移除）
  - 修复 body `padding-top:56px` 与登录页兼容（body.login-page 重置为 padding-top:0）
  - 新增 .toast-notification slideInRight 动画
  - 新增 DataTables 分页、Select2、表格首末列内边距兼容样式
  - 响应式：登录卡片移动端适配 max-width:95%，padding 压缩
