package com.kry.pms.service.guest.impl;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.guest.MemberLevelDao;
import com.kry.pms.model.persistence.guest.MemberLevel;
import com.kry.pms.service.guest.MemberLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberLevelServiceImpl implements MemberLevelService {
	@Autowired
	MemberLevelDao memberLevelDao;

	@Override
	public MemberLevel add(MemberLevel entity) {
		return memberLevelDao.saveAndFlush(entity);
	}

	@Override
	public void delete(String id) {
		memberLevelDao.deleteById(id);
	}

	@Override
	public MemberLevel modify(MemberLevel memberLevel) {
		return memberLevelDao.saveAndFlush(memberLevel);
	}

	@Override
	public MemberLevel findById(String id) {
		return memberLevelDao.getOne(id);
	}

	@Override
	public List<MemberLevel> getAllByHotelCode(String code) {
		return memberLevelDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<MemberLevel> listPage(PageRequest<MemberLevel> prq) {
		Example<MemberLevel> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(memberLevelDao.findAll(ex, req));
	}
}
