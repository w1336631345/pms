package com.kry.pms.service.guest.impl;

import com.kry.pms.base.Constants;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.guest.CustomerDao;
import com.kry.pms.dao.guest.MemberInfoDao;
import com.kry.pms.dao.guest.MemberIntegralDao;
import com.kry.pms.dao.quartz.QuartzSetDao;
import com.kry.pms.model.persistence.guest.MemberInfo;
import com.kry.pms.model.persistence.guest.MemberIntegral;
import com.kry.pms.model.persistence.guest.MemberIntegralType;
import com.kry.pms.model.persistence.guest.MemberIntegralTypeInfo;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.quartz.QuartzSet;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.guest.MemberInfoService;
import com.kry.pms.service.guest.MemberIntegralService;
import com.kry.pms.service.sys.AccountService;
import com.kry.pms.service.sys.BusinessSeqService;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	//????????????
	@Override
	public HttpResponse reduce(MemberIntegral entity) {
		HttpResponse hr = new HttpResponse();
		entity.setIsOverdue(0);
		String orderNo = businessSeqService.fetchNextSeqNum(entity.getHotelCode(), Constants.Key.MENBER_INTEGRAL_NO);
		entity.setOrderNo(orderNo);
		MemberInfo memberInfo = memberInfoDao.findByHotelCodeAndCardNum(entity.getHotelCode(), entity.getCardNum());
		if(memberInfo.getIntegral() == null || memberInfo.getIntegral() == 0.0){
			return hr.error("????????????");
		}
		memberInfo.setIntegral(memberInfo.getIntegral() - entity.getOutIntegral());
		memberInfoDao.saveAndFlush(memberInfo);
		hr.setData(memberIntegralDao.saveAndFlush(entity));
		return hr;
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

	//????????????????????????????????????????????????????????????????????????????????????
	@Override
	@Transactional
	public void boOverdueList(String hotelCode) {
		//?????????????????????????????????
		List<String> cardNums = memberIntegralDao.boOverdueCardNumList(hotelCode);
		for(int i=0; i<cardNums.size(); i++){
			String cardNum = cardNums.get(i);
			//????????????????????????????????????
			List<MemberIntegral> overdue = memberIntegralDao.boOverdueListByCardNum(hotelCode, cardNum);
			Double inIntegral = memberIntegralDao.sumInIntegral(hotelCode, cardNum);//?????????????????????
			for(int j=0; j<overdue.size(); j++){
				MemberIntegral mi = overdue.get(j);
//				inIntegral = inIntegral + mi.getInIntegral();
				mi.setIsOverdue(1);//??????????????????????????????
				memberIntegralDao.save(mi);
			}
			//???????????????????????????
			Double outIntegral = memberIntegralDao.sumOutIntegral(hotelCode, cardNum);//???????????????
			if(inIntegral > outIntegral){//??????????????????????????????????????????????????????????????? = ??????????????? - ???????????????????????????????????????0
				Double overIntegral_befor = memberIntegralDao.sumOverIntegral(hotelCode, cardNum);//???????????????
				Double overIntegral_now = inIntegral - outIntegral;
				if(overIntegral_now > overIntegral_befor){//???????????????????????????????????????????????????????????????????????????
					Double overIntegral = overIntegral_now - overIntegral_befor;
					MemberIntegral entity = new MemberIntegral();
					entity.setIsOverdue(1);
					entity.setOverIntegral(overIntegral);
					entity.setCardNum(cardNum);
					entity.setMacNum(overdue.get(0).getMacNum());
					entity.setHotelCode(hotelCode);
					entity.setConsDate(LocalDate.now());
					entity.setCreateDate(LocalDateTime.now());
					entity.setRemark("??????");
					entity.setInOrOut("OVER");
					String orderNo = businessSeqService.fetchNextSeqNum(entity.getHotelCode(), Constants.Key.MENBER_INTEGRAL_NO);
					entity.setOrderNo(orderNo);
					memberIntegralDao.saveAndFlush(entity);
					MemberInfo memberInfo = memberInfoDao.findByHotelCodeAndCardNum(entity.getHotelCode(), entity.getCardNum());
					memberInfo.setIntegral(memberInfo.getIntegral() - overIntegral);
					memberInfoDao.saveAndFlush(memberInfo);
				}
			}
		}
	}
	@Override
	public void auditInInteger(List<Map<String, Object>> list, LocalDate businessDate, User auditUser) {
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			String member_info_id = MapUtils.getString(map, "member_info_id");
			if(member_info_id != null){
				Double cost = MapUtils.getDouble(map, "cost");
				MemberInfo memberInfo = memberInfoDao.getOne(member_info_id);
				MemberIntegralType type = memberInfo.getMemberIntegralType();
				List<MemberIntegralTypeInfo> typeInfos = type.getMemberIntegralTypeInfos();
				for(int j=0; j<typeInfos.size(); j++){
					MemberIntegralTypeInfo typeInfo = typeInfos.get(j);
					if("rm".equals(typeInfo.getCode())){//??????
						if(cost > typeInfo.getStartStep()){
							MemberIntegral memberIntegral = new MemberIntegral();
							memberIntegral.setBusinessDate(businessDate);
							memberIntegral.setHotelCode(auditUser.getHotelCode());
							memberIntegral.setCreateDate(LocalDateTime.now());
							memberIntegral.setCreateUser(auditUser.getId());
							memberIntegral.setInOrOut("IN");
							memberIntegral.setCardNum(memberInfo.getCardNum());
							memberIntegral.setMacNum(memberInfo.getMacNum());
							memberIntegral.setMemberInfo(memberInfo);
							memberIntegral.setRoomPrice(cost);//????????????
							memberIntegral.setIntegralType(type);
							memberIntegral.setRemark("??????????????????");
							memberIntegral.setConsDate(LocalDate.now());
							memberIntegral.setLimitationDate(LocalDate.now().plusDays(type.getEffectiveDuration()));
							add(memberIntegral);

						}
					}
				}
			}
		}
	}
}
