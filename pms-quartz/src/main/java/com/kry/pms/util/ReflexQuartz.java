package com.kry.pms.util;

import java.lang.reflect.Method;

public class ReflexQuartz {

    public static void reflex(String id) {
        Class<?> cla = null;
        try {
            cla = Class.forName("com.kry.pms.service.quratz.impl.QuartzTestServiceImpl");
            Object o = cla.newInstance();
            Method method = cla.getMethod("getTest",String.class);
            method.invoke(o, new Object[] {"sdfsd"});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
