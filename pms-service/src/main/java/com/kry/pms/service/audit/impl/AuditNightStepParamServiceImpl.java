package com.kry.pms.service.audit.impl;

import com.kry.pms.dao.audit.AuditNightStepParamDao;
import com.kry.pms.model.persistence.audit.AuditNightStepParam;
import com.kry.pms.service.audit.AuditNightStepParamService;
import com.kry.pms.service.sys.BusinessSeqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuditNightStepParamServiceImpl implements AuditNightStepParamService {
	@Autowired
	AuditNightStepParamDao auditNightStepParamDao;
	@Autowired
	BusinessSeqService businessSeqService;


	@Override
	public AuditNightStepParam add(AuditNightStepParam auditNightStepParam) {
		AuditNightStepParam ansp = auditNightStepParamDao.save(auditNightStepParam);
		return ansp;
	}

	@Override
	public AuditNightStepParam update(AuditNightStepParam auditNightStepParam) {
		AuditNightStepParam ansp = auditNightStepParamDao.saveAndFlush(auditNightStepParam);
		return ansp;
	}

	@Override
	public void deleted(String id) {
		auditNightStepParamDao.deleteById(id);
	}

	@Override
	public Map<String, Object> toMapParams(List<AuditNightStepParam> list){
		Map<String, Object> map = new HashMap<>();
		for(int i=0; i<list.size(); i++){
			AuditNightStepParam a = list.get(i);
			if("String".equals(a.getValueType())){
				map.put(a.getMapKey(), a.getMapValue());
			}
			if("Integer".equals(a.getValueType())){
				map.put(a.getMapKey(), Integer.parseInt(a.getMapValue()));
			}
			if("LocalDate".equals(a.getValueType())){
				map.put(a.getMapKey(), LocalDate.parse(a.getMapValue()));
			}
			if("LocalDateTime".equals(a.getValueType())){
				map.put(a.getMapKey(), LocalDateTime.parse(a.getMapValue()));
			}
			if("Double".equals(a.getValueType())){
				map.put(a.getMapKey(), Double.parseDouble(a.getMapValue()));
			}
			if("Float".equals(a.getValueType())){
				map.put(a.getMapKey(), Float.parseFloat(a.getMapValue()));
			}
			if("Long".equals(a.getValueType())){
				map.put(a.getMapKey(), Long.parseLong(a.getMapValue()));
			}
			if("Boolean".equals(a.getValueType())){
				map.put(a.getMapKey(), Boolean.parseBoolean(a.getMapValue()));
			}
		}
		return map;
	}
}
