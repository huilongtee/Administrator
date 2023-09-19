package com.example.Administrator.mapper;

import com.example.Administrator.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Insert("insert into user(name, password, username, email, phone, address, avatar) values(#{name}, #{password}, #{username}, #{email}, #{phone}, #{address}, #{avatar})")
    void insertUser(User user);

    @Update("update user set name=#{name}, password=#{password}, username=#{username}, email=#{email}, phone=#{phone}, address=#{address}, avatar=#{avatar} where id=#{id}")
    void updateUser(User user);

    @Delete("delete from user where id=#{id}")
    void deleteUser(int id);


    @Delete("delete from user where id = #{id}")
    void batchDeleteUser(int id);


    //    @Select("select * from user")
    @Select("select * from user order by id desc")
    List<User> selectAllUser();

    @Select("select * from user where id = #{id}")
    User selectUserById(int id);

    @Select("select * from user where username = #{username} AND name = #{name}")
    List<User> selectUserByMore(@Param("username") String username,@Param("name") String name);
    @Select("select * from user where username like concat('%' , #{username},'%') AND name like concat('%', #{name},'%')")
    List<User> selectUserByMo(@Param("username") String username,@Param("name") String name);

    @Select("select * from user where username like concat('%' , #{username},'%') AND name like concat('%', #{name},'%') limit #{skipNum},#{pageSize}")
    List<User> selectUserByPage(@Param("skipNum") int skipNum,@Param("pageSize") int pageSize,@Param("username") String username,@Param("name") String name);

    @Select("select count(id) from user where username like concat('%' , #{username},'%') AND name like concat('%', #{name},'%') order by id desc ")
    int selectCountByPage(@Param("username") String username,@Param("name") String name);

    @Select("select * from user where username = #{username}")
    User selectUserByName(String username);
}
