package com.kry.pms.service.audit.impl;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.audit.AuditNightStepDao;
import com.kry.pms.model.persistence.audit.AuditNightStep;
import com.kry.pms.model.persistence.audit.AuditNightStepHis;
import com.kry.pms.model.persistence.audit.AuditNightStepParam;
import com.kry.pms.model.persistence.busi.DailyVerify;
import com.kry.pms.service.audit.AuditNightStepHisService;
import com.kry.pms.service.audit.AuditNightStepParamService;
import com.kry.pms.service.audit.AuditNightStepService;
import com.kry.pms.service.busi.DailyVerifyService;
import com.kry.pms.service.sys.BusinessSeqService;
import com.kry.pms.service.sys.SqlTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
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
	@Autowired
	DailyVerifyService dailyVerifyService;


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
		List<AuditNightStep> list = auditNightStepDao.findByHotelCode(code);
		return list;
	}

	@Override
	public PageResponse<AuditNightStep> listPage(PageRequest<AuditNightStep> prq) {
		return null;
	}

	@Override
	public HttpResponse findByHotelCodeAndBusinessDate(String code) {
		HttpResponse hr = new HttpResponse();
		LocalDate businessDate = businessSeqService.getBuinessDate(code);

		DailyVerify dailyVerify = dailyVerifyService.findByHotelCodeAndBusinessDate(code, businessDate);
		if(dailyVerify == null){
			return hr.error(99999, "请先夜审入账");
		}
//		List<AuditNightStep> list = auditNightStepDao.findByHotelCodeAndBusinessDate(code, businessDate);
		List<AuditNightStep> list = auditNightStepDao.findByHotelCode(code);
		List<AuditNightStepHis> anss = auditNightStepHisService.findByHotelCodeAndBusinessDate(code);
		for(int i=0; i<list.size(); i++){
			AuditNightStepHis ansh = anss.get(i);
			ansh.setStartTime(LocalDateTime.now());
			ansh.setResultCode("loading");
			auditNightStepHisService.add(ansh);
			AuditNightStep ans = list.get(i);
			if(ans.getProcessName() == null || "".equals(ans.getProcessName())){
				try {
					Random random = new Random();
					int s = random.nextInt(5000)%(5000-1000+1) + 1000;
					//线程睡眠一会儿，用来模拟后面生成报表步骤是的耗时
					Thread.sleep(s);
					ansh.setResultCode("success");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else {
				List<AuditNightStepParam> params = ans.getParams();
				try {
					Map<String, Object> map = auditNightStepParamService.toMapParams(params);
					List<Map<String, Object>> rl = sqlTemplateService.storedProcedure(code, businessDate, ans.getProcessName(), map);
					ansh.setResultCode("success");
				}catch (Exception e) {
					ansh.setResultCode("error");
					ansh.setResultMsg(e.getMessage());
				}

			}
			ansh.setEndTime(LocalDateTime.now());
			Duration duration = Duration.between(ansh.getStartTime(),ansh.getEndTime());
			ansh.setDuration(String.valueOf(duration.toMillis()));
			auditNightStepHisService.add(ansh);
		}
		businessSeqService.plusBuinessDate(code);//营业日期+1
		hr.addData(list);
		return hr;
	}

}
