package com.kry.pms.service.sys;

import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
public interface DateTimeService {
    public LocalDate getCurrentDate();
    public LocalDateTime getCurrentDateTime();
    public LocalDate getBusinessDate(String hotelCode);
    public LocalDateTime getBusinessDateTime(String hotelCode);
}
