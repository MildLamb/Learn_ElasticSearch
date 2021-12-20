package com.mildlamb.utils;

import com.mildlamb.pojo.Content;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
