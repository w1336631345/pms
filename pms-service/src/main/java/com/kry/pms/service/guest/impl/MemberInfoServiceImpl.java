package com.kry.pms.service.guest.impl;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.guest.CustomerDao;
import com.kry.pms.dao.guest.MemberInfoDao;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.model.persistence.guest.MemberInfo;
import com.kry.pms.service.guest.MemberInfoService;
import com.kry.pms.service.sys.AccountService;
import com.kry.pms.service.sys.BusinessSeqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberInfoServiceImpl implements MemberInfoService {
	@Autowired
	MemberInfoDao memberInfoDao;
	@Autowired
	AccountService accountService;
	@Autowired
	BusinessSeqService businessSeqService;
	@Autowired
	CustomerDao customerDao;

	@Override
	public MemberInfo add(MemberInfo entity) {
		if(entity.getBalance() == null){
			entity.setBalance(0.0);
		}
		if(entity.getGivePrice() == null){
			entity.setGivePrice(0.0);
		}
		if(entity.getIntegral() == null){
			entity.setIntegral(0.0);
		}
		if(entity.getCardNum() == null){
			String num = businessSeqService.fetchNextSeqNum(entity.getHotelCode(), Constants.Key.MENBER_NUM);
			entity.setCardNum(num);
		}
//		Customer cust = customerDao.getOne(entity.getCustomer().getId());
//		entity.setCustomer(cust);
		entity.setName(entity.getCustomer().getName());
		accountService.createMemberAccount(entity.getName(), entity.getHotelCode());
		return memberInfoDao.saveAndFlush(entity);
	}

	@Override
	public void delete(String id) {
		memberInfoDao.deleteById(id);
	}

	@Override
	public MemberInfo modify(MemberInfo memberType) {
		return memberInfoDao.saveAndFlush(memberType);
	}

	@Override
	public MemberInfo findById(String id) {
		return memberInfoDao.getOne(id);
	}

	@Override
	public List<MemberInfo> getAllByHotelCode(String code) {
		return memberInfoDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<MemberInfo> listPage(PageRequest<MemberInfo> prq) {
		Example<MemberInfo> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(memberInfoDao.findAll(ex, req));
	}
}
