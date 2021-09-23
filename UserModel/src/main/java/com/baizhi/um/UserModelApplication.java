package com.baizhi.um;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.baizhi.um.dao")
public class UserModelApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserModelApplication.class,args);
    }
}
