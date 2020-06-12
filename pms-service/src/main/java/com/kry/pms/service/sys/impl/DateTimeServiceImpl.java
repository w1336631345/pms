package com.kry.pms.service.sys.impl;

import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.service.sys.BusinessSeqService;
import com.kry.pms.service.sys.DateTimeService;
import com.kry.pms.service.sys.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class DateTimeServiceImpl implements DateTimeService {
    @Autowired
    SystemConfigService systemConfigService;
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

    @Override
    public LocalDate getCheckInRecordStartDate(CheckInRecord cir) {
        LocalTime criticalTime = systemConfigService.getCriticalTime(cir.getHotelCode());
        LocalTime time = cir.getArriveTime().toLocalTime();
        LocalDate startDate = cir.getArriveTime().toLocalDate();
        if (time.isBefore(criticalTime)) {
            startDate = startDate.plusDays(-1);
        }
        return startDate;
    }

    @Override
    public LocalDate getStartDate(String hotelCode, LocalDateTime startTime) {
        LocalTime criticalTime = systemConfigService.getCriticalTime(hotelCode);
        LocalTime time = startTime.toLocalTime();
        LocalDate startDate = startTime.toLocalDate();
        if (time.isBefore(criticalTime)) {
            startDate = startDate.plusDays(-1);
        }
        return startDate;
    }
}
