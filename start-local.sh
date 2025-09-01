#!/bin/bash

echo "启动本地开发环境..."

# 检查Docker是否运行
if ! docker info > /dev/null 2>&1; then
    echo "错误: Docker未运行，请先启动Docker"
    exit 1
fi

# 启动MySQL
echo "启动MySQL数据库..."
docker run -d \
  --name mysql-todo \
  -e MYSQL_ROOT_PASSWORD=rootpassword \
  -e MYSQL_DATABASE=todo_db \
  -e MYSQL_USER=todo_user \
  -e MYSQL_PASSWORD=password \
  -p 3306:3306 \
  mysql:8.0

# 等待MySQL启动
echo "等待MySQL启动..."
sleep 30

# 执行数据库初始化脚本
echo "初始化数据库..."
docker exec -i mysql-todo mysql -u root -prootpassword < database/init.sql

echo "MySQL启动完成！"
echo "数据库连接信息:"
echo "  Host: localhost"
echo "  Port: 3306"
echo "  Database: todo_db"
echo "  Username: todo_user"
echo "  Password: password"
echo "  Root Password: rootpassword"
echo ""
echo "接下来请手动启动后端和前端服务:"
echo "1. 后端: cd backend/mytodo && mvn clean dependency:resolve && mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev""
echo "2. 前端: cd frontend/mytodo && npm install && npm run dev" 