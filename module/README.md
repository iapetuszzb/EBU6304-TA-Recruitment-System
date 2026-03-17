## BUPT 国际学校 TA 招聘系统（Servlet/JSP 版）

### 技术约束符合说明
- **架构**：轻量级 Java Servlet/JSP Web 应用（无 Spring Boot）。
- **存储**：仅使用纯文本文件（`CSV` + 简历文件），无数据库。
- **依赖**：仅依赖 Servlet 容器（如 Tomcat）提供的 Servlet/JSP API。

### 核心角色与功能
- **TA**：创建/维护申请档案（基础信息）、上传简历、浏览岗位、投递申请、查询申请状态
- **MO（模块组织者）**：发布招聘岗位、查看/筛选申请者（更改申请状态：入围/拒绝）
- **管理员**：查看 TA 整体工作量（按 TA 统计：申请数、入围数）

### 默认账号（首次运行自动生成到 `data/users.csv`）
- TA：`ta` / `ta`
- MO：`mo` / `mo`
- 管理员：`admin` / `admin`

### 运行方式（推荐：Tomcat 9/10）
#### 方式 A：Maven 打包成 war（推荐）
1. 安装 JDK 8+ 与 Maven。
2. 在 `module/` 目录执行：
   - `mvn -q package`
3. 生成 `target/ta-recruit.war`，把它丢进 Tomcat 的 `webapps/`，启动 Tomcat。
4. 访问：`http://localhost:8080/ta-recruit/`

#### 方式 B：IDEA 直接部署（war exploded）
1. 安装 Tomcat。
2. 将本项目作为 **Web Application** 部署：
   - `webapp/` 为 Web 根目录
   - `webapp/WEB-INF/web.xml` 为部署描述符
   - `src/` 下为 Servlet 与业务代码（需编译进 `WEB-INF/classes`）
3. 启动 Tomcat 后访问：`http://localhost:8080/ta-recruit/`

> 如果你使用 IntelliJ IDEA：
> - 新建/配置一个 Tomcat Run Configuration
> - Deployment 选择 “war exploded”
> - Web resource directory 指向 `module/webapp`

### 数据文件位置
运行后会在部署目录的 `WEB-INF/data/` 生成（仍为纯文本文件）：
- `users.csv` 用户
- `profiles.csv` TA 档案
- `jobs.csv` 岗位
- `applications.csv` 申请记录
- `resumes/` 简历文件夹（上传文件会落盘）

