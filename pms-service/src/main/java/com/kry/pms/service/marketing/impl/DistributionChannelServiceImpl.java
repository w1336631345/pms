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
import com.kry.pms.dao.marketing.DistributionChannelDao;
import com.kry.pms.model.persistence.marketing.DistributionChannel;
import com.kry.pms.service.marketing.DistributionChannelService;

@Service
public class  DistributionChannelServiceImpl implements  DistributionChannelService{
	@Autowired
	 DistributionChannelDao distributionChannelDao;
	 
	 @Override
	public DistributionChannel add(DistributionChannel distributionChannel) {
		 DistributionChannel dc = distributionChannelDao.findByHotelCodeAndDeletedAndCode(distributionChannel.getHotelCode(), Constants.DELETED_FALSE, distributionChannel.getCode());
		 if(dc == null){
			 return distributionChannelDao.saveAndFlush(distributionChannel);
		 }
		 return null;
	}

	@Override
	public void delete(String id) {
		DistributionChannel distributionChannel = distributionChannelDao.findById(id).get();
		if (distributionChannel != null) {
			distributionChannel.setDeleted(Constants.DELETED_TRUE);
		}
		modify(distributionChannel);
	}

	@Override
	public DistributionChannel modify(DistributionChannel distributionChannel) {
		return distributionChannelDao.saveAndFlush(distributionChannel);
	}

	@Override
	public DistributionChannel findById(String id) {
		return distributionChannelDao.getOne(id);
	}

	@Override
	public List<DistributionChannel> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return distributionChannelDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<DistributionChannel> listPage(PageRequest<DistributionChannel> prq) {
		Example<DistributionChannel> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(distributionChannelDao.findAll(ex, req));
	}

	@Override
	public List<DistributionChannel> getAllByHotelCode(String currentHotleCode, int deleted) {
		// TODO Auto-generated method stub
		return distributionChannelDao.findByHotelCodeAndDeleted(currentHotleCode,deleted);
	}
	 
	 
	 
	 
}
