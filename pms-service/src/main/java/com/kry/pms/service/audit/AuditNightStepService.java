package com.kry.pms.service.audit;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.audit.AuditNightStep;
import com.kry.pms.service.BaseService;

import java.util.List;

public interface AuditNightStepService extends BaseService<AuditNightStep>{

    HttpResponse findByHotelCodeAndBusinessDate(String code);

}