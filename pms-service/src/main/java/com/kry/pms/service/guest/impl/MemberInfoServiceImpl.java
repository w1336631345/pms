package com.kry.pms.service.guest.impl;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.guest.CustMarketDao;
import com.kry.pms.dao.guest.CustomerDao;
import com.kry.pms.dao.guest.MemberInfoDao;
import com.kry.pms.model.persistence.guest.CustMarket;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.model.persistence.guest.MemberInfo;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.service.guest.MemberInfoService;
import com.kry.pms.service.guest.MemberIntegralService;
import com.kry.pms.service.guest.MemberRechargeService;
import com.kry.pms.service.sys.AccountService;
import com.kry.pms.service.sys.BusinessSeqService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
	@Autowired
	MemberIntegralService memberIntegralService;
	@Autowired
	MemberRechargeService memberRechargeService;
	@Autowired
	CustMarketDao custMarketDao;

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
		//?????????????????????????????????????????????????????????
//		if(entity.getCardNum() == null){
//			String num = businessSeqService.fetchNextSeqNum(entity.getHotelCode(), Constants.Key.MENBER_NUM);
//			entity.setCardNum(num);
//		}
		entity.setName(entity.getCustomer().getName());
		Account account = accountService.createMemberAccount(entity.getCustomer(), entity.getHotelCode());
		entity.setAccount(account);
		entity.setCardNum(account.getCode());//?????????????????????????????????????????????????????????????????????????????????????????????


		// entity.setCardNum("MA00006");  //????????????

		// ???????????????????????????????????????????????????
		List<MemberInfo> cardList = memberInfoDao.findByHotelCodeAndCardNum2(entity.getHotelCode(),entity.getCardNum());
		if (null != cardList && 0 != cardList.size()){
			return null;
		}
		return memberInfoDao.saveAndFlush(entity);
	}

	@Override
	public void delete(String id) {
		memberInfoDao.deleteById(id);
	}

	@Override
	public MemberInfo modify(MemberInfo memberInfo) {
		return memberInfoDao.saveAndFlush(memberInfo);
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

	@Override
	public List<MemberInfo> getByCreateDate(String hotelCode, String createDate){
		List<MemberInfo> list = memberInfoDao.getByCreateDate(hotelCode, createDate);
		return list;
	}
	@Override
	public Integer getByCreateDateCount(String hotelCode, String createDate){
		Integer count = memberInfoDao.getByCreateDateCount(hotelCode, createDate);
		return count;
	}
	@Override
	public List<Map<String, Object>> countByCreateUser(String hotelCode, String createDate){
		List<Map<String, Object>> list = memberInfoDao.countByCreateUser(hotelCode, createDate);
		return list;
	}
	@Override
	public List<Map<String, Object>> rechargeReport(String hotelCode, String rechargeDate){
		List<Map<String, Object>> list = memberInfoDao.rechargeReport(hotelCode, rechargeDate);
		return list;
	}
	@Override
	public List<Map<String, Object>> integralReport(String hotelCode, String consDate){
		List<Map<String, Object>> list = memberInfoDao.integralReport(hotelCode, consDate);
		return list;
	}

	@Override
	public List<MemberInfo> byParamsList(String hotelCode, String startTime, String endTime, String birthDay) {
		Specification<MemberInfo> sf = new Specification<MemberInfo>() {
			@Override
			public Predicate toPredicate(Root<MemberInfo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> list = new ArrayList<Predicate>();
				if (hotelCode != null) {
					list.add(criteriaBuilder.equal(root.get("hotelCode"), hotelCode));
				}
				//startTime >= ????????????
				if(startTime != null){
					list.add(criteriaBuilder.greaterThanOrEqualTo(root.get("limitationDate"), LocalDate.parse(startTime)));
				}
				//endTime <= ????????????
				if(endTime != null){
					list.add(criteriaBuilder.lessThanOrEqualTo(root.get("limitationDate"), LocalDate.parse(endTime)));
				}
				//??????
				if(birthDay != null){
					list.add(criteriaBuilder.equal(root.join("customer").get("birthday"), LocalDate.parse(birthDay)));
				}
				return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
			}
		};
		List<MemberInfo> list = memberInfoDao.findAll(sf);
		return list;
	}
	@Override
	public List<MemberInfo> getParmsList(String name, String mobile,String cardNum, String idCardNum, String hotelCode,String isUsed) {
		List<MemberInfo> list = memberInfoDao.findAll(new Specification<MemberInfo>() {
			@Override
			public Predicate toPredicate(Root<MemberInfo> root, CriteriaQuery<?> query,
										 CriteriaBuilder criteriaBuilder) {
				List<Predicate> list = new ArrayList<Predicate>();
				if (hotelCode != null) {
					list.add(criteriaBuilder.equal(root.get("hotelCode"), hotelCode));
				}
				if (name != null) {//??????
					// ??????????????????????????????join???get
					list.add(criteriaBuilder.like(root.join("customer").get("name"), "%" + name + "%"));
				}
				if (mobile != null) {//?????????
					list.add(criteriaBuilder.equal(root.join("customer").get("mobile"), mobile));
				}
				if (cardNum != null) {
//					list.add(criteriaBuilder.equal(root.get("cardNum"), cardNum));
					List<Predicate> predicateListOr = new ArrayList<>();
					predicateListOr.add(criteriaBuilder.equal(root.get("cardNum"),  cardNum));
					predicateListOr.add(criteriaBuilder.equal(root.get("macNum"),  cardNum));
					list.add(criteriaBuilder.or(predicateListOr.toArray(new Predicate[predicateListOr.size()])));
				}
				if (idCardNum != null) {//??????
					// ??????????????????????????????join???get
					list.add(criteriaBuilder.equal(root.join("customer").get("idCardNum"), idCardNum));
				}

                // isUsed???????????????????????????????????????X???????????????????????????????????????I,U,R?????????????????????
				if (null != isUsed){
					if("X".equals(isUsed)){
						List<Predicate> predicateListOr = new ArrayList<>();
						predicateListOr.add(criteriaBuilder.equal(root.get("isUsed"),  "I"));
						predicateListOr.add(criteriaBuilder.equal(root.get("isUsed"),  "U"));
						predicateListOr.add(criteriaBuilder.equal(root.get("isUsed"),  "R"));
						list.add(criteriaBuilder.or(predicateListOr.toArray(new Predicate[predicateListOr.size()])));
					}else {
						list.add(criteriaBuilder.equal(root.get("isUsed"), isUsed));
					}

				}

				Predicate[] array = new Predicate[list.size()];
				return criteriaBuilder.and(list.toArray(array));
			}
		});
		for(int i=0; i<list.size(); i++){
			if(list.get(i).getCustomer() != null){
				String custId = list.get(i).getCustomer().getId();
				List<CustMarket> custMarket = custMarketDao.findByCustomerId(custId);
				if(custMarket != null && !custMarket.isEmpty()){
					list.get(i).setCustMarket(custMarket.get(0));
				}
			}
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> list(String hotelCode, String type, String isUsed, String moreParams){
		List<Map<String, Object>> list = memberInfoDao.list(hotelCode, type, isUsed, moreParams);
		return list;
	}
	@Override
	public List<MemberInfo> parmsList(String hotelCode,String type, String isUsed, String moreParams){
		List<MemberInfo> list = memberInfoDao.parmsList(hotelCode, type, isUsed, moreParams);
		return list;
	}

    @Override
    public List<MemberInfo> findByCustomer(String customerId){
	    Customer customer = new Customer();
	    customer.setId(customerId);
        List<MemberInfo> list = memberInfoDao.findByCustomer(customer);
        return list;
    }
    @Override
    public Boolean findByIdAndPassword(String id, String password){
		MemberInfo mi = memberInfoDao.findByIdAndPassword(id, password);
		if(mi != null){
			return true;
		}else{
			return false;
		}
	}

	//??????????????????????????????????????????????????????
	@Override
	public void boOverdueList(String code) {
		memberIntegralService.boOverdueList(code);
		memberRechargeService.boOverdueList(code);
	}

	@Override
	public MemberInfo findByAccountId(String hotelCode, String accountId) {
		MemberInfo memberInfo = memberInfoDao.findByHotelCodeAndAccountId(hotelCode, accountId);
		return memberInfo;
	}

	@Override
	public MemberInfo getByHotelAndMobile(String hotelCode, String mobile) {
		MemberInfo memberInfo = memberInfoDao.getByHotelAndMobile(hotelCode, mobile);
		return memberInfo;
	}

	@Override
	public List<Map<String, Object>> getSendMsgList(String hotelCode, String tDay, String sDay, String[] leavelIds){
		List<String> lids = null;
		if (leavelIds != null) {
			lids = Arrays.asList(leavelIds);
		}
		List<Map<String, Object>> list = memberInfoDao.getSendMsgList(hotelCode, tDay, sDay, lids);
		return list;
	}
}
