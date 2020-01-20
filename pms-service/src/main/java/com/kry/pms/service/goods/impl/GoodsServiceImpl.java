package com.kry.pms.service.goods.impl;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.goods.GoodsDao;
import com.kry.pms.model.persistence.goods.Goods;
import com.kry.pms.service.goods.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	GoodsDao goodsDao;

	@Override
	public Goods getById(String id) {
		return null;
	}

	@Override
	public Goods add(Goods entity) {
		return goodsDao.save(entity);
	}

	@Override
	public void delete(String id) {

	}

	@Override
	public Goods modify(Goods goods) {
		return goodsDao.saveAndFlush(goods);
	}

	@Override
	public Goods findById(String id) {
		return null;
	}

	@Override
	public List<Goods> getAllByHotelCode(String code) {
		List<Goods> list = goodsDao.getByHotelCode(code);
		return list;
	}

	@Override
	public PageResponse<Goods> listPage(PageRequest<Goods> prq) {
		return null;
	}
}
