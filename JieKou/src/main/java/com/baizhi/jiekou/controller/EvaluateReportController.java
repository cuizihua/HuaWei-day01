package com.baizhi.jiekou.controller;

import com.baizhi.jiekou.service.ReadEvaluateReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EvaluateReportController {


    @Autowired
    private ReadEvaluateReport readEvaluateReport;

    /**
     * 获取到登录的评估报告
     * @param appname 应用名
     * @param username 用户名
     * @param uuid 登录标记
     * @return 评估报告对应的json
     */
    @RequestMapping("/readEvaluateReport/{appName}/{username}/{uuid}")
    public String readEvaluateReport(@PathVariable("appName") String appname, @PathVariable("username") String username,@PathVariable("uuid") String uuid){


        String report = readEvaluateReport.readEvaluateReport(appname + ":" + username, uuid);
        return report;
    }
}
