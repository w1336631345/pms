package com.kry.pms.dao.report;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.report.BusinessReport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface BusinessReportDao extends BaseDao<BusinessReport> {

    @Query(nativeQuery = true, value = " select * from t_report_business " +
            " where 1=1 " +
            "  and if(:hotelCode is not null && :hotelCode != '', hotel_code=:hotelCode, 1=1 ) " +
            "  and if(:businessDate is not null && :businessDate != '', DATE_FORMAT(business_date, '%Y-%m-%d') =:businessDate, 1=1 ) " +
            "  and if(:projectType is not null && :projectType != '', project_type =:projectType, 1=1 ) " +
            " order by sort, number_ desc ")
    List<BusinessReport> getByBusinessDate(@Param("businessDate") String businessDate, @Param("hotelCode") String hotelCode, @Param("projectType") String projectType);

}
