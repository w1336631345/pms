package com.kry.pms.service.quratz.impl;

import com.kry.pms.model.persistence.quartz.QuartzSet;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.BaseService;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface QuartzSetService extends BaseService<QuartzSet> {
    List<QuartzSet> getByHotelCode(User user);

    List<QuartzSet> getAll();

    List<QuartzSet> findByHotelCodeAndStatus(String hotelCode, String status);

    String getTest() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException;

    void addTest(String hotelCode);
}
