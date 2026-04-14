# Nacos 配置模板（用于 dev/test/prod 独立 namespace）

本目录用于**存放将要发布到 Nacos 的配置模板**，方便团队协作与版本管理。

## 推荐约定
- **Namespace**：dev / test / prod 各一个（彼此隔离）
- **Group**：`STUDENT_MAGE`（与 `bootstrap.yml` 中 `NACOS_GROUP` 一致）
- **DataId**：
  - `common.yaml`（公共配置，所有服务共享）
  - `gateway.yaml`
  - `system-service.yaml`
  - `biz-service.yaml`

## 发布方式（手动）
在 Nacos 控制台对应的 namespace 下，按上面的 DataId/Group 新建配置，格式选择 `YAML`，将本目录同名文件内容粘贴进去即可。

## 必要提醒（否则 Nacos 控制台看不到实例）
- 项目默认 `spring.profiles.active: local`，并且在 `local` profile 下**禁用了** Nacos 注册/配置。\n
  所以要注册到 Nacos，启动时必须使用 `nacos` profile（例如 `-Dspring-boot.run.profiles=nacos`）。\n
- Spring Boot 3 默认不一定加载 `bootstrap.yml`，项目已补齐 `spring-cloud-starter-bootstrap` 以确保 `bootstrap.yml` 生效。\n

## 运行时注入（建议）
运行服务时通过环境变量注入（示例名，可按你的部署平台改造）：\n
- `NACOS_ADDR`\n
- `NACOS_NAMESPACE`\n
- `NACOS_GROUP`（默认 `STUDENT_MAGE`）\n
- `NACOS_USERNAME` / `NACOS_PASSWORD`（如启用鉴权）\n

> 注意：本目录是模板与约定，不会自动把配置推送到 Nacos。

