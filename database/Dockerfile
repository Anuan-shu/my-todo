FROM mysql:8.0

# 复制初始化脚本
COPY init.sql /docker-entrypoint-initdb.d/init.sql

# 设置时区
ENV TZ=Asia/Shanghai

# 暴露端口
EXPOSE 3306