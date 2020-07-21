package com.kry.pms.service.guest.impl;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.guest.CustomerDao;
import com.kry.pms.dao.guest.MemberInfoDao;
import com.kry.pms.dao.guest.MemberIntegralDao;
import com.kry.pms.model.persistence.guest.MemberInfo;
import com.kry.pms.model.persistence.guest.MemberIntegral;
import com.kry.pms.service.guest.MemberInfoService;
import com.kry.pms.service.guest.MemberIntegralService;
import com.kry.pms.service.sys.AccountService;
import com.kry.pms.service.sys.BusinessSeqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberIntegralServiceImpl implements MemberIntegralService {
	@Autowired
	MemberIntegralDao memberIntegralDao;
	@Autowired
	AccountService accountService;
	@Autowired
	BusinessSeqService businessSeqService;
	@Autowired
	CustomerDao customerDao;
	@Autowired
	MemberInfoDao memberInfoDao;

	@Override
	public MemberIntegral add(MemberIntegral entity) {
		entity.setIsOverdue(0);
		String orderNo = businessSeqService.fetchNextSeqNum(entity.getHotelCode(), Constants.Key.MENBER_INTEGRAL_NO);
		entity.setOrderNo(orderNo);
		MemberInfo memberInfo = memberInfoDao.findByHotelCodeAndCardNum(entity.getHotelCode(), entity.getCardNum());
		if(memberInfo.getIntegral() == null){
			memberInfo.setIntegral(0.0);
		}
		memberInfo.setIntegral(memberInfo.getIntegral() + entity.getInIntegral());
		memberInfoDao.saveAndFlush(memberInfo);
		return memberIntegralDao.saveAndFlush(entity);
	}
	@Override
	public MemberIntegral reduce(MemberIntegral entity) {
		entity.setIsOverdue(0);
		String orderNo = businessSeqService.fetchNextSeqNum(entity.getHotelCode(), Constants.Key.MENBER_INTEGRAL_NO);
		entity.setOrderNo(orderNo);
		MemberInfo memberInfo = memberInfoDao.findByHotelCodeAndCardNum(entity.getHotelCode(), entity.getCardNum());
		if(memberInfo.getIntegral() == null){
			memberInfo.setIntegral(0.0);
		}
		memberInfo.setIntegral(memberInfo.getIntegral() - entity.getOutIntegral());
		memberInfoDao.saveAndFlush(memberInfo);
		return memberIntegralDao.saveAndFlush(entity);
	}

	@Override
	public void delete(String id) {
		memberIntegralDao.deleteById(id);
	}

	@Override
	public MemberIntegral modify(MemberIntegral memberIntegral) {
		return memberIntegralDao.saveAndFlush(memberIntegral);
	}

	@Override
	public MemberIntegral findById(String id) {
		return memberIntegralDao.getOne(id);
	}

	@Override
	public List<MemberIntegral> getAllByHotelCode(String code) {
		return memberIntegralDao.findByHotelCode(code);
	}
	@Override
	public List<MemberIntegral> getList(String code, String cardNum) {
		return memberIntegralDao.findByHotelCodeAndCardNum(code, cardNum);
	}

	@Override
	public PageResponse<MemberIntegral> listPage(PageRequest<MemberIntegral> prq) {
		Example<MemberIntegral> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(memberIntegralDao.findAll(ex, req));
	}
}
