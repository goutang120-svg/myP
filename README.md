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

## 后端已切换为 Nacos

后端三个服务默认使用 `nacos` profile 启动（`SPRING_PROFILES_ACTIVE` 默认值为 `nacos`）。

### 1) 启动 Nacos

可使用 Docker 快速启动：

```bash
docker run --name nacos-standalone \
  -e MODE=standalone \
  -p 8848:8848 \
  -d nacos/nacos-server:v2.3.2
```

### 2) 配置环境变量（可选）

```bash
export NACOS_SERVER_ADDR=127.0.0.1:8848
export NACOS_NAMESPACE=public
```

> 若不配置，服务默认读取：`NACOS_SERVER_ADDR=127.0.0.1:8848`、`NACOS_NAMESPACE=public`。

### 3) 启动后端

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

### 4) 本地无 Nacos 的临时运行方式

如果你暂时不想依赖 Nacos，可切回本地 profile：

```bash
export SPRING_PROFILES_ACTIVE=local
```

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
