package com.baizhi.wa.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String log = request.getParameter("log");
        Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
//        logger.info("WebApplication EVALUATE [zhangsan] 119e76e86bea42e9a098c2461a6b9314 \"123456\" zz \"113.65,34.76\" [1500,2800,3100] \"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWesssbKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36\"");
        System.out.println(log);
        logger.info(log);

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
