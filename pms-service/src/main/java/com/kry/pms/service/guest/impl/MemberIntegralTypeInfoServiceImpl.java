package com.kry.pms.service.guest.impl;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.guest.MemberIntegralTypeDao;
import com.kry.pms.dao.guest.MemberIntegralTypeInfoDao;
import com.kry.pms.model.persistence.guest.MemberIntegralType;
import com.kry.pms.model.persistence.guest.MemberIntegralTypeInfo;
import com.kry.pms.service.guest.MemberIntegralTypeInfoService;
import com.kry.pms.service.guest.MemberIntegralTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberIntegralTypeInfoServiceImpl implements MemberIntegralTypeInfoService {
	@Autowired
	MemberIntegralTypeInfoDao memberIntegralTypeInfoDao;

	@Override
	public MemberIntegralTypeInfo add(MemberIntegralTypeInfo entity) {
		return memberIntegralTypeInfoDao.saveAndFlush(entity);
	}

	@Override
	public void delete(String id) {
		memberIntegralTypeInfoDao.deleteById(id);
	}

	@Override
	public MemberIntegralTypeInfo modify(MemberIntegralTypeInfo memberIntegralTypeInfo) {
		return memberIntegralTypeInfoDao.saveAndFlush(memberIntegralTypeInfo);
	}

	@Override
	public MemberIntegralTypeInfo findById(String id) {
		return memberIntegralTypeInfoDao.getOne(id);
	}

	@Override
	public List<MemberIntegralTypeInfo> getAllByHotelCode(String code) {
		return memberIntegralTypeInfoDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<MemberIntegralTypeInfo> listPage(PageRequest<MemberIntegralTypeInfo> prq) {
		Example<MemberIntegralTypeInfo> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(memberIntegralTypeInfoDao.findAll(ex, req));
	}
}
