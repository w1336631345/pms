package com.kry.pms.service.audit.impl;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.audit.AuditNightStepDao;
import com.kry.pms.dao.audit.AuditNightStepHisDao;
import com.kry.pms.model.persistence.audit.AuditNightStep;
import com.kry.pms.model.persistence.audit.AuditNightStepHis;
import com.kry.pms.model.persistence.busi.DailyVerify;
import com.kry.pms.service.audit.AuditNightStepHisService;
import com.kry.pms.service.audit.AuditNightStepService;
import com.kry.pms.service.busi.DailyVerifyService;
import com.kry.pms.service.sys.BusinessSeqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class AuditNightStepHisServiceImpl implements AuditNightStepHisService {
	@Autowired
	AuditNightStepHisDao auditNightStepHisDao;
	@Autowired
	BusinessSeqService businessSeqService;
	@Autowired
	AuditNightStepService auditNightStepService;
	@Autowired
	DailyVerifyService dailyVerifyService;


	@Override
	public AuditNightStepHis add(AuditNightStepHis entity) {
		return auditNightStepHisDao.saveAndFlush(entity);
	}

	@Override
	public void delete(String id) {

	}

	@Override
	public AuditNightStepHis modify(AuditNightStepHis auditNightStepHis) {
		return null;
	}

	@Override
	public AuditNightStepHis findById(String id) {
		return null;
	}

	@Override
	public List<AuditNightStepHis> getAllByHotelCode(String code) {
//		List<AuditNightStepHis> list = auditNightStepHisDao.findByHotelCode(code);
		return null;
	}

	@Override
	public PageResponse<AuditNightStepHis> listPage(PageRequest<AuditNightStepHis> prq) {
		return null;
	}

	@Override
	public HttpResponse findByHotelCodeAndBusinessDate(String code) {
		HttpResponse hr = new HttpResponse();
		LocalDate businessDate = businessSeqService.getBuinessDate(code);

		List<AuditNightStepHis> list = auditNightStepHisDao.findByHotelCodeAndBusinessDate(code, businessDate);
		if(list == null || list.isEmpty()){
//			DailyVerify dailyVerify = dailyVerifyService.findByHotelCodeAndBusinessDate(code, businessDate);
//			if(dailyVerify == null){
//				return hr.error(99999, "请先夜审入账");
//			}
			List<AuditNightStep> anss = auditNightStepService.getAllByHotelCode(code);
			for(int i=0 ; i<anss.size(); i++){
				AuditNightStep ans = anss.get(i);
				AuditNightStepHis ah = new AuditNightStepHis();
				ah.setStepName(ans.getStepName());
				ah.setHotelCode(code);
				ah.setBusinessDate(businessDate);
//				ah.setStartTime(LocalDateTime.now());
				ah.setResultCode("stop");
				auditNightStepHisDao.saveAndFlush(ah);
				list.add(ah);
			}
		}
		hr.setData(list);
		return hr;
	}

}
