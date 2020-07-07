package com.kry.pms.service.goods.impl;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.goods.BosGoodsInfoDao;
import com.kry.pms.dao.guest.CustVehicleDao;
import com.kry.pms.model.persistence.goods.BosGoodsInfo;
import com.kry.pms.model.persistence.goods.BosGoodsType;
import com.kry.pms.model.persistence.guest.CustVehicle;
import com.kry.pms.service.goods.BosGoodsInfoService;
import com.kry.pms.service.guest.CustVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BosGoodsInfoServiceImpl implements BosGoodsInfoService {
	@Autowired
	BosGoodsInfoDao bosGoodsInfoDao;


	@Override
	public BosGoodsInfo add(BosGoodsInfo entity) {
		return bosGoodsInfoDao.saveAndFlush(entity);
	}

	@Override
	public void delete(String id) {
		bosGoodsInfoDao.deleteById(id);
	}

	@Override
	public void deleteByTypeId(String typeId) {
		bosGoodsInfoDao.deleteByTypeId(typeId);
	}

	@Override
	public BosGoodsInfo modify(BosGoodsInfo bosGoodsInfo) {
		return bosGoodsInfoDao.saveAndFlush(bosGoodsInfo);
	}

	@Override
	public BosGoodsInfo findById(String id) {
		return bosGoodsInfoDao.getOne(id);
	}

	@Override
	public List<BosGoodsInfo> getAllByHotelCode(String code) {
		return bosGoodsInfoDao.findByHotelCode(code);
	}

	@Override
	public List<BosGoodsInfo> findByBosGoodsType(BosGoodsType bosGoodsType) {
		return bosGoodsInfoDao.findByBosGoodsType(bosGoodsType);
	}

	@Override
	public List<BosGoodsInfo> findByBosGoodsTypeId(String bosGoodsTypeId) {
		return bosGoodsInfoDao.findByBosGoodsTypeId(bosGoodsTypeId);
	}

	@Override
	public List<BosGoodsInfo> findByBosSiteId(String siteId) {
		return bosGoodsInfoDao.findByBosSiteId(siteId);
	}

	@Override
	public PageResponse<BosGoodsInfo> listPage(PageRequest<BosGoodsInfo> prq) {
		Example<BosGoodsInfo> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(bosGoodsInfoDao.findAll(ex, req));
	}

	@Override
	public List<BosGoodsInfo> getByHotelCode(String hotelCode) {
		return bosGoodsInfoDao.findByHotelCode(hotelCode);
	}
}
