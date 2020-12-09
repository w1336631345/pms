package com.kry.pms.service.marketing.impl;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.marketing.SalesMenDao;
import com.kry.pms.model.persistence.marketing.SalesMen;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.service.marketing.SalesMenService;
import com.kry.pms.service.sys.BusinessSeqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class SalesMenServiceImpl implements SalesMenService {
	@Autowired
	SalesMenDao salesMenDao;
	@Autowired
	BusinessSeqService businessSeqService;

	@Override
	public SalesMen add(SalesMen salesMen) {
		salesMen.setCode(businessSeqService.fetchNextSeqNum(salesMen.getHotelCode(),
				Constants.Key.BUSINESS_BUSINESS_SALES_MEN_SEQ));
		return salesMenDao.saveAndFlush(salesMen);
	}

	@Override
	public void delete(String id) {
		SalesMen salesMen = salesMenDao.findById(id).get();
		if (salesMen != null) {
			salesMen.setDeleted(Constants.DELETED_TRUE);
		}
		modify(salesMen);
	}

	@Override
	public SalesMen modify(SalesMen salesMen) {
		return salesMenDao.saveAndFlush(salesMen);
	}

	@Override
	public SalesMen findById(String id) {
		return salesMenDao.getOne(id);
	}

	@Override
	public List<SalesMen> getAllByHotelCode(String code) {
//		return null;// 默认不实现
	 	return salesMenDao.findByHotelCodeAndDeleted(code, Constants.DELETED_FALSE);
	}

	@Override
	public PageResponse<SalesMen> listPage(PageRequest<SalesMen> prq) {
		Example<SalesMen> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(salesMenDao.findAll(ex, req));
	}

	@Override
	public PageResponse<SalesMen> listPage2(int pageIndex, int pageSize, String type, String status,
											String name, String contactMobile, String hotelCode) {
		Pageable page = org.springframework.data.domain.PageRequest.of(pageIndex - 1, pageSize);
		Page<SalesMen> pageList = salesMenDao.findAll(new Specification<SalesMen>() {
			@Override
			public Predicate toPredicate(Root<SalesMen> root, CriteriaQuery<?> query,
										 CriteriaBuilder criteriaBuilder) {
				List<Predicate> list = new ArrayList<Predicate>();
				if (hotelCode != null) {
					list.add(criteriaBuilder.equal(root.get("hotelCode"), hotelCode));
				}
				if (type != null) {
					list.add(criteriaBuilder.equal(root.get("type"), type));
				}
				if (status != null) {
					list.add(criteriaBuilder.equal(root.get("status"), status));
				}
				List<Predicate> predicateListOr = new ArrayList<Predicate>();
				if (name != null) {
					predicateListOr.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
					predicateListOr.add(criteriaBuilder.like(root.get("mnemonicCode"), "%" + name + "%"));
					list.add(criteriaBuilder.or(predicateListOr.toArray(new Predicate[predicateListOr.size()])));
				}
				if (contactMobile != null) {
					list.add(criteriaBuilder.equal(root.get("contactMobile"), contactMobile));
				}
				Predicate[] array = new Predicate[list.size()];
				return criteriaBuilder.and(list.toArray(array));
			}
		}, page);
		return convent(pageList);
	}

	@Override
	public SalesMen createByEmployee(Employee employee) {
		SalesMen salesMen = new SalesMen();
		salesMen.setName(employee.getName());
		salesMen.setContactMobile(employee.getMobile());
		return add(salesMen);
	}

}
