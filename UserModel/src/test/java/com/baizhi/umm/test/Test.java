package com.baizhi.umm.test;

import com.baizhi.um.UserModelApplication;
import com.baizhi.um.dao.UserDAO;
import com.baizhi.um.entity.User;
import com.baizhi.um.service.UserService;
import com.baizhi.um.service.impl.UserServiceImpl;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

//@RunWith(SpringJUnit4ClassRunner.class)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserModelApplication.class)
public class Test {

    @Autowired
    private UserDAO userDAO;

    @org.junit.Test
    public void t(){

//        userDAO.deleteByUserIds(new Integer[]{11,10,9,8,7});
        List<User> list = userDAO.queryUserByPage(0, 1000, "name", "a");
        System.out.println(list);
    }

    @Autowired
    private UserService userService;

    @org.junit.Test
    public void testService(){

//        UserService userService = new UserServiceImpl();
//        List<User> list = userService.queryUserByPage(1, 1000, null, null);
//        System.out.println(list.size());
        userService.deleteByUserIds(new Integer[]{12,13,14});
    }
}
