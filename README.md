# Todo 应用 - SpringBoot + Vue + K8s

这是一个基于SpringBoot + Vue的Todo应用，支持部署到Kubernetes集群中。

## 功能特性

### 1. 任务增删改查（基础核心）
- ✅ 添加新任务（标题 + 内容 + 截止时间）
- ✅ 编辑任务
- ✅ 删除任务
- ✅ 查看所有任务列表

### 2. 任务状态管理
- ✅ 标记任务为"未完成/已完成"
- ✅ 支持按状态筛选（全部/未完成/已完成）
- ✅ 已完成任务显示删除线

### 3. 任务优先级设置
- ✅ 创建任务时可选择优先级（高/中/低）
- ✅ 列表按优先级排序

### 4. 简单用户认证
- ✅ 登录/注册功能
- ✅ 每个用户仅能查看自己的任务
- ✅ 简单密码验证

### 5. 任务到期提醒
- ✅ 登录后自动提示"24小时内到期的未完成任务"

## 技术架构

### 后端技术栈
- **Spring Boot 3.5.5** - 主框架
- **Spring Data JPA** - 数据访问层
- **MySQL 8.0** - 数据库
- **Maven** - 依赖管理

### 前端技术栈
- **Vue 3** - 前端框架
- **Vue Router** - 路由管理
- **Element Plus** - UI组件库
- **Axios** - HTTP客户端
- **Day.js** - 日期处理
- **Vite** - 构建工具

### 部署技术
- **Docker** - 容器化
- **Kubernetes** - 容器编排
- **Nginx** - 反向代理

## 快速开始

### 本地开发

可以执行`start-local.sh`脚本或按照以下步骤启动：

#### 1. 启动MySQL数据库
```bash
# 使用Docker启动MySQL
docker run -d \
  --name mysql-todo \
  -e MYSQL_ROOT_PASSWORD=rootpassword \
  -e MYSQL_DATABASE=todo_db \
  -e MYSQL_USER=todo_user \
  -e MYSQL_PASSWORD=password \
  -p 3306:3306 \
  mysql:8.0

# 执行初始化脚本
mysql -h localhost -P 3306 -u root -prootpassword < database/init.sql
```

#### 2. 启动后端服务
```bash
cd backend/mytodo
mvn clean install
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

#### 3. 启动前端服务
```bash
cd frontend/mytodo
npm install
npm run dev
```

### 访问应用
- 前端: http://localhost:5173
- 后端API: http://localhost:8080
- 默认用户: admin / 123456

## 部署到Kubernetes

### 1. 构建Docker镜像
由于要构建镜像，请保证网络畅通✈️
```bash
chmod +x build-images.sh
bash ./build-images.sh
```

### 2. 部署到K8s
```bash
# 确保kubectl已配置
chmod +x deploy.sh
bash ./deploy-k8s.sh
```

### 3. 访问应用
```txt
访问 http://localhost:30080
```

### 4. 从k8s中删除应用
```bash
chmod +x uninstall-k8s.sh
bash ./uninstall-k8s.sh
```

## 许可证

MIT License 