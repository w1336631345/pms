package com.kry.pms.service.busi;

import com.kry.pms.model.persistence.busi.DailyVerify;
import com.kry.pms.service.BaseService;

import java.time.LocalDate;

public interface DailyVerifyService extends BaseService<DailyVerify>{

    DailyVerify findByHotelCodeAndBusinessDate(String hotelCode, LocalDate businessDate);

    public void autoDailyVerify(String hotelCode);

}