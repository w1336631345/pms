package com.kry.pms.service.audit;

import com.kry.pms.model.persistence.audit.AuditNightStep;
import com.kry.pms.model.persistence.audit.AuditNightStepHis;
import com.kry.pms.service.BaseService;

import java.util.List;

public interface AuditNightStepHisService extends BaseService<AuditNightStepHis>{

    List<AuditNightStepHis> findByHotelCodeAndBusinessDate(String code);

    List<AuditNightStepHis> stepList(String currentHotleCode);
}