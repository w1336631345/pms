package com.kry.pms.service.guest.impl;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.guest.MemberInfoDao;
import com.kry.pms.dao.guest.MemberRechargeDao;
import com.kry.pms.model.persistence.guest.MemberInfo;
import com.kry.pms.model.persistence.guest.MemberRecharge;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.guest.MemberInfoService;
import com.kry.pms.service.guest.MemberRechargeService;
import com.kry.pms.service.sys.AccountService;
import com.kry.pms.service.sys.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberRechargeServiceImpl implements MemberRechargeService {
	@Autowired
	MemberRechargeDao memberRechargeDao;
	@Autowired
	AccountService accountService;
	@Autowired
	MemberInfoDao memberInfoDao;
	@Autowired
	UserService userService;

	@Override
	public MemberRecharge add(MemberRecharge entity) {
		MemberInfo memberInfo = memberInfoDao.findByHotelCodeAndCardNum(entity.getHotelCode(), entity.getCardNum());
		if(entity.getAmount() == null){
			entity.setAmount(0.0);
		}
		if(entity.getGiveAmount() == null){
			entity.setGiveAmount(0.0);
		}
		memberInfo.setBalance(memberInfo.getBalance() + entity.getAmount());
		memberInfo.setGivePrice(memberInfo.getGivePrice() + entity.getGiveAmount());
		memberInfoDao.saveAndFlush(memberInfo);
		return memberRechargeDao.saveAndFlush(entity);
	}

	@Override
	public void delete(String id) {
		memberRechargeDao.deleteById(id);
	}

	@Override
	public MemberRecharge modify(MemberRecharge memberRecharge) {
		return memberRechargeDao.saveAndFlush(memberRecharge);
	}

	@Override
	public MemberRecharge findById(String id) {
		return memberRechargeDao.getOne(id);
	}

	@Override
	public List<MemberRecharge> getAllByHotelCode(String code) {
		List<MemberRecharge> list = memberRechargeDao.findByHotelCode(code);
		return list;
	}
	@Override
	public List<MemberRecharge> getList(String code) {
		List<MemberRecharge> list = memberRechargeDao.findByHotelCode(code);
		for(int i=0; i<list.size(); i++){
			String createBy = list.get(i).getCreateUser();
			User user = userService.findById(createBy);
			list.get(i).setOperator(user.getUsername());
		}
		return list;
	}


	@Override
	public PageResponse<MemberRecharge> listPage(PageRequest<MemberRecharge> prq) {
		Example<MemberRecharge> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(memberRechargeDao.findAll(ex, req));
	}
}
