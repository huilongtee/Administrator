package com.example.Administrator.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.Administrator.entity.News;
import com.example.Administrator.mapper.NewsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class NewsService extends ServiceImpl<NewsMapper, News> {

    @Resource
    NewsMapper newsMapper;

    public void insert(News news) {
        newsMapper.insertNews(news);
    }
}
