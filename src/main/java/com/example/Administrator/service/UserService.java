package com.example.Administrator.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.Administrator.common.Page;
import com.example.Administrator.entity.User;
import com.example.Administrator.exception.ServiceException;
import com.example.Administrator.mapper.UserMapper;
import com.example.Administrator.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Resource
    UserMapper userMapper;

//    public void insertUser(User user) {
//        userMapper.insertUser(user);
//    }
//
//    public void updateUser(User user) {
//        userMapper.updateUser(user);
//    }
//
//    public void deleteUser(int id) {
//        userMapper.deleteUser(id);
//    }
//
//    public void batchDeleteUser(List<Integer> ids) {
//        for (int id : ids) {
//            userMapper.batchDeleteUser(id);
//        }
//    }
//
//    public List<User> selectAllUser() {
//        return userMapper.selectAllUser();
//    }
//
//    public User selectById(int id) {
//        return userMapper.selectUserById(id);
//    }
//
//    public List<User> selectByMore(String username, String name) {
//        return userMapper.selectUserByMore(username, name);
//    }
//
//    public List<User> selectByMo(String username, String name) {
//        return userMapper.selectUserByMo(username, name);
//
//    }
//
//    public Page<User> selectByPage(int pageNum, int pageSize, String username, String name) {
//        int skipNum = (pageNum - 1) * pageSize;
//
//        Page<User> page = new Page<>();
//        List<User> users = userMapper.selectUserByPage(skipNum, pageSize, username, name);
//        int total = userMapper.selectCountByPage(username, name);
//
//        page.setTotal(total);
//        page.setList(users);
//        return page;
//    }

//
//    @Override
//    public boolean removeById(User entity) {
//        User currentUser = TokenUtils.getCurrentUser();
//        if (entity.getId() == currentUser.getId()) {
//            throw new ServiceException("Cannot delete current login user");
//        }
//        return super.removeById(entity);
//    }
//
//    @Override
//    public boolean removeBatchByIds(Collection<?> list) {
//        User currentUser = TokenUtils.getCurrentUser();
//        if (currentUser != null && list.contains(currentUser.getId())) {
//            throw new ServiceException("Cannot delete current login user");
//        }
//        return super.removeBatchByIds(list);
//    }

    public User selectUserByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return getOne(queryWrapper);
    }

    public User login(User user) {

        User userDb = selectUserByUsername(user.getUsername());

        if (userDb == null) {
            throw new ServiceException("Account does not exist");
        }

//        cannot put userDb.getPassword() in front of user.getPassword()
//        because it will occur pointer null error
        if (!user.getPassword().equals(userDb.getPassword())) {
            throw new ServiceException("Username or Password doesn't correct");
        }

        String token = TokenUtils.generateToken(String.valueOf(userDb.getId()), userDb.getPassword());
        userDb.setToken(token);

        return userDb;
    }

    public User register(User user) {
        User userDb = selectUserByUsername(user.getUsername());

        if (userDb != null) {
            throw new ServiceException("Username already exists");
        }
        user.setName(user.getUsername());
        userMapper.insert(user);
        userDb = selectUserByUsername(user.getUsername());
        String token = TokenUtils.generateToken(String.valueOf(userDb.getId()), userDb.getPassword());
        user.setToken(token);
        return user;
    }

    public User resetPassword(User user) {
        User userDb = selectUserByUsername(user.getUsername());


        if (userDb == null) {
            throw new ServiceException("Account does not exist");
        }

        if (!user.getPhone().equals(userDb.getPhone())) {
            throw new ServiceException("Verification error");
        }

        userDb.setPassword("123");
        updateById(userDb);
        return userDb;

    }
}
