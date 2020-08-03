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
import com.kry.pms.dao.report.ReportRowDefinitionDao;
import com.kry.pms.model.persistence.report.ReportRowDefinition;
import com.kry.pms.service.report.ReportRowDefinitionService;

@Service
public class  ReportRowDefinitionServiceImpl implements  ReportRowDefinitionService{
	@Autowired
	 ReportRowDefinitionDao reportRowDefinitionDao;
	 
	 @Override
	public ReportRowDefinition add(ReportRowDefinition reportRowDefinition) {
		return reportRowDefinitionDao.saveAndFlush(reportRowDefinition);
	}

	@Override
	public void delete(String id) {
		ReportRowDefinition reportRowDefinition = reportRowDefinitionDao.findById(id).get();
		if (reportRowDefinition != null) {
			reportRowDefinition.setDeleted(Constants.DELETED_TRUE);
		}
		modify(reportRowDefinition);
	}

	@Override
	public ReportRowDefinition modify(ReportRowDefinition reportRowDefinition) {
		return reportRowDefinitionDao.saveAndFlush(reportRowDefinition);
	}

	@Override
	public ReportRowDefinition findById(String id) {
		return reportRowDefinitionDao.getOne(id);
	}

	@Override
	public List<ReportRowDefinition> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return reportRowDefinitionDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<ReportRowDefinition> listPage(PageRequest<ReportRowDefinition> prq) {
		Example<ReportRowDefinition> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(reportRowDefinitionDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
