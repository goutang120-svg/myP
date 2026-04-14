# studentMage

学生管理系统（Spring Cloud 微服务 + Vue3）。

## 项目结构

- `backend/gateway`：网关服务，统一入口，端口 `8080`
- `backend/system-service`：系统服务（登录/注册/用户）
- `backend/biz-service`：业务服务（学生/课程/成绩）
- `frontend`：Vue3 + Element Plus 前端，端口 `5173`

## 技术栈

- 后端：Spring Boot 3、Spring Cloud Gateway、Nacos、MyBatis-Plus、MySQL
- 前端：Vue3、Vite、Element Plus、Axios

## 后端 Nacos 配置说明

后端三个服务固定使用 `nacos` profile，并统一接入上游 Nacos：

- Nacos 地址：`62.234.1.152:8848`
- Namespace：`b66c93ce-3c83-4edd-a789-e3f47335c127`

> 配置中不再读取环境变量，也不再提供本地 Nacos 默认地址。

## 启动后端

在 `backend` 目录执行：

```bash
mvn clean package
mvn -pl system-service spring-boot:run
mvn -pl biz-service spring-boot:run
mvn -pl gateway spring-boot:run
```

网关通过 Nacos 服务发现将流量转发到：

- `/api/system/**` -> `system-service`
- `/api/biz/**` -> `biz-service`

## 启动前端

在 `frontend` 目录执行：

```bash
npm install
npm run dev
```

## 默认数据

- 预置管理员账号：
  - 用户名：`admin`
  - 密码：`admin`
- 业务服务预置了 1 条学生、1 条课程、1 条成绩演示数据
