package com.kry.pms.service.guest.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.guest.GuestInfoDao;
import com.kry.pms.model.persistence.guest.GuestInfo;
import com.kry.pms.service.guest.GuestInfoService;

@Service
public class GuestInfoServiceImpl implements GuestInfoService {
	@Autowired
	GuestInfoDao guestInfoDao;

	@Override
	public GuestInfo add(GuestInfo guestInfo) {
		return guestInfoDao.saveAndFlush(guestInfo);
	}

	@Override
	public void delete(String id) {
		GuestInfo guestInfo = guestInfoDao.findById(id).get();
		if (guestInfo != null) {
			guestInfo.setDeleted(Constants.DELETED_TRUE);
		}
		modify(guestInfo);
	}

	@Override
	public GuestInfo modify(GuestInfo guestInfo) {
		return guestInfoDao.saveAndFlush(guestInfo);
	}

	@Override
	public GuestInfo findById(String id) {
		return guestInfoDao.getOne(id);
	}

	@Override
	public List<GuestInfo> getAllByHotelCode(String code) {
		return null;// 默认不实现
		// return guestInfoDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<GuestInfo> listPage(PageRequest<GuestInfo> prq) {
		Example<GuestInfo> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(guestInfoDao.findAll(ex, req));
	}

	@Override
	public GuestInfo findByIdCardNum(String idCardNum) {
		return guestInfoDao.findByIdCardNum(idCardNum);
	}

}
