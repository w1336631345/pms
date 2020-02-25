package com.kry.pms.service.sys.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.sys.SqlTemplateDao;
import com.kry.pms.model.persistence.sys.SqlTemplate;
import com.kry.pms.service.sys.SqlTemplateService;

@Service
public class  SqlTemplateServiceImpl implements  SqlTemplateService{
	@Autowired
	 SqlTemplateDao sqlTemplateDao;
	 
	 @Override
	public SqlTemplate add(SqlTemplate sqlTemplate) {
		return sqlTemplateDao.saveAndFlush(sqlTemplate);
	}

	@Override
	public void delete(String id) {
		SqlTemplate sqlTemplate = sqlTemplateDao.findById(id).get();
		if (sqlTemplate != null) {
			sqlTemplate.setDeleted(Constants.DELETED_TRUE);
		}
		modify(sqlTemplate);
	}

	@Override
	public SqlTemplate modify(SqlTemplate sqlTemplate) {
		return sqlTemplateDao.saveAndFlush(sqlTemplate);
	}

	@Override
	public SqlTemplate findById(String id) {
		return sqlTemplateDao.getOne(id);
	}

	@Override
	public List<SqlTemplate> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return sqlTemplateDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<SqlTemplate> listPage(PageRequest<SqlTemplate> prq) {
		Example<SqlTemplate> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(sqlTemplateDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
