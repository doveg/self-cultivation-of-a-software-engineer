# ElasticSearch 常用命令

## 基础命令

### 测试
GET /_search
{
  "query": {
    "match_all": {}
  }
}


### 查看许可证
GET /_license


### 查看各个节点的版本号
GET /_cat/nodes?v&h=host,name,version


### 查看集群设置
GET /_cluster/settings


### 查看分片状态
GET /_cat/shards?v&pretty


### 查看集群状态
GET /_cluster/health?pretty


### 查看索引列表

GET /_cat/indices?v


### 新建索引
PUT /test_index
{
  "settings": {
    "index": {
      "number_of_shards": 1,
      "number_of_replicas": 0
    }
  }
}

### 删除索引
DELETE /test_index




### 索引数据迁移
POST /_reindex
{
  "source": {
    "index": "my-index-000001"
  },
  "dest": {
    "index": "my-new-index-000001"
  }
}




