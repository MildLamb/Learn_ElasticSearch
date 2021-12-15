# IK分词器插件
分词∶即把一段中文或者别的划分成一个个的关键字，我们在搜索时候会把自己的信息进行分词，会把数据库中或者索引库中的数据进行分词，  
然后进行一个匹配操作，默认的中文分词是将每个字看成一个词，比如“我爱千珏"会被分为"我""爱""千""珏”，这显然是不符合要求的，  
所以我们需要安装中文分词器ik来解决这个问题。  
IK提供了两个分词算法:∶ ik_smart和ik_max_word，其中 ik_smart为最少切分，ik_max_word为最细粒度划分!

# 安装IK分词器
- 下载地址：https://github.com/medcl/elasticsearch-analysis-ik （注意和elasticsearch版本对应）
- 下载完毕后，放入到es的plugins插件目录中，在plugins中创建一个ik的文件夹，解压到这个文件夹中
- 重启ES观察
- elasticsearch-plugin list ，可以通过这个命令来查看加载进来的插件
- 使用kibana进行测试

### 查看不同的分词器效果
- ik_smart为最少切分
![image](https://user-images.githubusercontent.com/92672384/146117543-83d5a801-1e7a-47cc-9feb-a77f95994a89.png)

- ik_max_word为最细粒度划分
![image](https://user-images.githubusercontent.com/92672384/146117588-1fc7623e-3c9b-415c-bceb-70167aa31285.png)

## 如何将自己想要的一些词添加到分词器字典中呢？
- 进入ik分词器的config目录中(在ES的plugins里面)
- 新建一个自己的词典，把你想要的词写进去(我新建的字典文件名为mildlamb.dic)
- 在ik分词器的配置文件IKAnalyzer.cfg.xml中的如下字段中添加刚刚编写的配置字典
```xml
<!--用户可以在这里配置自己的扩展字典 -->
<entry key="ext_dict">mildlamb.dic</entry>
```
- 重启ES，测试



# 出现的问题
- kibana无法启动
```bash
[warning][config][plugins][security] Generating a random key for xpack.security.encryptionKey. To prevent sessions from being invalidated on restart, please set xpack.security.encryptionKey in kibana.yml
[warning][config][plugins][security] Session cookies will be transmitted over insecure connections. This is not recommended.
[warning][security] Generating a random key for xpack.security.encryptionKey. To prevent sessions from being invalidated on restart, please set xpack.security.encryptionKey in kibana.yml
```
- 好像是要什么安全认证，解决方案
在kibana config/kibana.yml中添加如下配置
```bash
# 使用32个字符或更长的任何文本字符串作为加密密钥
xpack.security.encryptionKey: "something_at_least_32_characters"
# 设置多次重启和kibana的多个实例之间保留相同的密钥
xpack.reporting.encryptionKey: "something_at_least_32_characters"
```
