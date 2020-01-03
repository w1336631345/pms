package com.kry.pms.service.goods.impl;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.goods.ProductDao;
import com.kry.pms.dao.goods.ProductTypeDao;
import com.kry.pms.model.persistence.goods.Product;
import com.kry.pms.model.persistence.goods.ProductType;
import com.kry.pms.service.goods.ProductService;
import com.kry.pms.service.goods.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductTypeServiceImpl implements ProductTypeService {
	@Autowired
	ProductTypeDao productTypeDao;
	 
	 @Override
	public ProductType add(ProductType productType) {
		return productTypeDao.saveAndFlush(productType);
	}

	@Override
	public void delete(String id) {
		productTypeDao.deleteById(id);
	}

	@Override
	public ProductType modify(ProductType productType) {
		return productTypeDao.saveAndFlush(productType);
	}

	@Override
	public ProductType findById(String id) {
		return productTypeDao.getOne(id);
	}

	@Override
	public List<ProductType> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return productDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<ProductType> listPage(PageRequest<ProductType> prq) {
		Example<ProductType> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(productTypeDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
