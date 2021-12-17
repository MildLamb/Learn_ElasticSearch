# 配置基本的项目
- 创建springboot项目，如果是用的最新版，观察springboot依赖包使用的ES版本
![image](https://user-images.githubusercontent.com/92672384/146468571-c16af142-0f78-46c7-ae5b-8e42ff43269e.png)
- 一定要保证我们导入的依赖和我们的es版本一致
```xml
<properties>
  <java.version>1.8</java.version>
  <!-- 修改依赖的ES版本与本地的一致 -->
  <elasticsearch.version>7.12.1</elasticsearch.version>
</properties>
```
