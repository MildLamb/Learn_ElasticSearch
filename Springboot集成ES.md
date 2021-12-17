# 配置基本的项目
## 环境搭建
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

## 使用
- 创建一个RestHighLevelClient的Bean
```java
@Configuration
public class ESConfig {
    @Bean
    public RestHighLevelClient restHighLevelClient(){
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost",9200,"http")
//                        ,new HttpHost("ipAddress",9200,"http")
                )
        );
        return client;
    }
}
```

# 索引操作的API
- 创建索引
- 判断是否存在索引
- 获取索引
- 删除索引
```java
@SpringBootTest
class EsApiApplicationTests {

	@Autowired
	@Qualifier("restHighLevelClient")
	private RestHighLevelClient client;

	/**
	 * ES  7.12.1  API
	 */

	// 测试索引的创建
	@Test
	void testCreateIndex() throws IOException {
		// 1.创建索引请求 相当于 PUT mildlamb_index
		CreateIndexRequest request = new CreateIndexRequest("mildlamb_index");
		// 2.客户端执行请求，获取响应
		CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
		System.out.println(createIndexResponse);
	}

	// 测试索引是否存在
	@Test
	void testExistsIndex() throws IOException {
		GetIndexRequest request = new GetIndexRequest("mildlamb");
		//判断索引是否存在
		boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
		System.out.println(exists);
	}

	// 测试索引的获取
	@Test
	void testGetIndex() throws IOException {
		GetIndexRequest request = new GetIndexRequest("mildlamb");
		//判断索引是否存在
		GetIndexResponse getIndexResponse = client.indices().get(request, RequestOptions.DEFAULT);
		System.out.println(getIndexResponse.getSettings());
	}

	// 测试索引的删除
	@Test
	void testDelIndex() throws IOException {
		DeleteIndexRequest request = new DeleteIndexRequest("mildlamb_index");
		AcknowledgedResponse delete = client.indices().delete(request, RequestOptions.DEFAULT);
		System.out.println(delete.isAcknowledged());
	}
}
```

# 文档操作的API
- 创建一个实体类
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class User {
    private String name;
    private int age;
}
```
- 
