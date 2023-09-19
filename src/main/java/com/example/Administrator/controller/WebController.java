package com.example.Administrator.controller;

import cn.hutool.core.util.StrUtil;
import com.example.Administrator.common.AuthAccess;
import com.example.Administrator.common.Result;
import com.example.Administrator.entity.User;
import com.example.Administrator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class WebController {

    @Resource
//  @Autowired
    UserService userService;

//    @RequestMapping
//    public Result hello(){
//        return Result.success("Hello Spring boot");
//    }

    //    @AuthAccess to make the path open, not restricted
    @AuthAccess
    @GetMapping("/")
    public Result hello() {
        return Result.success("hello");
    }
//
//    @GetMapping("/getHello")
//    public Result getHello(String data){
//        return Result.success(data);
//
//    }
//
//    @PostMapping("/post")
//    public Result post(@RequestBody Obj obj){
//        return Result.success(obj);
//    }
//
////    normally put request is for updating data
//    @PutMapping("/put")
//    public Result put(@RequestBody Obj obj){
//        return Result.success(obj);
//    }
//
////    delete single data
////    @DeleteMapping("/delete/{id}")
////    public Result delete(@PathVariable int id){
////        return Result.success(id+" is deleted");
////
////    }
//
//    //delete multiple data
//    @DeleteMapping("/delete")
//    public Result delete(@RequestBody List<Integer> ids){
//        return Result.success(ids);
//
//    }


    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())) {
            return Result.error("Please make sure all fill has been filled");
        }

        user = userService.login(user);
        return Result.success(user);
    }

//    @AuthAccess to make the path open, not restricted
    @AuthAccess
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
//        must import hutool to use StrUtil
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())) {
            return Result.error("Please make sure all fill has been filled");
        }
        if (user.getUsername().length() > 15) {
            return Result.error("The username length must be less than 15 characters");
        }
        if (user.getPassword().length() > 15) {
            return Result.error("The password length must be less than 15 characters");
        }
        user = userService.register(user);
        return Result.success(user);
    }
}

