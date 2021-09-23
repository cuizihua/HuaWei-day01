package com.baizhi.wa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

    @RequestMapping("/login")
    public String login(String username,String password){

        //通过restTemplate发送远程调用，调用到后台的功能

        System.out.println("进来啦，好开心");

        return "forward:/list.jsp";
    }
}
