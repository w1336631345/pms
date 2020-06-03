package com.kry.pms.service.sys.impl;

import java.util.List;
import java.util.Map;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.service.goods.ProductService;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.sys.BookkeepingSetDao;
import com.kry.pms.model.persistence.sys.BookkeepingSet;
import com.kry.pms.service.sys.BookkeepingSetService;

@Service
public class  BookkeepingSetServiceImpl implements  BookkeepingSetService{
	@Autowired
	 BookkeepingSetDao bookkeepingSetDao;
	@Autowired
	ProductService productService;
	 
	 @Override
	public BookkeepingSet add(BookkeepingSet bookkeepingSet) {
		return bookkeepingSetDao.saveAndFlush(bookkeepingSet);
	}

	@Override
	public void delete(String id) {
		BookkeepingSet bookkeepingSet = bookkeepingSetDao.findById(id).get();
		if (bookkeepingSet != null) {
			bookkeepingSet.setDeleted(Constants.DELETED_TRUE);
		}
		modify(bookkeepingSet);
	}

	@Override
	public void deleteIsTrue(String id) {
		bookkeepingSetDao.deleteById(id);
	}

	@Override
	public BookkeepingSet modify(BookkeepingSet bookkeepingSet) {
		return bookkeepingSetDao.saveAndFlush(bookkeepingSet);
	}

	@Override
	public BookkeepingSet findById(String id) {
		return bookkeepingSetDao.getOne(id);
	}

	@Override
	public List<BookkeepingSet> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return bookkeepingSetDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<BookkeepingSet> listPage(PageRequest<BookkeepingSet> prq) {
		Example<BookkeepingSet> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(bookkeepingSetDao.findAll(ex, req));
	}
	 @Override
	 public BookkeepingSet isExist(String hotelCode, String accountId, String productId){
	 	BookkeepingSet bs = bookkeepingSetDao.findByHotelCodeAndAccountIdAndProductId(hotelCode, accountId, productId);
	 	return bs;
	 }

	@Override
	public HttpResponse addList(List<BookkeepingSet> list,String accountId, String hotelCode) {
		List<BookkeepingSet> oldList = bookkeepingSetDao.findByHotelCodeAndAccountId(hotelCode, accountId);
		for(int j=0; j<oldList.size(); j++){
			bookkeepingSetDao.deleteById(oldList.get(j).getId());
		}
	 	HttpResponse hr = new HttpResponse();
	 	for(int i=0; i<list.size(); i++){
	 		BookkeepingSet bs = list.get(i);
			bs.setHotelCode(hotelCode);
			bs.setDeleted(Constants.DELETED_FALSE);
			bookkeepingSetDao.saveAndFlush(bs);

		}
		return hr.ok();
	}
	@Override
	public HttpResponse addDefault(String hotelCode, String accountId) {
		HttpResponse hr = new HttpResponse();
		List<BookkeepingSet> setlist = bookkeepingSetDao.findByHotelCodeAndAccountId(hotelCode, accountId);
		if(setlist != null && !setlist.isEmpty()){
			return hr.error("已经设置，不用默认");
		}
		List<String> flist = productService.getDictCode(hotelCode);
		for(int f=0 ;f<flist.size(); f++){
			String productId = flist.get(f);
			BookkeepingSet bs = new BookkeepingSet();
			bs.setHotelCode(hotelCode);
			bs.setDeleted(Constants.DELETED_FALSE);
			bs.setAccountId(accountId);
			bs.setProductId(productId);
			bookkeepingSetDao.saveAndFlush(bs);
		}
		List<Map<String, Object>> list = productService.getPaySetListOtherStatus(hotelCode);
		for(int i=0; i<list.size(); i++){
			Map<String, Object> map = list.get(i);
			BookkeepingSet bs = new BookkeepingSet();
			bs.setHotelCode(hotelCode);
			bs.setDeleted(Constants.DELETED_FALSE);
			bs.setAccountId(accountId);
			String productId = MapUtils.getString(map, "id");
			bs.setProductId(productId);
			bookkeepingSetDao.saveAndFlush(bs);
		}
		return hr.ok();
	}

	@Override
	public List<BookkeepingSet> findSet(String hotelCode, String accountId){
	 	List<BookkeepingSet> list = bookkeepingSetDao.findByHotelCodeAndAccountId(hotelCode, accountId);
	 	return list;
	}

}
