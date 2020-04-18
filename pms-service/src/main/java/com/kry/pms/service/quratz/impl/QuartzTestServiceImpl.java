package com.kry.pms.service.quratz.impl;

import org.springframework.stereotype.Service;

import java.sql.SQLOutput;

@Service
public class QuartzTestServiceImpl implements QuartzTestService {

    @Override
    public String getTest(String test) {
        System.out.println("传入参数：" + test);
        return test;
    }

    @Override
    public String getTest() {
        System.out.println("传入参数：" + 1);
        return "123";
    }
}
