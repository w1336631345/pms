package com.kry.pms.dao.audit;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.audit.AuditNightStep;

import java.time.LocalDate;
import java.util.List;

public interface AuditNightStepDao extends BaseDao<AuditNightStep> {

    List<AuditNightStep> findByHotelCode(String hotelCode);

    List<AuditNightStep> findByHotelCodeAndDeleted(String hotelCode, int deleted);

    List<AuditNightStep> findByHotelCodeAndIsUsed(String hotelCode, String isUsed);

    List<AuditNightStep> findByHotelCodeOrderBySeqNum(String hotelCode);
    List<AuditNightStep> findByHotelCodeAndIsUsedOrderBySeqNum(String hotelCode, String isUsed);

    List<AuditNightStep> findByHotelCodeAndBusinessDate(String hotelCode, LocalDate businessDate);

}
