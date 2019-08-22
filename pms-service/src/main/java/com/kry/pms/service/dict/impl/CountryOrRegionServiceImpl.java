package com.kry.pms.service.dict.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.dict.CountryOrRegionDao;
import com.kry.pms.model.persistence.dict.CountryOrRegion;
import com.kry.pms.service.dict.CountryOrRegionService;

@Service
public class  CountryOrRegionServiceImpl implements  CountryOrRegionService{
	@Autowired
	 CountryOrRegionDao countryOrRegionDao;
	 
	 @Override
	public CountryOrRegion add(CountryOrRegion countryOrRegion) {
		return countryOrRegionDao.saveAndFlush(countryOrRegion);
	}

	@Override
	public void delete(String id) {
		CountryOrRegion countryOrRegion = countryOrRegionDao.findById(id).get();
		if (countryOrRegion != null) {
			countryOrRegion.setDeleted(true);
		}
		countryOrRegionDao.saveAndFlush(countryOrRegion);
	}

	@Override
	public CountryOrRegion modify(CountryOrRegion countryOrRegion) {
		return countryOrRegionDao.saveAndFlush(countryOrRegion);
	}

	@Override
	public CountryOrRegion findById(String id) {
		return countryOrRegionDao.getOne(id);
	}

	@Override
	public List<CountryOrRegion> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return countryOrRegionDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<CountryOrRegion> listPage(PageRequest<CountryOrRegion> prq) {
		Example<CountryOrRegion> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(countryOrRegionDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
