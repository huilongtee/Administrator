package com.example.Administrator.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.Administrator.common.Result;
import com.example.Administrator.entity.User;
import com.example.Administrator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/add")
    public Result add(@RequestBody User user) {
        try {
            userService.save(user);
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
            if (StrUtil.isBlank(user.getName()) || StrUtil.isBlank(user.getEmail()) || StrUtil.isBlank(user.getPhone()) || StrUtil.isBlank(user.getAddress())) {
                return Result.error("Please make sure all fill has been filled");
            }
            user.setRole(user.getRole().toLowerCase());
            userService.updateById(user);
        } catch (Exception e) {

            return Result.error("System error");


        }
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable int id) {
        try {
//            User currentUser = TokenUtils.getCurrentUser();
//            if (id == currentUser.getId()) {
//                throw new ServiceException("Cannot delete current login user");
//            }

            userService.removeById(id);
        } catch (Exception e) {

            return Result.error("System error");


        }
        return Result.success();
    }

    @DeleteMapping("/delete/batch")
    public Result deleteMultipleUser(@RequestBody List<Integer> ids) {


//            User currentUser = TokenUtils.getCurrentUser();
//            if (currentUser != null  && ids.contains(currentUser.getId())) {
//                throw new ServiceException("Cannot delete current login user");
//            }

        userService.removeBatchByIds(ids);

        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll() {
        List<User> users = userService.list(new QueryWrapper<User>().orderByDesc("id"));
        return Result.success(users);
    }

    @PostMapping("/selectById")
    public Result selectAll(@RequestBody int id) {
        User users = userService.getById(id);
        return Result.success(users);
    }
//
//    @GetMapping("/selectByMore")
//    public Result selectByMore(@RequestParam String username, @RequestParam String name) {
//        List<User> users = userService.selectByMore(username, name);
//        return Result.success(users);
//    }
//
//    @GetMapping("/selectByMo")
//    public Result selectByMo(@RequestParam String username, @RequestParam String name) {
//        List<User> users = userService.selectByMo(username, name);
//        return Result.success(users);
//    }

    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam int pageNum, @RequestParam int pageSize, @RequestParam String username, @RequestParam String name) {
        QueryWrapper queryWrapper = new QueryWrapper<User>().orderByDesc("id");

//       select * from user where username like concat('%' , #{username},'%') AND name like concat('%', #{name},'%')
        queryWrapper.like(StrUtil.isNotBlank(username), "username", username);
        queryWrapper.like(StrUtil.isNotBlank(name), "name", name);
        Page<User> page = userService.page(new Page<>(pageNum, pageSize), queryWrapper);
        return Result.success(page);
    }

    //    export all user in the table
    // Get Mapping cannot accept list
    @GetMapping("/export")
    public void exportData(@RequestParam(required = false) String username, @RequestParam(required = false) String name, @RequestParam(required = false) String ids, HttpServletResponse response) throws IOException {
        ExcelWriter writer = ExcelUtil.getWriter(true);
        List<User> list;
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        if (StrUtil.isNotBlank(ids)) {  //['1', '2', '3'] => [1,2,3]
            List<Integer> idArr = Arrays.stream(ids.split(",")).map(Integer::valueOf).collect(Collectors.toList());
            queryWrapper.in("id", idArr); //check whether the id is in the list
        } else {
            //export all
            queryWrapper.like(StrUtil.isNotBlank(username), "username", username);
            queryWrapper.like(StrUtil.isNotBlank(name), "name", name);

        }
        list = userService.list(queryWrapper);
        writer.write(list, true);

        //set the format of file that you would like to export
        //must put it in front of response
        //==================== start format setting ====================
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("users table list", "UTF-8") + ".xlsx");
        //==================== end format setting ====================

        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream, true);//refresh and close
        outputStream.flush();
        outputStream.close();

    }

    /**
     * @param file
     * @return result of import
     * @throws IOException
     * batch import
     * import file, input excel format file
     */
    @PostMapping("/import")
    public Result imporData(MultipartFile file) throws IOException {
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        List<User> userList = reader.readAll(User.class);
        try {
            userService.saveBatch(userList);

        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                return Result.error("Insert into database failed");
            } else {
                return Result.error("System error");
            }

        }
        return Result.success();
    }

}
