package com.kry.pms.service.marketing.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.marketing.MarketingChannelDao;
import com.kry.pms.model.persistence.marketing.MarketingChannel;
import com.kry.pms.service.marketing.MarketingChannelService;

@Service
public class  MarketingChannelServiceImpl implements  MarketingChannelService{
	@Autowired
	 MarketingChannelDao marketingChannelDao;
	 
	 @Override
	public MarketingChannel add(MarketingChannel marketingChannel) {
		return marketingChannelDao.saveAndFlush(marketingChannel);
	}

	@Override
	public void delete(String id) {
		MarketingChannel marketingChannel = marketingChannelDao.findById(id).get();
		if (marketingChannel != null) {
			marketingChannel.setDeleted(true);
		}
		marketingChannelDao.saveAndFlush(marketingChannel);
	}

	@Override
	public MarketingChannel modify(MarketingChannel marketingChannel) {
		return marketingChannelDao.saveAndFlush(marketingChannel);
	}

	@Override
	public MarketingChannel findById(String id) {
		return marketingChannelDao.getOne(id);
	}

	@Override
	public List<MarketingChannel> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return marketingChannelDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<MarketingChannel> listPage(PageRequest<MarketingChannel> prq) {
		Example<MarketingChannel> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(marketingChannelDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
