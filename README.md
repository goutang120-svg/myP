# studentMage

学生管理系统（Spring Cloud 微服务 + Vue3）。

## 项目结构

- `backend/gateway`：网关服务，统一入口，端口 `8080`
- `backend/system-service`：系统服务（登录/注册/用户），端口 `8081`
- `backend/biz-service`：业务服务（学生/课程/成绩），端口 `8082`
- `frontend`：Vue3 + Element Plus 前端，端口 `5173`

## 技术栈

- 后端：Spring Boot 3、Spring Cloud Gateway、MyBatis-Plus、MySQL
- 前端：Vue3、Vite、Element Plus、Axios

## 启动后端

在 `backend` 目录执行：

```bash
mvn clean package
```

分别启动三个服务：

```bash
mvn -pl gateway spring-boot:run
mvn -pl system-service spring-boot:run
mvn -pl biz-service spring-boot:run
```

## 启动前端

在 `frontend` 目录执行：

```bash
npm install
npm run dev
```

## 数据库配置

当前后端已配置连接：

- 地址：`65.234.1.152:3306`
- 数据库：`studentManger`
- 用户名：`root`
- 密码：`Root123456!`

如需修改，编辑：

- `backend/system-service/src/main/resources/application.yml`
- `backend/biz-service/src/main/resources/application.yml`

## 默认数据

- 预置管理员账号：
  - 用户名：`admin`
  - 密码：`admin`
- 业务服务预置了 1 条学生、1 条课程、1 条成绩演示数据

## 网关接口前缀

- 系统服务：`/api/system/**`
- 业务服务：`/api/biz/**`
