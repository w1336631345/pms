package com.kry.pms.service.goods.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.goods.ProductDao;
import com.kry.pms.model.persistence.goods.Product;
import com.kry.pms.service.goods.ProductService;

@Service
public class  ProductServiceImpl implements  ProductService{
	@Autowired
	 ProductDao productDao;
	 
	 @Override
	public Product add(Product product) {
	 	Product exsitProduct = productDao.findByHotelCodeAndCode(product.getHotelCode(),product.getCode());
	 	if(exsitProduct==null){
			return productDao.saveAndFlush(product);
		}
	 	return null;
	}

	@Override
	public void delete(String id) {
		Product product = productDao.findById(id).get();
		if (product != null) {
			product.setDeleted(Constants.DELETED_TRUE);
		}
		productDao.saveAndFlush(product);
	}

	@Override
	public Product modify(Product product) {
		return productDao.saveAndFlush(product);
	}

	@Override
	public Product findById(String id) {
		return productDao.getOne(id);
	}
	@Override
	public Product getById(String id) {
		return productDao.getById(id);
	}

	@Override
	public Product findHalfRoomFee(String hotelCode) {
		return productDao.findByHotelCodeAndCode(hotelCode,Constants.Code.HALF_DAY_ROOM_FEE);
	}

	@Override
	public Product findFullRoomFee(String hotelCode) {
		return productDao.findByHotelCodeAndCode(hotelCode,Constants.Code.FULL_DAY_ROOM_FEE);
	}

	@Override
	public List<Product> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return productDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<Product> listPage(PageRequest<Product> prq) {
		Example<Product> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(productDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
