package com.kry.pms.service.marketing.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.marketing.MarketingSourcesDao;
import com.kry.pms.model.persistence.marketing.MarketingSources;
import com.kry.pms.service.marketing.MarketingSourcesService;

@Service
public class  MarketingSourcesServiceImpl implements  MarketingSourcesService{
	@Autowired
	 MarketingSourcesDao marketingSourcesDao;
	 
	 @Override
	public MarketingSources add(MarketingSources marketingSources) {
		 MarketingSources ms = marketingSourcesDao.findByHotelCodeAndDeletedAndCode(marketingSources.getHotelCode(), Constants.DELETED_FALSE, marketingSources.getCode());
		 if(ms == null){
			 return marketingSourcesDao.saveAndFlush(marketingSources);
		 }
		 return null;
	}

	@Override
	public void delete(String id) {
		MarketingSources marketingSources = marketingSourcesDao.findById(id).get();
		if(("M").equals(marketingSources.getRoleStatus())){
			return;//M是不可删除状态
		}
		if (marketingSources != null) {
			marketingSources.setDeleted(Constants.DELETED_TRUE);
		}
		modify(marketingSources);
	}

	@Override
	public MarketingSources modify(MarketingSources marketingSources) {
		return marketingSourcesDao.saveAndFlush(marketingSources);
	}

	@Override
	public MarketingSources findById(String id) {
		return marketingSourcesDao.getOne(id);
	}

	@Override
	public List<MarketingSources> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return marketingSourcesDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<MarketingSources> listPage(PageRequest<MarketingSources> prq) {
		Example<MarketingSources> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(marketingSourcesDao.findAll(ex, req));
	}

	@Override
	public List<MarketingSources> getAllByHotelCode(String currentHotleCode, int deleted) {
		return marketingSourcesDao.findByHotelCodeAndDeleted(currentHotleCode,deleted);
	}
	@Override
	public List<MarketingSources> getByHotelCodeAndIsUsed(String currentHotleCode, String isUsed, int deleted) {
		return marketingSourcesDao.findByHotelCodeAndIsUsedAndDeleted(currentHotleCode,isUsed,deleted);
	}

	@Override
	public List<Map<String, Object>> countRoom(String dateTime, String hotelCode){
	 	List<Map<String, Object>> list = marketingSourcesDao.countRoom(dateTime, hotelCode);
	 	return list;
	}
	 
}
