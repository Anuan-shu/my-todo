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
- ✅ Session会话认证
- ✅ 每个用户仅能查看自己的任务
- ✅ 简单密码验证

### 5. 任务到期提醒
- ✅ 登录后自动提示"24小时内到期的未完成任务"

## 技术架构

### 后端技术栈
- **Spring Boot 3.5.5** - 主框架
- **Spring Data JPA** - 数据访问层
- **MySQL 8.0** - 数据库
- **Session认证** - 简单会话管理
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

## 项目结构

```
my-todo/
├── backend/                 # 后端项目
│   ├── mytodo/
│   │   ├── src/main/java/com/hamza/mytodo/
│   │   │   ├── controller/     # 控制器层
│   │   │   ├── service/        # 服务层
│   │   │   ├── repository/     # 数据访问层
│   │   │   ├── entity/         # 实体类
│   │   │   └── dto/            # 数据传输对象
│   │   ├── pom.xml
│   │   └── Dockerfile
├── frontend/                # 前端项目
│   ├── mytodo/
│   │   ├── src/
│   │   │   ├── views/          # 页面组件
│   │   │   ├── components/     # 通用组件
│   │   │   ├── router/         # 路由配置
│   │   │   └── api/            # API服务
│   │   ├── package.json
│   │   └── Dockerfile
├── database/                # 数据库脚本
│   └── init.sql
├── k8s/                     # Kubernetes配置
│   ├── namespace.yaml
│   ├── mysql-*.yaml
│   ├── backend-deployment.yaml
│   └── frontend-deployment.yaml
├── deploy.sh                # 部署脚本
├── start-local.sh           # 本地开发启动脚本
└── README.md                # 详细文档
```

## 快速开始

### 本地开发

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
mvn spring-boot:run
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
```bash
# 构建后端镜像
cd backend/mytodo
mvn clean package
docker build -t todo-backend:latest .

# 构建前端镜像
cd frontend/mytodo
npm run build
docker build -t todo-frontend:latest .
```

### 2. 部署到K8s
```bash
# 确保kubectl已配置
chmod +x deploy.sh
./deploy.sh
```

### 3. 访问应用
```bash
# 端口转发
kubectl port-forward service/todo-frontend 8080:80 -n todo-app

# 访问 http://localhost:8080
```

## API接口

### 认证接口
- `POST /api/auth/register` - 用户注册
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/logout` - 用户退出
- `GET /api/auth/check` - 检查认证状态

### 任务接口
- `GET /api/todos` - 获取所有任务
- `GET /api/todos/status/{status}` - 按状态获取任务
- `POST /api/todos` - 创建任务
- `PUT /api/todos/{id}` - 更新任务
- `DELETE /api/todos/{id}` - 删除任务
- `GET /api/todos/upcoming` - 获取即将到期的任务

## 数据库设计

### users表
- id: 主键
- username: 用户名（唯一）
- password: 密码（明文存储，仅用于演示）
- created_at: 创建时间
- updated_at: 更新时间

### todos表
- id: 主键
- title: 任务标题
- content: 任务内容
- deadline: 截止时间
- status: 状态（PENDING/COMPLETED）
- priority: 优先级（LOW/MEDIUM/HIGH）
- user_id: 用户ID（外键）
- created_at: 创建时间
- updated_at: 更新时间

## 开发规范

- 遵循函数式编程原则
- 使用纯函数，避免隐藏状态
- 严格类型检查，避免Any类型
- 遵循DRY、KISS、YAGNI原则
- 使用虚拟环境管理依赖

## 许可证

MIT License 