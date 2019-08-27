package com.tenquare.search.controller;

import com.tenquare.search.pojo.Article;
import com.tenquare.search.service.ArticleService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Article article){
        articleService.save(article);
        return new Result(true, StatusCode.OK,"添加成功");
    }
    @RequestMapping(value = "/search/{keywords}/{page}/{size}",method = RequestMethod.GET)
    public Result findByTitleLikeOrContentLike(@PathVariable String keywords,@PathVariable int page,@PathVariable int size){
        Page<Article> pagelist = articleService.findByTitleLikeOrContentLike(keywords, page, size);
        PageResult articlePageResult = new PageResult(pagelist.getTotalElements(),pagelist.getContent());
        return new Result(true,StatusCode.OK,"查询成功",articlePageResult);
    }
}
