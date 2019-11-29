package com.kry.pms.dao.busi;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.busi.DailyVerify;

import java.time.LocalDate;
import java.util.List;

public interface DailyVerifyDao extends BaseDao<DailyVerify>{

    DailyVerify findByHotelCodeAndBusinessDate(String hotelCode, LocalDate businessDate);
}
