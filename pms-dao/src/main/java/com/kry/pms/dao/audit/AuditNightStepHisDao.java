package com.kry.pms.dao.audit;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.audit.AuditNightStep;
import com.kry.pms.model.persistence.audit.AuditNightStepHis;

import java.time.LocalDate;
import java.util.List;

public interface AuditNightStepHisDao extends BaseDao<AuditNightStepHis> {

    List<AuditNightStepHis> findByHotelCode(String hotelCode);

    List<AuditNightStepHis> findByHotelCodeAndBusinessDate(String hotelCode, LocalDate businessDate);

}
