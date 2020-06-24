package com.kry.pms.service.goods.impl;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.goods.BosBusinessSiteDao;
import com.kry.pms.dao.goods.BosGoodsInfoDao;
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
public class BosBusinessSiteServiceImpl implements BosBusinessSiteService {
	@Autowired
	BosBusinessSiteDao bosBusinessSiteDao;
	@Autowired
	BosGoodsTypeService bosGoodsTypeService;


	@Override
	public BosBusinessSite add(BosBusinessSite entity) {
		entity.setChildSize(0);
		return bosBusinessSiteDao.saveAndFlush(entity);
	}

	@Override
	public void delete(String id) {
		bosBusinessSiteDao.deleteById(id);
	}

	@Override
	@Transactional
	public HttpResponse deleteAll(String id, String deleteAll) {
		HttpResponse hr = new HttpResponse();
		if(deleteAll != null){
			bosGoodsTypeService.deleteBySiteId(id);
			bosBusinessSiteDao.deleteById(id);
		}else {
			List<BosGoodsType> list = bosGoodsTypeService.findByBosBusinessSite(findById(id));
			if(list != null){
				return hr.error("还有子商品类别，删除失败");
			}
		}
		return hr.ok();
	}

	@Override
	public BosBusinessSite modify(BosBusinessSite bosBusinessSite) {
		return bosBusinessSiteDao.saveAndFlush(bosBusinessSite);
	}

	@Override
	public BosBusinessSite findById(String id) {
		return bosBusinessSiteDao.getOne(id);
	}

	@Override
	public List<BosBusinessSite> getAllByHotelCode(String code) {
		return bosBusinessSiteDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<BosBusinessSite> listPage(PageRequest<BosBusinessSite> prq) {
		Example<BosBusinessSite> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(bosBusinessSiteDao.findAll(ex, req));
	}

	@Override
	public List<BosBusinessSite> getByHotelCode(String hotelCode) {
		return bosBusinessSiteDao.findByHotelCode(hotelCode);
	}

}
