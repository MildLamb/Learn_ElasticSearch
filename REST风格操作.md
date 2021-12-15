# 基本Rest命令说明
| method      | url地址 | 描述 |
| :-----------: | :-----------: | :-----------: |
| PUT      | localhost:9200/索引名称/类型名称/文档id  | 创建文档(指定文档id) |
| POST     | localhost:9200/索引名称/类型名称        | 创建文档(随机文档id) |
| POST     | localhost:9200/索引名称/类型名称/文档id/_update   | 修改文档   |
| DELETE   | localhost:9200/索引名称/类型名称/文档id     |   删除文档    |
|  GET     | localhost:9200/索引名称/类型名称/文档id     |  通过文档id查询文档 |
|  POST    | localhost:9200/索引名称/类型名称/_search   |  查询所有数据    |

# 命令实操
### 创建文档
1. 先进入kibana的开发工具页面
2. 输入命令 PUT /索引名称/类型名称/文档id
```JavaScript
PUT /test1/type1/1
{
  "name": "mildlamb温柔小羊",
  "age": 1500
}
```
3. 可以在elasticsearch-head数据浏览中找到对应的索引查看到刚刚添加的数据

### 创建具体的索引规则
- elasticsearch数据类型
  - 字符串类型(text,keyword)
  - 数值类型(long,integer,short,byte,double,float,half_float,scaled_float)
  - 日期类型(date)
  - 布尔类型(boolean)
  - 二进制类型(binary)

```JavaScript
PUT /test2
{
  "mappings": {
    "properties": {
      "name": {
        "type": "text"
      },
      "age": {
        "type": "long"
      },
      "birthday": {
        "type": "date"
      }
    }
  }
}
```

### 获取信息 GET,GET 到索引就是获取索引信息，GET到文档就是获取文档信息
- 请求
```JavaScript
GET test2
```
- 响应
```JavaScript
{
  "test2" : {
    "aliases" : { },
    "mappings" : {
      "properties" : {
        "age" : {
          "type" : "long"
        },
        "birthday" : {
          "type" : "date"
        },
        "name" : {
          "type" : "text"
        }
      }
    },
    "settings" : {
      "index" : {
        "routing" : {
          "allocation" : {
            "include" : {
              "_tier_preference" : "data_content"
            }
          }
        },
        "number_of_shards" : "1",
        "provided_name" : "test2",
        "creation_date" : "1639555809082",
        "number_of_replicas" : "1",
        "uuid" : "EkWEQUlqQQWrcRGL5ugqOw",
        "version" : {
          "created" : "7120199"
        }
      }
    }
  }
}
```
- 如果获取的文档字段没有指定，那么ES就会给我们默认配置字段类型
- 扩展：  GET _cat/xxx 可以查看很多
 - GET _cat/health ： 查看健康状态
 - GET _cat/indices?v : 查看索引的一些信息

### 修改索引
- 方法一：直接对原来的数据进行覆盖的修改，重复命令，改变数据即可
- 方法二：POST  _update 更新
```JavaScript
POST /test3/_update/1
{
  "doc": {
    "name": "小小狼灵"
  }
}
```

### 删除索引
- DELETE 索引名称
```JavaScript
DELETE test2
```
