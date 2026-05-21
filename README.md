# 东软颐养中心管理系统 (yiyang-center)

一个基于 Spring Boot 2.7 + Thymeleaf + Bootstrap 4 的养老院后台管理系统，提供 **网页端** 和 **控制台端** 双入口操作。

---

## 功能模块

| 模块 | 说明 |
|------|------|
| 🏥 护理项目管理 | 护理项目的增删改查、启用/停用 |
| 📊 护理等级管理 | 护理等级维护，与护理项目的多对多关联 |
| 🛏️ 床位管理 | 楼栋-房间-床位三级结构，空闲/入住/维修状态 |
| 👴 老人管理 | 老人信息录入/编辑，护理服务购买和到期管理 |
| 📋 护理记录 | 护理执行记录登记与查询 |
| 📤 退住申请 | 退住申请提交、审核（通过/拒绝） |
| 🚶 请假管理 | 请假申请、审核、归院登记 |
| 👤 操作员管理 | 管理员账号管理 |

---

## 环境要求

| 软件 | 版本 |
|------|------|
| JDK | 1.8+ |
| Maven | 3.6+ |
| MySQL | 8.0+ |
| Node.js | 无需（纯后端 + Thymeleaf 模板） |

---

## 快速启动

### 1. 创建数据库

MySQL 中手动创建数据库（或在应用启动时自动创建）：

```sql
CREATE DATABASE IF NOT EXISTS yiyang_center
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;
```

> **可选**：`src/main/resources/schema.sql` 中包含了完整的建表语句和初始管理员数据，可在生产环境使用 `ddl-auto: none` 时手动执行。

### 2. 修改数据库配置（可选）

打开 `src/main/resources/application.yml`，按实际情况修改：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/yiyang_center?...
    username: root        # 你的 MySQL 用户名
    password: YourPasswordHere       # 你的 MySQL 密码
```

### 3. 编译

```bash
cd D:\Coding_File\Java\yiyang-center1
mvn clean compile
```

> 编译通过后，target 目录下会生成 class 文件。

### 4. 启动

```bash
mvn spring-boot:run
```

或先打包再运行：

```bash
mvn clean package -DskipTests
java -jar target/yiyang-center-1.0.0.jar
```

启动日志末尾应看到：

```
Started Application in X.XXX seconds
```

---

## 使用方式

系统提供 **两套操作入口**，可按需切换：

### 🌐 网页端

启动后浏览器访问：

```
http://localhost:8080/page/login
```

**默认管理员账号：**

| 登录账号 | 密码 | 角色 |
|----------|------|------|
| `admin` | `123456` | 系统管理员 |

登录后可通过顶部导航栏进入各功能模块：

1. **首页** — 统计概览（在住老人数、空闲床位数、护理项目数、待审核申请数）
2. **护理管理** → 护理项目 / 护理等级 / 护理记录 / 护理服务设置
3. **床位管理** — 床位列表与新增
4. **老人管理** — 老人信息维护（增删改）
5. **申请管理** → 退住申请 / 请假管理（提交 + 审核）
6. **操作员** — 操作员账号管理（增删改）

### 💻 控制台端

系统启动后，在运行 `mvn spring-boot:run` 的**终端窗口中**直接输入 `menu` 命令：

```
shell:>menu
```

即可显示主菜单：

```
╔════════════════════════════════════╗
║    东软颐养中心管理系统            ║
║          主菜单                    ║
╚════════════════════════════════════╝
1. 护理项目管理
2. 护理等级管理
3. 床位管理
4. 老人管理
5. 护理记录
6. 退住申请管理
7. 请假管理
8. 操作员管理
0. 退出
请选择:
```

输入数字编号进入子菜单，在每个子菜单中选择 `0` 返回上级菜单。

> **注意**：控制台端在 IDEA 或 VS Code 内置终端中可能会出现显示异常，推荐在独立命令行窗口（cmd / PowerShell / Git Bash）中运行。

---

## 项目结构

```
yiyang-center1/
├── src/main/java/com/yiyang/
│   ├── Application.java              # 启动类
│   ├── config/
│   │   ├── LoginInterceptor.java      # 登录拦截器
│   │   └── WebMvcConfig.java          # Web MVC 配置
│   ├── controller/
│   │   ├── api/                       # REST API 控制器
│   │   └── page/
│   │       └── PageController.java    # Thymeleaf 页面路由
│   ├── dto/                           # 数据传输对象
│   ├── entity/                        # JPA 实体类
│   ├── repository/                    # 数据访问层
│   ├── service/                       # 业务逻辑层
│   └── shell/
│       └── MainMenuCommand.java       # Spring Shell 控制台菜单
│
├── src/main/resources/
│   ├── application.yml                # 应用配置
│   ├── schema.sql                     # 建表 SQL 参考脚本
│   ├── static/
│   │   ├── css/style.css              # 全局样式
│   │   └── js/common.js              # 通用 JS 工具
│   └── templates/
│       ├── fragments/layout.html      # 公共布局模板
│       ├── login.html                 # 登录页
│       ├── home.html                  # 首页
│       ├── nursing/                   # 护理项目管理
│       ├── level/                     # 护理等级管理
│       ├── bed/                       # 床位管理
│       ├── client/                    # 老人管理
│       ├── record/                    # 护理记录
│       ├── checkout/                  # 退住申请
│       ├── leave/                     # 请假管理
│       └── operator/                  # 操作员管理
│
├── sql/
│   └── init.sql                      # 初始化 SQL 脚本
│
└── pom.xml                            # Maven 构建配置
```

---

## 开发说明

### ddl-auto 策略

当前使用 `spring.jpa.hibernate.ddl-auto=update`，每次启动时 Hibernate 会自动对比实体类与数据库表结构，自动建表或加字段。

**生产环境请改为 `none`**，使用 `schema.sql` 手动建表。

### Thymeleaf 模板缓存

开发时已禁用 Thymeleaf 缓存（`spring.thymeleaf.cache=false`），修改 HTML 页面后刷新浏览器即可看到效果，无需重启。

### 登录拦截

所有 `/page/**` 路径（除登录页和登出外）由 `LoginInterceptor` 拦截，未登录时自动跳转到登录页。

### Swagger 文档

启动后访问：

```
http://localhost:8080/swagger-ui.html
```

可查看所有 REST API 文档。

---

## 常见问题

**Q: 启动报错 `Access denied for user 'root'@'localhost'`**

检查 `application.yml` 中的数据库用户名和密码是否正确，确认 MySQL 服务已启动。

**Q: 启动报错 `Unknown database 'yiyang_center'`**

MySQL 中创建数据库或使用 `ddl-auto: update`（会自动创建）。

**Q: 页面加载后显示白屏或 500 错误**

查看控制台日志中的异常信息，常见原因：
- 数据库连接失败
- 实体类与表结构不匹配（删除 target 目录重新 `mvn compile`）

**Q: 控制台菜单输入 `menu` 没反应**

确保在运行应用的那个终端窗口中输入。Spring Shell 仅在启动应用的终端中可用。

---

## 技术栈

| 技术 | 用途 |
|------|------|
| Spring Boot 2.7.18 | 应用框架 |
| Spring Data JPA + Hibernate | ORM / 数据访问 |
| MySQL 8.0 | 数据库 |
| Thymeleaf | 服务端模板引擎 |
| Bootstrap 4 | 前端 UI 框架 |
| jQuery | 前端 Ajax 交互 |
| Spring Shell 2.1.13 | 控制台交互式菜单 |
| SpringDoc OpenAPI | API 文档 (Swagger) |
| Lombok | 简化实体类代码 |
