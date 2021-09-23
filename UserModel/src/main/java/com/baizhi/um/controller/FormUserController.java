package com.baizhi.um.controller;

import com.baizhi.um.entity.User;
import com.baizhi.um.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController  //@Controller+@ResponseBody
@RequestMapping(value = "/formUserManager")
public class FormUserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/registerUser")//等价于下面的代码
//    @RequestMapping(value = "registerUser",method = RequestMethod.POST)
    public User registerUser(User user, MultipartFile multipartFile) throws IOException {
        File file = new File("d:/"+multipartFile.getOriginalFilename());
        multipartFile.transferTo(file);

        //把文件路径设置到对象里面
        user.setPhoto(file.getPath());

        System.out.println("*********************");

        //调用service，把user传进去，一直到把user保存到数据库
        return user;
    }

    @PostMapping(value = "/userLogin")
    public User userLogin(User user){
        return null;
    }

    @PutMapping(value = "/updateUser")
    public void updateUser(User user, MultipartFile multipartFile) throws IOException {}

    @DeleteMapping(value = "/deleteUserByIds")
    public void delteUserByIds(Integer[] ids){}

    @GetMapping(value = "/queryUserByPage")
    public List<User> queryUserByPage(@RequestParam(value = "pageIndex",defaultValue = "1")
                                              Integer pageIndex,
                                      @RequestParam(value = "rows",defaultValue =
                                              "10") Integer pageSize,
                                      @RequestParam(value = "column",required = false)
                                              String column,
                                      @RequestParam(value = "value",required = false)
                                              String value){
        return null;
    }

    @GetMapping(value = "/queryUserCount")
    public Integer queryUserCount(String column,String value){
        return null;
    }

    @GetMapping(value = "/queryUserById")
    public User queryUserById(Integer id){
        return null;
    }

}