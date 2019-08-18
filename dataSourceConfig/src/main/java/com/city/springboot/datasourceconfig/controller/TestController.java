package com.city.springboot.datasourceconfig.controller;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TestController {

//    @Resource(name = "basisDataSource")
    private DruidDataSource druidDataSource;

    @RequestMapping
    public String test(){
        return "";
    }
}
