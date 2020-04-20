package com.kry.pms.service;

import com.kry.pms.model.ScheduleJobModel;
import com.kry.pms.util.SpringContextUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Method;

@Service
public class ReflexQuartzService {

    public void reflex(ScheduleJobModel sjm) {
        Class<?> cla = null;
        try {
//            cla = Class.forName(sjm.getClassPath());
//            Object o = cla.newInstance();
//            Method method = cla.getMethod(sjm.getMethodName(),String.class);
//            method.invoke(o, new Object[] {sjm.getHotelCode()});
            cla = Class.forName(sjm.getClassPath());
            Object o = SpringContextUtil.getBean(sjm.getClassName());
            Method method = cla.getDeclaredMethod(sjm.getMethodName(),String.class);
            method.invoke(o, new Object[] {sjm.getHotelCode()});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
