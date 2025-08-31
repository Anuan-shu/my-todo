#!/bin/bash

echo "开始部署Todo应用到Kubernetes..."

# 创建命名空间
echo "创建命名空间..."
kubectl apply -f k8s/namespace.yaml

# 部署MySQL
echo "部署MySQL..."
kubectl apply -f k8s/mysql-secret.yaml
kubectl apply -f k8s/mysql-configmap.yaml
kubectl apply -f k8s/mysql-deployment.yaml

# 等待MySQL就绪
echo "等待MySQL就绪..."
kubectl wait --for=condition=ready pod -l app=todo-database -n todo-app --timeout=300s

# 部署后端
echo "部署后端服务..."
kubectl apply -f k8s/backend-deployment.yaml

# 部署前端
echo "部署前端服务..."
kubectl apply -f k8s/frontend-deployment.yaml

# 等待服务就绪
echo "等待服务就绪..."
kubectl wait --for=condition=ready pod -l app=todo-backend -n todo-app --timeout=300s
kubectl wait --for=condition=ready pod -l app=todo-frontend -n todo-app --timeout=300s

echo "部署完成！"
echo "查看服务状态：kubectl get all -n todo-app"
echo "查看Pod日志：kubectl logs -f deployment/todo-backend -n todo-app"