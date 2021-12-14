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
- 安装可视化界面 es-head 插件
  - 下载地址：https://github.com/mobz/elasticsearch-head
  - 安装所需环境 npm/cnpm install
  - 解决跨域问题，修改elasticsearch.yml文件，添加
    - http.cors.enabled: true
    - http.cors.allow-origin: "*"
  - 启动测试 npm run start,默认端口9100；http://localhost:9100


# 了解ELK
ELK，是ElasticSearch，Logstash，Kibana三大开源框架首字母大写简称。其中Elastic是一个基于Lucene，分布式，通过Restful方式  
进行交互的近实时搜索平台框架。Logstash是ELK的中央数据流引擎，用于从不同目标(文件/数据存储/MQ)收集不同格式数据，经过过滤后支持  
输出到不同目的地(文件/MQ/redis/elasticsearch/kafka等)。Kibana可以将elasticsearch的数据通过友好的页面展示出来，提供实时分  
析的功能。  

实际上ELK不仅仅适用于日志分析，它还可以支持其他任何数据分析和收集的场景，日志分析和收集只是更具代表性。并非唯一性。

![image](https://user-images.githubusercontent.com/92672384/145927541-96ec84b9-9985-42f7-9061-75d3779a9d88.png)
