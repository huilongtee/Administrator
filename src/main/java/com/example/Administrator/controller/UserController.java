package com.example.Administrator.controller;

import com.example.Administrator.common.Page;
import com.example.Administrator.common.Result;
import com.example.Administrator.entity.User;
import com.example.Administrator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/add")
    public Result add(@RequestBody User user) {
        try {
            userService.insertUser(user);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                return Result.error("Insert into database failed");
            } else {
                return Result.error("System error");
            }

        }
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody User user) {
        try {
            userService.updateUser(user);
        } catch (Exception e) {

            return Result.error("System error");


        }
        return Result.success();
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestBody int id) {
        try {
            userService.deleteUser(id);
        } catch (Exception e) {

            return Result.error("System error");


        }
        return Result.success();
    }

    @DeleteMapping("/delete/batch")
    public Result deleteMultipleUser(@RequestBody List<Integer> ids) {
        try {
            userService.batchDeleteUser(ids);
        } catch (Exception e) {

            return Result.error("System error");


        }
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll() {
        List<User> users= userService.selectAllUser();
        return Result.success(users);
    }

    @PostMapping("/selectById")
    public Result selectAll(@RequestBody int id) {
        User users= userService.selectById(id);
        return Result.success(users);
    }

    @GetMapping("/selectByMore")
    public Result selectByMore(@RequestParam String username, @RequestParam String name) {
        List<User> users= userService.selectByMore(username,name);
        return Result.success(users);
    }

    @GetMapping("/selectByMo")
    public Result selectByMo(@RequestParam String username, @RequestParam String name) {
        List<User> users= userService.selectByMo(username,name);
        return Result.success(users);
    }

    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam int skipNum,@RequestParam int pageSize, @RequestParam String username, @RequestParam String name) {
Page<User> page= userService.selectByPage(skipNum,pageSize,username,name);
        return Result.success(page);
    }
}
