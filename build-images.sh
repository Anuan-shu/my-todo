#!/bin/bash

# 构建后端
cd backend/mytodo
mvn clean package
cd ../../

# 构建前端
cd frontend/mytodo
npm run build
cd ../../

# 如果有先删除镜像
docker rmi todo-backend:latest
docker rmi todo-frontend:latest
docker rmi todo-database:latest

# 构建镜像
docker build -t todo-backend:latest -f backend/Dockerfile backend --load
docker build -t todo-frontend:latest -f frontend/Dockerfile frontend --load
docker build -t todo-database:latest -f database/Dockerfile database --load