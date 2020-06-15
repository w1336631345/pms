package com.kry.pms.service.sys;

import com.kry.pms.model.persistence.busi.CheckInRecord;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface DateTimeService {
    public LocalDate getCurrentDate();

    public LocalDateTime getCurrentDateTime();

    public LocalDate getBusinessDate(String hotelCode);

    public LocalDateTime getBusinessDateTime(String hotelCode);

    public LocalDate getCheckInRecordStartDate(CheckInRecord cir);

    public LocalDate getStartDate(String hotelCode, LocalDateTime startTime);

    LocalDateTime getBusinessDateTimeBeforeDawn(String hotelCode);
}
