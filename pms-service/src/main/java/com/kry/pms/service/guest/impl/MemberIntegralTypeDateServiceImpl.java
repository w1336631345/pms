package com.kry.pms.service.guest.impl;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.guest.MemberIntegralTypeDateDao;
import com.kry.pms.dao.guest.MemberIntegralTypeInfoDao;
import com.kry.pms.model.persistence.guest.MemberIntegralTypeDate;
import com.kry.pms.model.persistence.guest.MemberIntegralTypeInfo;
import com.kry.pms.service.guest.MemberIntegralTypeDateService;
import com.kry.pms.service.guest.MemberIntegralTypeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberIntegralTypeDateServiceImpl implements MemberIntegralTypeDateService {
	@Autowired
	MemberIntegralTypeDateDao memberIntegralTypeDateDao;

	@Override
	public MemberIntegralTypeDate add(MemberIntegralTypeDate entity) {
		return memberIntegralTypeDateDao.saveAndFlush(entity);
	}

	@Override
	public void delete(String id) {
		memberIntegralTypeDateDao.deleteById(id);
	}

	@Override
	public MemberIntegralTypeDate modify(MemberIntegralTypeDate memberIntegralTypeDate) {
		return memberIntegralTypeDateDao.saveAndFlush(memberIntegralTypeDate);
	}

	@Override
	public MemberIntegralTypeDate findById(String id) {
		return memberIntegralTypeDateDao.getOne(id);
	}

	@Override
	public List<MemberIntegralTypeDate> getAllByHotelCode(String code) {
		return memberIntegralTypeDateDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<MemberIntegralTypeDate> listPage(PageRequest<MemberIntegralTypeDate> prq) {
		Example<MemberIntegralTypeDate> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(memberIntegralTypeDateDao.findAll(ex, req));
	}
}
