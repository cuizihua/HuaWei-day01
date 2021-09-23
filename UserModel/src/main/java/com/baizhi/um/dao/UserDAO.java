package com.baizhi.um.dao;

import com.baizhi.um.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDAO {

    /**
     * 添加用户
     * @param user 需要保存的用户对象
     */
    void saveUser(User user);

    /**
     * 根据用户名和密码查询用户信息
     * @param user 查询需要的用户名和密码封装的对象
     * @return 查询到的用户对象
     */
    User queryUserByNameAndPassword(User user);

    /**
     * 根据id查询用户信息
     * @param id 查询需要的id参数
     * @return 查询到的用户对象
     */
    User queryUserById(Integer id);

    /**
     * 根据主键删除用户
     * @param id 删除需要的id参数
     */
    void deleteByUserId(Integer id);
    
    //可以添加一个批量删除的方法
    /**
     * 根据主键删除多个用户
     * @param ids 删除需要的所有id
     */
    void deleteByUserIds(@Param("idssss") Integer[] ids);
    //通过动态sql的方式完成批量删除：拼接一个条件where id in (1,12,22)

    /**
     * 根据给定字段作为条件进行分页查询
     * @param offset 分页需要的参数-偏移量
     * @param limit 分页需要的参数-每页显示数据的控制
     * @param column 查询需要的字段名
     * @param value 对应字段的数据
     * @return 查询到的用户信息集合
     */
    List<User> queryUserByPage(
            @Param(value = "offset") Integer offset,
            @Param(value = "limit") Integer limit,
            @Param(value = "column") String column,
            @Param(value = "value") Object value);

    /**
     * 根据给定字段作为条件查询记录数
     * @param column 查询需要的字段名
     * @param value 对应字段的数据
     * @return
     */
    int queryCount(
            @Param(value = "column") String column,
            @Param(value = "value") Object value);

    /**
     * 修改用户信息
     * @param user 需要修改的用户信息
     */
    void updateUser(User user);
}