package com.kry.pms.service.report.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.report.ReportBaseValueDefinitionDao;
import com.kry.pms.model.persistence.report.ReportBaseValueDefinition;
import com.kry.pms.service.report.ReportBaseValueDefinitionService;

@Service
public class  ReportBaseValueDefinitionServiceImpl implements  ReportBaseValueDefinitionService{
	@Autowired
	 ReportBaseValueDefinitionDao reportBaseValueDefinitionDao;
	 
	 @Override
	public ReportBaseValueDefinition add(ReportBaseValueDefinition reportBaseValueDefinition) {
		return reportBaseValueDefinitionDao.saveAndFlush(reportBaseValueDefinition);
	}

	@Override
	public void delete(String id) {
		ReportBaseValueDefinition reportBaseValueDefinition = reportBaseValueDefinitionDao.findById(id).get();
		if (reportBaseValueDefinition != null) {
			reportBaseValueDefinition.setDeleted(Constants.DELETED_TRUE);
		}
		modify(reportBaseValueDefinition);
	}

	@Override
	public ReportBaseValueDefinition modify(ReportBaseValueDefinition reportBaseValueDefinition) {
		return reportBaseValueDefinitionDao.saveAndFlush(reportBaseValueDefinition);
	}

	@Override
	public ReportBaseValueDefinition findById(String id) {
		return reportBaseValueDefinitionDao.getOne(id);
	}

	@Override
	public List<ReportBaseValueDefinition> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return reportBaseValueDefinitionDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<ReportBaseValueDefinition> listPage(PageRequest<ReportBaseValueDefinition> prq) {
		Example<ReportBaseValueDefinition> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(reportBaseValueDefinitionDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
