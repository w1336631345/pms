package com.kry.pms.service.sys.impl;

import com.kry.pms.service.sys.BusinessSeqService;
import com.kry.pms.service.sys.DateTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class DateTimeServiceImpl implements DateTimeService {

    @Autowired
    BusinessSeqService businessSeqService;

    @Override
    public LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    @Override
    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    @Override
    public LocalDate getBusinessDate(String hotelCode) {
        return businessSeqService.getBuinessDate(hotelCode);
    }

    @Override
    public LocalDateTime getBusinessDateTime(String hotelCode) {
        return LocalDateTime.of(businessSeqService.getBuinessDate(hotelCode), LocalTime.now());
    }
}
