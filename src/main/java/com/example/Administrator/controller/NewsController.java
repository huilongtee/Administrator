package com.example.Administrator.controller;

import cn.hutool.core.util.StrUtil;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.Administrator.common.Result;
import com.example.Administrator.entity.News;
import com.example.Administrator.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/news")

public class NewsController {

    @Autowired
    NewsService newsService;

    @PostMapping("/add")
    public Result addNews(@RequestBody News news) {

            newsService.save(news);
//            newsService.insert(news);

        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody News news) {
        newsService.updateById(news);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable int id) {
        newsService.removeById(id);
        return Result.success();
    }

    @DeleteMapping("/delete/batch")
    public Result deleteMultipleNews(@RequestBody List<Integer> ids) {
        newsService.removeBatchByIds(ids);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll() {
        List<News> users = newsService.list(new QueryWrapper<News>().orderByDesc("id"));
        return Result.success(users);
    }

    @PostMapping("/selectById")
    public Result selectAll(@RequestBody int id) {
        News users = newsService.getById(id);
        return Result.success(users);
    }


    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam int pageNum, @RequestParam int pageSize, @RequestParam String title) {
        QueryWrapper queryWrapper = new QueryWrapper<News>().orderByDesc("id");
        queryWrapper.like(StrUtil.isNotBlank(title), "title", title);
        Page<News> page = newsService.page(new Page<>(pageNum, pageSize), queryWrapper);
        return Result.success(page);
    }
}
