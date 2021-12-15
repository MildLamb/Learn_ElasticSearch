# ES核心概念
- elasticsearch是面向文档，关系型数据库和elasticsearch

| RelationalDB      | ElasticSearch |
| :----------- | :----------- |
| 数据库(database)      | 索引(indices)       |
| 表(table)   | types        |
| 行(rows)   | 文档(documents)        |
| 列(columns)   | 字段(fields)        |

- elasticsearch(集群)中可以包含多个索引(数据库),每个索引可以包含多个类型(表)，每个类型下又包含多个文档(行),  
每个文档中又包含多个字段(列)
- 物理设计：
  - elasticsearch在后台把每个索引划分成多个分片，每个分片可以在集群中的不同服务器间迁移
  - 一个人就是一个集群，默认的集群名称为elasticsearch

```json
{
"name": "LAPTOP-AP9E7L32",
"cluster_name": "elasticsearch",
"cluster_uuid": "hA79clC-SemdlCNj3Wmq5g",
"version": {
"number": "7.12.1",
"build_flavor": "default",
"build_type": "zip",
"build_hash": "3186837139b9c6b6d23c3200870651f10d3343b7",
"build_date": "2021-04-20T20:56:39.040728659Z",
"build_snapshot": false,
"lucene_version": "8.8.0",
"minimum_wire_compatibility_version": "6.8.0",
"minimum_index_compatibility_version": "6.0.0-beta1"
},
"tagline": "You Know, for Search"
}
```
- 逻辑设计：一个索引类型中，包含多个文档。当我们索引一篇文章时，可以通过这样的一个顺序找到它： 索引 > 类型 > 文档ID，  
通过这个组合我们就能索引到某个具体的文档。注意ID：不必非得是整数，可以是字符串

### 文档
elasticsearch索引和搜索数据的最小单位是文档，elasticsearch中，文档有几个重要属性：
- 自我包含，一篇文档同时包含字段和对应的值，也就是同时包含key:value 
- 可以是层次性的，一个文档中包含自文档，复杂的逻辑实体就是这么来的，说白了json对象
- 灵活的结构，文档不依赖预先定义的模式，我们知道关系型数据库中，要提前定义字段才能使用，在elasticsearch中，对于字段是  
非常灵活的，有时候，我们可以忽略该字段，或者动态的添加新的字段

### 类型
类型是文档的逻辑容器，就像关系型数据库一样，表格是行的容器。类型中对于字段的定义称为映射，比如name映射为字符串类型。我们说  
文档是无模式的，它们不需要拥有映射中所定义的所有字段，比如说新增一个字段，那么elasticsearch是怎么做的呢?elasticsearch会  
自动将新字段加入映射，但是这个字段不确定是什么类型，elasticsearch会去猜，假如这个值是18，那么elasticsearch会认为它是整形  
，但也可能猜不对，所以最好的方式是提前定义好所需要的映射。

### 索引
就好比数据库  
索引是类型映射的容器，elasticsearch中的索引是一个非常大的文档集合。索引存储了映射类似的字段和其他设置。然后它们被存储到了各个  
分片上了。

## (分片)倒排索引
elasticsearch使用的是一种称为倒排索引的结构，为了创建倒排索引，我们首先会将每个文档拆分为独立的词，然后创建一个包含所有不重复  
的词条的排序列表，然后列出每个词条出现在哪个文档，出现越多的列表，权重越高
![image](https://user-images.githubusercontent.com/92672384/146111080-a60cb98d-7d87-4791-99c1-9c7aec7d2ebf.png)
