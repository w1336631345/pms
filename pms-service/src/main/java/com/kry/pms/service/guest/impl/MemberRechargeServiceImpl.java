package com.kry.pms.service.guest.impl;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.guest.MemberInfoDao;
import com.kry.pms.dao.guest.MemberRechargeDao;
import com.kry.pms.dao.org.EmployeeDao;
import com.kry.pms.model.persistence.busi.Bill;
import com.kry.pms.model.persistence.guest.MemberInfo;
import com.kry.pms.model.persistence.guest.MemberRecharge;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.busi.BillService;
import com.kry.pms.service.goods.ProductService;
import com.kry.pms.service.guest.MemberRechargeService;
import com.kry.pms.service.org.EmployeeService;
import com.kry.pms.service.sys.AccountService;
import com.kry.pms.service.sys.BusinessSeqService;
import com.kry.pms.service.sys.UserService;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
	@Autowired
	BillService billService;
	@Autowired
	ProductService productService;
	@Autowired
	EmployeeDao employeeDao;
	@Autowired
	BusinessSeqService businessSeqService;

	@Override
	public MemberRecharge add(MemberRecharge entity) {
		MemberInfo memberInfo = memberInfoDao.findByHotelCodeAndCardNum(entity.getHotelCode(), entity.getCardNum());
		if(entity.getAmount() == null){
			entity.setAmount(0.0);
		}
		if(entity.getGiveAmount() == null){
			entity.setGiveAmount(0.0);
		}
		entity.setIsOverdue(0);
		memberInfo.setBalance(memberInfo.getBalance() + entity.getAmount());
		memberInfo.setGivePrice(memberInfo.getGivePrice() + entity.getGiveAmount());
		memberInfoDao.saveAndFlush(memberInfo);
		return memberRechargeDao.saveAndFlush(entity);
	}
	@Override
	public HttpResponse recharge(MemberRecharge entity) {
		HttpResponse hr = new HttpResponse();
		MemberInfo memberInfo = memberInfoDao.findByHotelCodeAndCardNum(entity.getHotelCode(), entity.getCardNum());
		if(entity.getAmount() == null){
			entity.setAmount(0.0);
		}
		if(entity.getGiveAmount() == null){
			entity.setGiveAmount(0.0);
		}
		entity.setIsOverdue(0);
		memberInfo.setBalance(memberInfo.getBalance() + entity.getAmount());
		memberInfo.setGivePrice(memberInfo.getGivePrice() + entity.getGiveAmount());
		memberInfoDao.saveAndFlush(memberInfo);
		entity = memberRechargeDao.saveAndFlush(entity);

		//入账
		Bill bill = new Bill();
		Employee emp = employeeDao.findByUserId(entity.getCreateUser());
		bill.setProduct(entity.getPayType());
		bill.setTotal(entity.getAmount());
		bill.setCost(entity.getAmount());
		bill.setQuantity(1);
		bill.setAccount(memberInfo.getAccount());
		bill.setHotelCode(entity.getHotelCode());
		bill.setShiftCode(entity.getShiftCode());
		bill.setOperationRemark("会员充值");
		bill.setOperationEmployee(emp);
		LocalDate businessDate = businessSeqService.getBuinessDate(entity.getHotelCode());
		bill.setBusinessDate(businessDate);
		billService.add(bill);

		hr.setData(entity);
		return hr;
	}
	//使用金额
	@Override
	public HttpResponse use(MemberRecharge entity) {
		HttpResponse hr = new HttpResponse();
		entity.setIsOverdue(0);
		MemberInfo memberInfo = memberInfoDao.findByHotelCodeAndCardNum(entity.getHotelCode(), entity.getCardNum());
		if(memberInfo.getBalance() == null || memberInfo.getBalance() == 0.0){
			return hr.error("余额不足");
		}
		memberInfo.setBalance(memberInfo.getBalance() - entity.getUseAmount());
		memberInfo.setGivePrice(memberInfo.getGivePrice() - entity.getUseGiveAmount());
		memberInfoDao.saveAndFlush(memberInfo);
		hr.setData(memberRechargeDao.saveAndFlush(entity));
		return hr;
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
			if(createBy != null){
				User user = userService.findById(createBy);
				list.get(i).setOperator(user.getUsername());
			}
		}
		return list;
	}
	@Override
	public List<MemberRecharge> getByHotelCodeAndCardNum(String code, String cardNum) {
		List<MemberRecharge> list = memberRechargeDao.findByHotelCodeAndCardNum(code, cardNum);
		for(int i=0; i<list.size(); i++){
			String createBy = list.get(i).getCreateUser();
			if(createBy != null){
				User user = userService.findById(createBy);
				list.get(i).setOperator(user.getUsername());
			}
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

	//定时计算过期的金额
	@Override
	@Transient
	public void boOverdueList(String hotelCode) {
		//把所有过期的卡号查出来
		List<String> cardNums = memberRechargeDao.boOverdueCardNumList(hotelCode);
		for(int i=0; i<cardNums.size(); i++){
			String cardNum = cardNums.get(i);
			//根据卡号查询过期积分记录
			List<MemberRecharge> overdue = memberRechargeDao.boOverdueListByCardNum(hotelCode, cardNum);
			Map<String, Object> amountMap = memberRechargeDao.sumAmount(hotelCode, cardNum);
			Double amount = MapUtils.getDouble(amountMap, "amount");//总共过期的充值金额
			Double giveAmount = MapUtils.getDouble(amountMap, "give_amount");//总共过期赠送金额
			for(int j=0; j<overdue.size(); j++){
				MemberRecharge mr = overdue.get(j);
				mr.setIsOverdue(1);//将原数据状态改为过期
				memberRechargeDao.save(mr);
			}
			Map<String, Object> useMap = memberRechargeDao.sumUseAmount(hotelCode, cardNum);
			Double use_amount = MapUtils.getDouble(useMap, "use_amount");//总共使用的金额
			Double use_give_amount = MapUtils.getDouble(useMap, "use_give_amount");//总使用赠送金额
			Double nowOverAmount = 0.0;//今日实际过期金额
			Double nowOverGiveAmount = 0.0;//今日实际过期赠送金额
			Map<String, Object> overMap = memberRechargeDao.overAmount(hotelCode, cardNum);//过期金额查询
			Double overAmount = MapUtils.getDouble(overMap, "over_amount");//过期金额
			Double overGiveAmount = MapUtils.getDouble(overMap, "over_give_amount");//过期赠送金额
			//如果总过期金额 > 总使用金额， 实际总过期金额 = 总过期金额 - 总使用金额, 否则过期之前已经被使用，实际过期金额为0
			if(amount > use_amount){
				Double allOverAmount = amount - use_amount;//实际总过期金额
				if(allOverAmount > overAmount){
					nowOverAmount = allOverAmount - overAmount;//实际总过期金额 - 以前过期金额 = 今日新增过期金额
				}
			}
			//如果总过期赠送金额 > 总使用赠送金额， 实际总过期赠送金额 = 总过期赠送金额 - 总使用赠送金额, 否则过期之前已经被使用，实际过期赠送金额为0
			if(giveAmount > use_give_amount){
				Double allOverGiveAmount = giveAmount - use_give_amount;//实际总过期赠送金额
				if(allOverGiveAmount > overGiveAmount){
					nowOverGiveAmount = allOverGiveAmount - overGiveAmount;//实际总过期赠送金额 - 以前过期赠送金额 = 今日新增过期赠送金额
				}
			}
			if(nowOverAmount > 0 || nowOverGiveAmount > 0){
				MemberRecharge entity = new MemberRecharge();
				entity.setIsOverdue(1);
				entity.setOverAmount(nowOverAmount);
				entity.setOverGiveAmount(nowOverGiveAmount);
				entity.setRechargeOrUse("O");
				entity.setCardNum(cardNum);
				entity.setMacNum(overdue.get(0).getMacNum());
				entity.setRemark("过期");
				entity.setRechargeDate(LocalDate.now());
				entity.setCreateDate(LocalDateTime.now());
				entity.setHotelCode(hotelCode);
				memberRechargeDao.save(entity);
				MemberInfo memberInfo = memberInfoDao.findByHotelCodeAndCardNum(entity.getHotelCode(), entity.getCardNum());
				memberInfo.setBalance(memberInfo.getBalance() - nowOverAmount);
				memberInfo.setGivePrice(memberInfo.getGivePrice() - nowOverGiveAmount);
				memberInfoDao.saveAndFlush(memberInfo);
			}

		}
	}
}
