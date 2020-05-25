package com.kry.pms.service.audit;

import com.kry.pms.model.persistence.audit.AuditNightStep;
import com.kry.pms.service.BaseService;

import java.util.List;

public interface AuditNightStepService extends BaseService<AuditNightStep>{

    List<AuditNightStep> findByHotelCodeAndBusinessDate(String code);

}