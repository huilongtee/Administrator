package com.example.Administrator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.Administrator.entity.News;
import org.apache.ibatis.annotations.Insert;

public interface NewsMapper extends BaseMapper<News> {

@Insert("insert into news(title,description,content,authorID,time) values(#{title},#{description},#{content},#{authorID},#{time})")
void insertNews(News news);


}
