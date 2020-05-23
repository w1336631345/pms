package com.kry.pms.service.audit;

import com.kry.pms.model.persistence.audit.AuditNightStepHis;
import com.kry.pms.model.persistence.audit.AuditNightStepParam;

import java.util.List;
import java.util.Map;

public interface AuditNightStepParamService{

    AuditNightStepParam add(AuditNightStepParam auditNightStepParam);

    AuditNightStepParam update(AuditNightStepParam auditNightStepParam);

    void deleted(String id);

    Map<String, Object> toMapParams(List<AuditNightStepParam> list);
}