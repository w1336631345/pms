package com.kry.pms.service.goods.impl;

import java.util.List;

import com.kry.pms.dao.goods.ProductTypeDao;
import com.kry.pms.model.persistence.goods.ProductType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.goods.ProductCategoryDao;
import com.kry.pms.model.persistence.goods.ProductCategory;
import com.kry.pms.service.goods.ProductCategoryService;

@Service
public class  ProductCategoryServiceImpl implements  ProductCategoryService{
	@Autowired
	 ProductCategoryDao productCategoryDao;
	@Autowired
	ProductTypeDao productTypeDao;
	 
	 @Override
	public ProductCategory add(ProductCategory productCategory) {
		return productCategoryDao.saveAndFlush(productCategory);
	}

	@Override
	public void delete(String id) {
		ProductCategory productCategory = productCategoryDao.findById(id).get();
		if (productCategory != null) {
			productCategory.setDeleted(Constants.DELETED_TRUE);
		}
		productCategoryDao.saveAndFlush(productCategory);
	}

	@Override
	public ProductCategory modify(ProductCategory productCategory) {
		return productCategoryDao.saveAndFlush(productCategory);
	}

	@Override
	public ProductCategory findById(String id) {
		return productCategoryDao.getOne(id);
	}

	@Override
	public List<ProductCategory> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return productCategoryDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<ProductCategory> listPage(PageRequest<ProductCategory> prq) {
		Example<ProductCategory> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(productCategoryDao.findAll(ex, req));
	}

	@Override
	public List<ProductCategory> treeAndType(String hotelCode){
	 	List<ProductCategory> list = productCategoryDao.findByHotelCodeAndDeleted(hotelCode, Constants.DELETED_FALSE);
	 	for(int i=0; i<list.size(); i++){
	 		ProductCategory pc = list.get(i);
	 		List<ProductType> productTypeList = productTypeDao.findByCategoryId(pc.getId());
	 		pc.setProductTypeList(productTypeList);
		}
	 	return list;
	}
	 
	 
	 
}
