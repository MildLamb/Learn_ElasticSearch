# 爬虫
- 爬取数据：(获取请求返回的页面信息，筛选出我们想要的数据)，jsoup
- 爬取数据的java类
```java
public class HtmlParseUtil {
    public static void main(String[] args) throws IOException {
        new HtmlParseUtil().parseJD("java").forEach(System.out::println);
    }

    public List<Content> parseJD(String keywords) throws IOException{
        // 获取请求 https://search.jd.com/Search?keyword=JAVA
        // 前提 需要联网
        String url = "https://search.jd.com/Search?keyword=" + keywords;
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
