package com.kry.pms.dao.report;

import com.kry.pms.model.persistence.report.ReportEmpDailyBillStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReportEmpDailyBillStatDao extends JpaRepository<ReportEmpDailyBillStat, String>, JpaSpecificationExecutor<ReportEmpDailyBillStat> {

    public List<ReportEmpDailyBillStat> findByHotelCodeAndQuantityDate(String hotelCode, LocalDate quantityDate);

    public List<ReportEmpDailyBillStat> findByHotelCodeAndQuantityDateAndEmployeeId(String hotelCode, LocalDate quantityDate, String employeeId);

}
