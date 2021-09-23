package com.baizhi.um.service.impl;

import com.baizhi.um.dao.UserDAO;
import com.baizhi.um.entity.User;
import com.baizhi.um.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    @Transactional
    public void saveUser(User user) {

    }

    @Override
    @Transactional(readOnly = true)
    public User queryUserByNameAndPassword(User user) {
        return null;
    }

    @Override
    public List<User> queryUserByPage(Integer pageIndex, Integer pageSize, String column, Object value) {

        Integer offset=(pageIndex-1)*pageSize;
        Integer limit=pageSize;
//        System.out.println(userDAO+"*****");
        return userDAO.queryUserByPage(offset,limit,column,value);
    }

    @Override
    public int queryUserCount(String column, Object value) {
        return 0;
    }

    @Override
    public User queryUserById(Integer id) {
        return null;
    }

    @Override
    @Transactional
    public void deleteByUserIds(Integer[] ids) {

        //需要配置mybatis的批量操作
        for (Integer id : ids) {
            userDAO.deleteByUserId(id);
        }

    }

    @Override
    @Transactional
    public void updateUser(User user) {

    }
}
