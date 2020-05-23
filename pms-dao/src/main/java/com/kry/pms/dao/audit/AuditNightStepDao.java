package com.kry.pms.dao.audit;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.pay.WechatMerchants;
import com.kry.pms.model.persistence.report.AuditNightStep;

import java.time.LocalDate;
import java.util.List;

public interface AuditNightStepDao extends BaseDao<AuditNightStep> {

    List<AuditNightStep> findByHotelCode(String hotelCode);

    List<AuditNightStep> findByHotelCodeAndBusinessDate(String hotelCode, LocalDate businessDate);

}
