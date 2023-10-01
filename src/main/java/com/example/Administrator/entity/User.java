package com.example.Administrator.entity;

//import lombok.AllArgsConstructor;
//import lombok.Builder;
import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
//import lombok.NoArgsConstructor;

//auto generate setter and getter
@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
@TableName("user") //mybatis-plus
public class User {
    @TableId(type=IdType.AUTO)
    @Alias("ID") // for naming the table column in excel file, you can put chinese words as well
    private int id;
    @Alias("Name")
    private String name;
    @Alias("Password")
    private String password;
    @Alias("Username")
    private String username;
    @Alias("Email")
    private String email;
    @Alias("Phone")
    private String phone;
    @Alias("Address")
    private String address;
    @Alias("Avatar")
    private String avatar;
    @Alias("Role")
    private String role;
    //    due to these variable is corresponding to database, so we use @TableField annotation to tell it that token do not exist in database
    @TableField(exist = false)
    private String token;



}
