package com.kry.pms.service.quratz;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.base.ParamSpecification;
import com.kry.pms.dao.quartz.QuartzSetDao;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.quartz.QuartzSet;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.quratz.impl.QuartzSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuartzSetServiceImpl implements QuartzSetService {
    @Autowired
    QuartzSetDao quartzSetDao;
    @Override
    public QuartzSet add(QuartzSet entity) {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public QuartzSet modify(QuartzSet quartzSet) {
        QuartzSet qs = quartzSetDao.save(quartzSet);
        return qs;
    }

    @Override
    public QuartzSet findById(String id) {
        return null;
    }

    @Override
    public List<QuartzSet> getByHotelCode(User user) {
        String hotelCode = null;
        if(user != null){
            hotelCode = user.getHotelCode();
        }
        List<QuartzSet> list;
        list = quartzSetDao.findByHotelCode(hotelCode);
        if(list.isEmpty()){
            QuartzSet qs = new QuartzSet();
            qs.setCreateUser(user.getId());
            qs.setCron("0 0 6 * * ?");
            qs.setCronTime("06:00:00");
            qs.setHotelCode(hotelCode);
            qs.setStatus("1");
            quartzSetDao.save(qs);
            list = quartzSetDao.findByHotelCode(hotelCode);
        }
        return list;
    }

    @Override
    public List<QuartzSet> getAllByHotelCode(String code) {
        List<QuartzSet> list = quartzSetDao.findByHotelCode(code);
        return list;
    }

    @Override
    public List<QuartzSet> getAll() {
        ParamSpecification<QuartzSet> psf = new ParamSpecification<QuartzSet>();
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> emap = new HashMap<>();
        emap.put("status", "0");
        map.put("equals", emap);
        Specification<QuartzSet> specification = psf.createSpecification(map);
        List<QuartzSet> list = quartzSetDao.findAll(specification);
        return list;
    }

    @Override
    public List<QuartzSet> findByHotelCodeAndStatus(String hotelCode, String status) {
        List<QuartzSet> list = quartzSetDao.findByHotelCodeAndStatus(hotelCode, status);
        return list;
    }

    @Override
    public PageResponse<QuartzSet> listPage(PageRequest<QuartzSet> prq) {
        return null;
    }

    @Override
    public String getTest() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Class<?> cla = Class.forName("com.kry.pms.service.quratz.impl.QuartzTestServiceImpl");
        Object o = cla.newInstance();
        Method method = cla.getMethod("getTest",String.class);
        method.invoke(o, new Object[] {"sdfsd"});
        return null;
    }

    @Override
    public void addTest(String hotelCode) {
        QuartzSet qs = new QuartzSet();
        qs.setCron("0/30 * * * * ? ");
        qs.setCronTime("30s/次");
        qs.setHotelCode(hotelCode);
        qs.setStatus("1");
        quartzSetDao.save(qs);
    }

}
