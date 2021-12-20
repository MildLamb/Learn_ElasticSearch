package com.mildlamb.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mildlamb.pojo.Content;
import com.mildlamb.utils.HtmlParseUtil;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.directory.SearchResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

    // 3.获取数据实现搜索功能,添加搜索高亮功能
    public List<Map<String,Object>> searchPageHigh(String keyword,int pageNo,int pageSize) throws IOException {
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

        // 高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");
        // 是否多个高亮
        highlightBuilder.requireFieldMatch(true);
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);

        // 执行搜索
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        // 解析查询结果
        ArrayList<Map<String,Object>> list = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {

            // 解析高亮的字段
            // 先获取高亮的字段
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField title = highlightFields.get("title");  // 高亮的部分

            Map<String, Object> sourceAsMap = hit.getSourceAsMap(); // 原来的结果，包括所有img,price,title等等，需要将高亮的title置换这里的title

            // 将原来的字段换为我们高亮的字段
            if (title != null){
                Text[] highlight_title = title.fragments();
                System.out.println( "highlight_title:" + highlight_title.toString());
                String n_title = "";
                for (Text text : highlight_title) {
                    n_title += text;
                }
                sourceAsMap.put("title",n_title);  // 替换为高亮字段
            }
            list.add(sourceAsMap);
        }

        return list;
    }
}
