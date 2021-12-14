# Learn_ElasticSearch
学习搜索引擎

# ElasticSearch概括
ElasticSearch，简称es，es是一个开源的高扩展的分布式全文检索引擎，它可以近乎实时的存储，检索数据；本身扩展性很好，  
可以扩展到上百台服务器，处理PB级别(大数据时代)的数据。es也使用java开发并使用Lucene作为核心来实现所有索引和搜索的  
功能，但是它的目的是通过简单的RESTful API来隐藏Lucene的复杂性，从而让全文搜索变得简单

# 安装ElasticSearch
- 下载
  - 镜像下载地址
    - https://mirrors.huaweicloud.com/elasticsearch/?C=N&O=D
    - https://mirrors.huaweicloud.com/logstash/?C=N&O=D
    - https://mirrors.huaweicloud.com/kibana/?C=N&O=D

- 解压即可
- 目录
  - bin  启动文件
  - config  配置文件
    - log4j2  日志配置文件
    - jvm.options  java虚拟机相关的配置
    - elasticsearch.yml  elasticsearch的配置文件，默认9200端口，跨域
  - lib  相关jar包
  - modules  功能模块
  - plugins  插件
