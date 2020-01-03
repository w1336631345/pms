package com.kry.pms.dao.quartz;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.quartz.QuartzSet;

import java.util.List;

public interface QuartzSetDao extends BaseDao<QuartzSet> {

    List<QuartzSet> findByHotelCode(String code);

    List<QuartzSet> findByHotelCodeAndStatus(String hotelCode, String status);
}
