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
import com.kry.pms.dao.report.ReportCellDefinitionDao;
import com.kry.pms.model.persistence.report.ReportCellDefinition;
import com.kry.pms.service.report.ReportCellDefinitionService;

@Service
public class  ReportCellDefinitionServiceImpl implements  ReportCellDefinitionService{
	@Autowired
	 ReportCellDefinitionDao reportCellDefinitionDao;
	 
	 @Override
	public ReportCellDefinition add(ReportCellDefinition reportCellDefinition) {
		return reportCellDefinitionDao.saveAndFlush(reportCellDefinition);
	}

	@Override
	public void delete(String id) {
		ReportCellDefinition reportCellDefinition = reportCellDefinitionDao.findById(id).get();
		if (reportCellDefinition != null) {
			reportCellDefinition.setDeleted(Constants.DELETED_TRUE);
		}
		modify(reportCellDefinition);
	}

	@Override
	public ReportCellDefinition modify(ReportCellDefinition reportCellDefinition) {
		return reportCellDefinitionDao.saveAndFlush(reportCellDefinition);
	}

	@Override
	public ReportCellDefinition findById(String id) {
		return reportCellDefinitionDao.getOne(id);
	}

	@Override
	public List<ReportCellDefinition> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return reportCellDefinitionDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<ReportCellDefinition> listPage(PageRequest<ReportCellDefinition> prq) {
		Example<ReportCellDefinition> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(reportCellDefinitionDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
