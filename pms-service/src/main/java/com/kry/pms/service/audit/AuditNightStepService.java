package com.kry.pms.service.audit;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.audit.AuditNightStep;
import com.kry.pms.service.BaseService;

import java.util.List;

public interface AuditNightStepService extends BaseService<AuditNightStep>{

    List<AuditNightStep> findByHotelCodeAndIsUsed(String code);

    HttpResponse findByHotelCodeAndBusinessDate(String code);

    //自动夜审执行过程
    HttpResponse findByHotelCodeAndBusinessDateAudit(String code);

    HttpResponse isAudit(String hotelCode);

    List<AuditNightStep> batchAdd(List<AuditNightStep> auditNightSteps);
}