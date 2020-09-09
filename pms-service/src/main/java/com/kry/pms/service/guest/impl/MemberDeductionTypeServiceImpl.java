package com.kry.pms.service.guest.impl;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.guest.MemberDeductionTypeDao;
import com.kry.pms.dao.guest.MemberIntegralTypeInfoDao;
import com.kry.pms.model.persistence.guest.MemberDeductionType;
import com.kry.pms.model.persistence.guest.MemberIntegralTypeInfo;
import com.kry.pms.service.guest.MemberDeductionTypeService;
import com.kry.pms.service.guest.MemberIntegralTypeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberDeductionTypeServiceImpl implements MemberDeductionTypeService {
	@Autowired
	MemberDeductionTypeDao memberDeductionTypeDao;

	@Override
	public MemberDeductionType add(MemberDeductionType entity) {
		return memberDeductionTypeDao.saveAndFlush(entity);
	}

	@Override
	public void delete(String id) {
		memberDeductionTypeDao.deleteById(id);
	}

	@Override
	public MemberDeductionType modify(MemberDeductionType memberDeductionType) {
		return memberDeductionTypeDao.saveAndFlush(memberDeductionType);
	}

	@Override
	public MemberDeductionType findById(String id) {
		return memberDeductionTypeDao.getOne(id);
	}

	@Override
	public List<MemberDeductionType> getAllByHotelCode(String code) {
		return memberDeductionTypeDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<MemberDeductionType> listPage(PageRequest<MemberDeductionType> prq) {
		Example<MemberDeductionType> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(memberDeductionTypeDao.findAll(ex, req));
	}
}
