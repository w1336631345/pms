package com.kry.pms.service.guest.impl;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.dict.DictDataDao;
import com.kry.pms.dao.goods.ProductDao;
import com.kry.pms.dao.guest.CustomerDao;
import com.kry.pms.dao.guest.MemberIntegralDao;
import com.kry.pms.dao.guest.MemberIntegralTypeDao;
import com.kry.pms.dao.guest.MemberIntegralTypeInfoDao;
import com.kry.pms.model.persistence.goods.Product;
import com.kry.pms.model.persistence.guest.MemberIntegral;
import com.kry.pms.model.persistence.guest.MemberIntegralType;
import com.kry.pms.model.persistence.guest.MemberIntegralTypeInfo;
import com.kry.pms.service.goods.ProductService;
import com.kry.pms.service.guest.MemberIntegralService;
import com.kry.pms.service.guest.MemberIntegralTypeInfoService;
import com.kry.pms.service.guest.MemberIntegralTypeService;
import com.kry.pms.service.sys.AccountService;
import com.kry.pms.service.sys.BusinessSeqService;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MemberIntegralTypeServiceImpl implements MemberIntegralTypeService {
	@Autowired
	MemberIntegralTypeDao memberIntegralTypeDao;
	@Autowired
	DictDataDao dictDataDao;
	@Autowired
	MemberIntegralTypeInfoDao memberIntegralTypeInfoDao;

	@Override
	public MemberIntegralType add(MemberIntegralType entity) {
		return memberIntegralTypeDao.saveAndFlush(entity);
	}

	@Override
	public MemberIntegralType addAll(MemberIntegralType entity) {
		List<Map<String, Object>> list = dictDataDao.getArchivesTypeAndHotelCode(entity.getHotelCode(),"AchievementType");
		List<MemberIntegralTypeInfo> memberIntegralTypeInfos = new ArrayList<>();
		for(int i=0; i<list.size(); i++){
			Map map = list.get(i);
			String code = MapUtils.getString(map, "code");
			String description = MapUtils.getString(map, "description");
			MemberIntegralTypeInfo miti = new MemberIntegralTypeInfo();
			miti.setCode(code);
			miti.setAchievement(description);
			miti.setStartStep(1);
			miti.setSetpLength(1);
			miti.setProportion(1.0);
			miti.setHotelCode(entity.getHotelCode());
			memberIntegralTypeInfoDao.saveAndFlush(miti);
			memberIntegralTypeInfos.add(miti);
		}
		entity.setMemberIntegralTypeInfos(memberIntegralTypeInfos);
		return memberIntegralTypeDao.saveAndFlush(entity);
	}

	@Override
	public void delete(String id) {
		memberIntegralTypeDao.deleteById(id);
	}

	@Override
	public MemberIntegralType modify(MemberIntegralType memberIntegralType) {
		return memberIntegralTypeDao.saveAndFlush(memberIntegralType);
	}

	@Override
	public MemberIntegralType findById(String id) {
		return memberIntegralTypeDao.getOne(id);
	}

	@Override
	public List<MemberIntegralType> getAllByHotelCode(String code) {
		return memberIntegralTypeDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<MemberIntegralType> listPage(PageRequest<MemberIntegralType> prq) {
		Example<MemberIntegralType> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(memberIntegralTypeDao.findAll(ex, req));
	}
}
