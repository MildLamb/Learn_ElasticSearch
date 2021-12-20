package com.mildlamb.controller;

import com.mildlamb.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

    @Autowired
    private ContentService contentService;

    @GetMapping({"/","/index"})
    public String index(){
        return "index";
    }

    @GetMapping("/parse/{keyword}")
    @ResponseBody
    public Boolean parse(@PathVariable("keyword") String word) throws IOException {
        return contentService.parseContent(word);
    }

    @GetMapping("/search/{pageNo}/{pageSize}/{keyword}")
    @ResponseBody
    public List<Map<String,Object>> search(@PathVariable("pageNo") int pno,@PathVariable("pageSize") int psize,@PathVariable("keyword") String word) throws IOException {
        System.out.println(word);
        return contentService.searchPageHigh(word,pno,psize);
    }


}
