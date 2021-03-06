package com.kry.pms.service.quratz.impl;

import com.kry.pms.service.quratz.QuartzTestService;
import org.springframework.stereotype.Service;

@Service
public class QuartzTestServiceImpl implements QuartzTestService {

    @Override
    public String getTest(String test) {
        System.out.println("传入参数1：" + test);
        return test;
    }

    @Override
    public String getTest(String test, String sdf) {
        System.out.println("传入参数1：" + test);
        System.out.println("传入参数2：" + sdf);
        return test;
    }

    @Override
    public String getTest() {
        System.out.println("传入参数：" + 1);
        return "123";
    }
}
