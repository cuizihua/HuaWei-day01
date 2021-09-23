package com.baizhi.um.service;

import com.baizhi.um.entity.User;

import java.util.List;

public interface UserService {
 /**
 * 保存用户户
 * @param user
 */
 void saveUser(User user);
 /**
 * 根据密码和用户名查询用户
 * @param user
 * @return
 */
 User queryUserByNameAndPassword(User user);
 /***
 *
 * @param pageIndex
 * @param pageSize
 * @param column 模糊查询列
 * @param value 模糊值
 * @return
 */
 List<User> queryUserByPage(Integer pageIndex, Integer pageSize,
                            String column, Object value);
 /**
 * 查询用户总条数
 * @param column
 * @param value
 * @return
 */
 int queryUserCount(String column, Object value);
 /**
 * 根据ID查询用户信息
 * @param id
 * @return
 */
 User queryUserById(Integer id);
 /**
 * 根据IDS删除用户
 * @param ids
 
 * a.可以调用dao接口中的deleteByUserIds完成批量删除
 * b.在service实现类中，通过循环调用dao接口中的deleteByUserId(id)
 */
 void deleteByUserIds(Integer[] ids);
 /**
 * 更新用户信息
 * @param user
 */
 void updateUser(User user);
}