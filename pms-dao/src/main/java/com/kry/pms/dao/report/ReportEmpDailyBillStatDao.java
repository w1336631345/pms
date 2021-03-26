package com.kry.pms.dao.report;

import com.kry.pms.model.persistence.report.ReportEmpDailyBillStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReportEmpDailyBillStatDao extends JpaRepository<ReportEmpDailyBillStat, String>, JpaSpecificationExecutor<ReportEmpDailyBillStat> {

    public List<ReportEmpDailyBillStat> findByHotelCodeAndQuantityDate(String hotelCode, LocalDate quantityDate);

    public List<ReportEmpDailyBillStat> findByHotelCodeAndQuantityDateAndEmployeeIdAndShift(String hotelCode, LocalDate quantityDate, String employeeId,String shift);

    @Modifying
    @Query(value = "DELETE FROM t_report_daily_bill_stat WHERE hotel_code =?1 AND quantity_date =?2",nativeQuery = true)
    void deleteReportDailyBillStat(String hotelCode, LocalDate quantityDate);

    @Modifying
    @Query(value = "DELETE FROM t_report_emp_daily_bill_stat WHERE hotel_code =?1 AND quantity_date =?2", nativeQuery = true)
    void deleteReportEmpDailyBillStat(String hotelCode, LocalDate quantityDate);

    @Modifying
    @Query(value = "DELETE FROM t_report_daily_summation WHERE hotel_code =?1 AND quantity_date =?2", nativeQuery = true)
    void deleteReportDailySummation(String hotelCode, LocalDate quantityDate);

    @Modifying
    @Query(value = "DELETE FROM t_report_daily_balance WHERE hotel_code =?1 AND quantity_date =?2", nativeQuery = true)
    void deleteReportDailyBalance(String hotelCode, LocalDate quantityDate);
}
