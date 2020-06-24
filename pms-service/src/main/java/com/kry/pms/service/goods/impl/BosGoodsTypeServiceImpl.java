package com.kry.pms.service.goods.impl;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.goods.BosGoodsInfoDao;
import com.kry.pms.dao.goods.BosGoodsTypeDao;
import com.kry.pms.model.persistence.goods.BosBusinessSite;
import com.kry.pms.model.persistence.goods.BosGoodsInfo;
import com.kry.pms.model.persistence.goods.BosGoodsType;
import com.kry.pms.service.goods.BosBusinessSiteService;
import com.kry.pms.service.goods.BosGoodsInfoService;
import com.kry.pms.service.goods.BosGoodsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BosGoodsTypeServiceImpl implements BosGoodsTypeService {
	@Autowired
	BosGoodsTypeDao bosGoodsTypeDao;
	@Autowired
	BosGoodsInfoService bosGoodsInfoService;
	@Autowired
	BosBusinessSiteService bosBusinessSiteService;


	@Override
	public BosGoodsType add(BosGoodsType entity) {
		entity =  bosGoodsTypeDao.saveAndFlush(entity);
		BosBusinessSite bosBusinessSite = entity.getBosBusinessSite();
		bosBusinessSite.setChildSize(bosBusinessSite.getChildSize() + 1);
		bosBusinessSiteService.modify(bosBusinessSite);
		return entity;
	}

	@Override
	public void delete(String id) {
		BosBusinessSite bosBusinessSite = findById(id).getBosBusinessSite();
		bosBusinessSite.setChildSize(bosBusinessSite.getChildSize() - 1);
		bosBusinessSiteService.modify(bosBusinessSite);
		bosGoodsTypeDao.deleteById(id);
	}

	@Override
	@Transactional
	public void deleteBySiteId(String bosBusinessSiteId) {
		BosBusinessSite bosBusinessSite = bosBusinessSiteService.findById(bosBusinessSiteId);
		List<BosGoodsType> list = bosGoodsTypeDao.findByBosBusinessSite(bosBusinessSite);
		bosBusinessSite.setChildSize(bosBusinessSite.getChildSize() - list.size());
		bosBusinessSiteService.modify(bosBusinessSite);
		bosGoodsTypeDao.deleteBySiteId(bosBusinessSiteId);
	}

	@Override
	@Transactional
	public HttpResponse deleteAll(String id, String deleteAll) {
		HttpResponse hr = new HttpResponse();
		if(deleteAll != null){
			bosGoodsInfoService.deleteByTypeId(id);
			delete(id);
		}else {
			List<BosGoodsInfo> list = bosGoodsInfoService.findByBosGoodsType(findById(id));
			if(list != null){
				return hr.error("还有子商品明细，删除失败");
			}
		}
		return hr.ok();
	}

	@Override
	public BosGoodsType modify(BosGoodsType bosGoodsType) {
		return bosGoodsTypeDao.saveAndFlush(bosGoodsType);
	}

	@Override
	public BosGoodsType findById(String id) {
		return bosGoodsTypeDao.getOne(id);
	}

	@Override
	public List<BosGoodsType> getAllByHotelCode(String code) {
		return bosGoodsTypeDao.findByHotelCode(code);
	}

	@Override
	public List<BosGoodsType> findByBosBusinessSite(BosBusinessSite bosBusinessSite) {
		return bosGoodsTypeDao.findByBosBusinessSite(bosBusinessSite);
	}
	@Override
	public List<BosGoodsType> findByBosBusinessSiteId(String bosBusinessSiteId) {
		return bosGoodsTypeDao.findByBosBusinessSiteId(bosBusinessSiteId);
	}

	@Override
	public PageResponse<BosGoodsType> listPage(PageRequest<BosGoodsType> prq) {
		Example<BosGoodsType> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(bosGoodsTypeDao.findAll(ex, req));
	}

	@Override
	public List<BosGoodsType> getByHotelCode(String hotelCode) {
		return bosGoodsTypeDao.findByHotelCode(hotelCode);
	}

}
