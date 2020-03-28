package com.kry.pms.service.guest.impl;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.guest.MemberTypeDao;
import com.kry.pms.model.persistence.guest.MemberType;
import com.kry.pms.service.guest.MemberTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberTypeServiceImpl implements MemberTypeService {
	@Autowired
	MemberTypeDao memberTypeDao;


	@Override
	public MemberType add(MemberType entity) {
		return memberTypeDao.saveAndFlush(entity);
	}

	@Override
	public void delete(String id) {
		memberTypeDao.deleteById(id);
	}

	@Override
	public MemberType modify(MemberType memberType) {
		return memberTypeDao.saveAndFlush(memberType);
	}

	@Override
	public MemberType findById(String id) {
		return memberTypeDao.getOne(id);
	}

	@Override
	public List<MemberType> getAllByHotelCode(String code) {
		return memberTypeDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<MemberType> listPage(PageRequest<MemberType> prq) {
		Example<MemberType> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(memberTypeDao.findAll(ex, req));
	}
}
