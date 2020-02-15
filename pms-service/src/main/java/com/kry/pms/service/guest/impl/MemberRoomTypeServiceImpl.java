package com.kry.pms.service.guest.impl;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.guest.MemberRoomTypeDao;
import com.kry.pms.dao.guest.VipRoomTypeDao;
import com.kry.pms.model.persistence.guest.MemberRoomType;
import com.kry.pms.model.persistence.guest.VipRoomType;
import com.kry.pms.service.guest.MemberRoomTypeService;
import com.kry.pms.service.guest.VipRoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberRoomTypeServiceImpl implements MemberRoomTypeService {
	@Autowired
	MemberRoomTypeDao memberRoomTypeDao;


	@Override
	public MemberRoomType add(MemberRoomType entity) {
		return memberRoomTypeDao.saveAndFlush(entity);
	}

	@Override
	public void delete(String id) {
		memberRoomTypeDao.deleteById(id);
	}

	@Override
	public MemberRoomType modify(MemberRoomType MemberRoomType) {
		return memberRoomTypeDao.saveAndFlush(MemberRoomType);
	}

	@Override
	public MemberRoomType findById(String id) {
		return memberRoomTypeDao.getOne(id);
	}

	@Override
	public List<MemberRoomType> getAllByHotelCode(String code) {
		return memberRoomTypeDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<MemberRoomType> listPage(PageRequest<MemberRoomType> prq) {
		Example<MemberRoomType> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(memberRoomTypeDao.findAll(ex, req));
	}
}
