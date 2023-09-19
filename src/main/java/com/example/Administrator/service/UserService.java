package com.example.Administrator.service;

import com.example.Administrator.common.Page;
import com.example.Administrator.entity.User;
import com.example.Administrator.exception.ServiceException;
import com.example.Administrator.mapper.UserMapper;
import com.example.Administrator.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public void insertUser(User user) {
        userMapper.insertUser(user);
    }

    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

    public void deleteUser(int id) {
        userMapper.deleteUser(id);
    }

    public void batchDeleteUser(List<Integer> ids) {
        for (int id : ids) {
            userMapper.batchDeleteUser(id);
        }
    }

    public List<User> selectAllUser() {
        return userMapper.selectAllUser();
    }

    public User selectById(int id) {
        return userMapper.selectUserById(id);
    }

    public List<User> selectByMore(String username, String name) {
        return userMapper.selectUserByMore(username, name);
    }

    public List<User> selectByMo(String username, String name) {
        return userMapper.selectUserByMo(username, name);

    }

    public Page<User> selectByPage(int pageNum, int pageSize, String username, String name) {
        int skipNum = (pageNum - 1) * pageSize;

        Page<User> page = new Page<>();
        List<User> users = userMapper.selectUserByPage(skipNum, pageSize, username, name);
        int total = userMapper.selectCountByPage(username, name);

        page.setTotal(total);
        page.setList(users);
        return page;
    }

    public User login(User user) {
        User userDb = userMapper.selectUserByName(user.getUsername());
        if (userDb == null) {
            throw new ServiceException("Account does not exist");
        }

//        cannot put userDb.getPassword() in front of user.getPassword()
//        because it will occur pointer null error
        if (!user.getPassword().equals(userDb.getPassword())) {
            throw new ServiceException("Username or Password doesn't correct");
        }

        String token= TokenUtils.generateToken(String.valueOf(userDb.getId()), userDb.getPassword());
        userDb.setToken(token);

        return userDb;
    }

    public User register(User user) {
        User userDb = userMapper.selectUserByName(user.getUsername());
        if (userDb != null) {
            throw new ServiceException("Username already exists");
        }
        user.setName(user.getUsername());
        userMapper.insertUser(user);
        return userDb;
    }
}
