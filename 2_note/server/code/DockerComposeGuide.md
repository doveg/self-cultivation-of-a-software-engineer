# 使用

### 首次启动

	docker-compose up -d && docker-compose logs -f

### 更新

	docker-compose down --remove-orphans && docker-compose pull \
	&& docker-compose up -d && docker-compose logs -f

### 重置环境

	docker-compose down --remove-orphans && docker-compose pull \
	&& rm -rf data \
	&& docker-compose up -d && docker-compose logs -f

