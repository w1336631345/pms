package com.kry.pms.service.audit.impl;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.audit.AuditNightStepDao;
import com.kry.pms.model.persistence.audit.AuditNightStep;
import com.kry.pms.model.persistence.audit.AuditNightStepParam;
import com.kry.pms.service.audit.AuditNightStepHisService;
import com.kry.pms.service.audit.AuditNightStepParamService;
import com.kry.pms.service.audit.AuditNightStepService;
import com.kry.pms.service.sys.BusinessSeqService;
import com.kry.pms.service.sys.SqlTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AuditNightStepServiceImpl implements AuditNightStepService {
	@Autowired
	AuditNightStepDao auditNightStepDao;
	@Autowired
	BusinessSeqService businessSeqService;
	@Autowired
	SqlTemplateService sqlTemplateService;
	@Autowired
	AuditNightStepHisService auditNightStepHisService;
	@Autowired
	AuditNightStepParamService auditNightStepParamService;


	@Override
	public AuditNightStep add(AuditNightStep entity) {
		LocalDate businessDate = businessSeqService.getBuinessDate(entity.getHotelCode());
		entity.setBusinessDate(businessDate);
		AuditNightStep ans = auditNightStepDao.save(entity);
		return ans;
	}

	@Override
	public void delete(String id) {

	}

	@Override
	public AuditNightStep modify(AuditNightStep auditNightStep) {
		AuditNightStep ans = auditNightStepDao.saveAndFlush(auditNightStep);
		return ans;
	}

	@Override
	public AuditNightStep findById(String id) {
		return null;
	}

	@Override
	public List<AuditNightStep> getAllByHotelCode(String code) {
//		List<AuditNightStep> list = auditNightStepDao.findByHotelCode(code);
		return null;
	}

	@Override
	public PageResponse<AuditNightStep> listPage(PageRequest<AuditNightStep> prq) {
		return null;
	}

	@Override
	public List<AuditNightStep> findByHotelCodeAndBusinessDate(String code) {
		LocalDate businessDate = businessSeqService.getBuinessDate(code);

		List<AuditNightStep> list = auditNightStepDao.findByHotelCodeAndBusinessDate(code, businessDate);
		for(int i=0; i<list.size(); i++){
			AuditNightStep ans = list.get(i);
			List<AuditNightStepParam> params = ans.getParams();
			Map<String, Object> map = auditNightStepParamService.toMapParams(params);
			List<Map<String, Object>> rl = sqlTemplateService.storedProcedure(code, ans.getProcessName(), map);
		}
		return list;
	}

	@Override
	public List<AuditNightStep> stepList(String hotleCode) {
		LocalDate businessDate = businessSeqService.getBuinessDate(hotleCode);
		List<AuditNightStep> list = auditNightStepDao.findByHotelCodeAndBusinessDate(hotleCode, businessDate);
		if(list != null && !list.isEmpty()){
			return list;
		}else {
			List<String> setp = new ArrayList<>();
			setp.add("夜核前自动补过房费");
			setp.add("包价入账及调整");
			setp.add("稽核独占");
			setp.add("主要表备份");
			List<AuditNightStep> rList = new ArrayList<>();
			for(int i=0; i<setp.size(); i++){
				AuditNightStep ans = new AuditNightStep();
				ans.setStartTime(LocalDateTime.now());
				ans.setHotelCode(hotleCode);
				ans.setBusinessDate(businessDate);
				ans.setStepName(setp.get(i));
				ans.setStatus("loading");
				auditNightStepDao.saveAndFlush(ans);
				try {
					Random random = new Random();
					int s = random.nextInt(5000)%(5000-1000+1) + 1000;
					//线程睡眠一会儿，用来模拟后面生成报表步骤是的耗时
					Thread.sleep(s);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				ans.setStatus("success");
				ans.setEndTime(LocalDateTime.now());
				auditNightStepDao.saveAndFlush(ans);
				rList.add(ans);
			}
			return rList;
		}
	}
}
