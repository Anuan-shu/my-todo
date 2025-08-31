#!/bin/bash

echo "开始卸载Todo应用..."

# 确认操作
read -p "确定要卸载Todo应用吗？这将删除所有相关资源 (y/N): " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo "操作已取消"
    exit 1
fi

echo "正在卸载..."

# 删除前端服务
echo "删除前端服务..."
kubectl delete -f k8s/frontend-deployment.yaml --ignore-not-found=true

# 删除后端服务
echo "删除后端服务..."
kubectl delete -f k8s/backend-deployment.yaml --ignore-not-found=true

# 删除MySQL相关资源
echo "删除MySQL资源..."
kubectl delete -f k8s/mysql-deployment.yaml --ignore-not-found=true

# 删除配置和密钥
echo "删除配置和密钥..."
kubectl delete -f k8s/mysql-configmap.yaml --ignore-not-found=true
kubectl delete -f k8s/mysql-secret.yaml --ignore-not-found=true

# 删除命名空间（这会删除命名空间内的所有资源）
echo "删除命名空间..."
kubectl delete namespace todo-app --ignore-not-found=true

# 等待命名空间删除完成
echo "等待命名空间删除完成..."
kubectl wait --for=delete namespace/todo-app --timeout=300s 2>/dev/null || true

echo "卸载完成！"
echo ""
echo "注意："
echo "- 所有Pod、Service、Deployment等资源已被删除"
echo "- 数据库数据已被删除（如果使用临时存储）"
echo "- 如果需要保留数据，请确保使用了PersistentVolume"
echo ""
echo "检查是否还有残留资源："
echo "kubectl get all --all-namespaces | grep todo" 