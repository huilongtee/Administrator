package com.example.Administrator.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data

@TableName("news") //mybatis-plus
public class News {

    @TableId(type=IdType.AUTO)
    private int id;
    private String title;
    private String description;
    private String content;
    private int authorid;
    private String time;
}
