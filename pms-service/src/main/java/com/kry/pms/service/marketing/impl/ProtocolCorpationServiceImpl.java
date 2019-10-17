package com.kry.pms.service.marketing.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.marketing.ProtocolCorpationDao;
import com.kry.pms.model.persistence.marketing.ProtocolCorpation;
import com.kry.pms.service.marketing.ProtocolCorpationService;

@Service
public class  ProtocolCorpationServiceImpl implements  ProtocolCorpationService{
	@Autowired
	 ProtocolCorpationDao protocolCorpationDao;
	 
	 @Override
	public ProtocolCorpation add(ProtocolCorpation protocolCorpation) {
		return protocolCorpationDao.saveAndFlush(protocolCorpation);
	}

	@Override
	public void delete(String id) {
		ProtocolCorpation protocolCorpation = protocolCorpationDao.findById(id).get();
		if (protocolCorpation != null) {
			protocolCorpation.setDeleted(Constants.DELETED_TRUE);
		}
		protocolCorpationDao.saveAndFlush(protocolCorpation);
	}

	@Override
	public ProtocolCorpation modify(ProtocolCorpation protocolCorpation) {
		return protocolCorpationDao.saveAndFlush(protocolCorpation);
	}

	@Override
	public ProtocolCorpation findById(String id) {
		return protocolCorpationDao.getOne(id);
	}

	@Override
	public List<ProtocolCorpation> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return protocolCorpationDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<ProtocolCorpation> listPage(PageRequest<ProtocolCorpation> prq) {
		Example<ProtocolCorpation> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(protocolCorpationDao.findAll(ex, req));
	}

	@Override
	public List<ProtocolCorpation> queryByNameOrCode(String key, String currentHotleCode) {
		if(key.contains("%")) {
			key.replace("%", "");
		}
		key = "%"+key+"%";
		return protocolCorpationDao.fetchByKey(key,currentHotleCode,Constants.DELETED_FALSE);
	}
	 
	 
	 
	 
}
