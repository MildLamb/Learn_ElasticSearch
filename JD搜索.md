# 爬虫
- 爬取数据：(获取请求返回的页面信息，筛选出我们想要的数据)，jsoup
- 爬取数据的java类
```java
public class HtmlParseUtil {
    public static void main(String[] args) throws IOException {
        new HtmlParseUtil().parseJD("明天").forEach(System.out::println);
    }

    public List<Content> parseJD(String keywords) throws IOException{
        // 获取请求 https://search.jd.com/Search?keyword=JAVA
        // 前提 需要联网
        // jsoup 高版本可以url可以直接传中文了，低版本的话，可以在请求的url中添加  &enc=utf-8
        String url = "https://search.jd.com/Search?keyword=" + keywords + "&enc=utf-8";
        // 解析网页 (Jsoup返回Document就是Document对象)
        Document document = Jsoup.parse(new URL(url), 30000);
        // 所有你在js中可以使用的方法，这里都能用
        Element element = document.getElementById("J_goodsList");
        // 获取所有的li元素
        Elements li_element = element.getElementsByTag("li");

        ArrayList<Content> goodsList = new ArrayList<>();

        // 获取元素中的内容,下面遍历出来的li就是每一个li标签了
        for (Element li : li_element) {
            String img = li.getElementsByTag("img").eq(0).attr("data-lazy-img");
            String price = li.getElementsByClass("p-price").eq(0).text();
            String title = li.getElementsByClass("p-name").eq(0).text();

            Content content = new Content();
            content.setImg(img);
            content.setPrice(price);
            content.setTitle(title);

            goodsList.add(content);
        }
//        System.out.println(element.html());
        return goodsList;
    }
}
```
- 使用一个实体类来封装获取到的数据
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Content {
    private String title;
    private String img;
    private String price;
    //可以自己添加属性
}
```

# 将搜索的数据存入ES中，并提供搜索的方法
```java
@Service
public class ContentService {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    // 1.解析数据放入es索引中
    public Boolean parseContent(String keywords) throws IOException {
        List<Content> contents = new HtmlParseUtil().parseJD(keywords);
        // 把获取的数据批量放入es中，用bulk
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("1m");
        for (int i = 0; i < contents.size(); i++) {
            System.out.println(new ObjectMapper().writeValueAsString(contents.get(i)));
            bulkRequest.add(new IndexRequest("jd_goods").id(""+(i+1))
                .source(new ObjectMapper().writeValueAsString(contents.get(i)), XContentType.JSON));
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        return !bulk.hasFailures();
    }

    // 2.获取数据实现搜索功能
    public List<Map<String,Object>> searchPage(String keyword,int pageNo,int pageSize) throws IOException {
        if (pageNo <= 1){
            pageNo = 1;
        }

        // 条件搜索
        SearchRequest searchRequest = new SearchRequest("jd_goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // 分页
        sourceBuilder.from(pageNo);
        sourceBuilder.size(pageSize);

        // 精准匹配
//        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title", keyword);
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", keyword);
        sourceBuilder.query(matchQueryBuilder);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        // 执行搜索
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        // 解析查询结果
        ArrayList<Map<String,Object>> list = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            list.add(hit.getSourceAsMap());
        }

        return list;
    }
}
```

# 控制层提供调用接口
```java
    @GetMapping("/parse/{keyword}")
    @ResponseBody
    public Boolean parse(@PathVariable("keyword") String word) throws IOException {
        return contentService.parseContent(word);
    }

    @GetMapping("/search/{pageNo}/{pageSize}/{keyword}")
    @ResponseBody
    public List<Map<String,Object>> search(@PathVariable("pageNo") int pno,@PathVariable("pageSize") int psize,@PathVariable("keyword") String word) throws IOException {
        return contentService.searchPage(word,pno,psize);
    }
```
