package com.kry.pms.service.dict.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.dict.CertificateTypeDao;
import com.kry.pms.model.persistence.dict.CertificateType;
import com.kry.pms.service.dict.CertificateTypeService;

@Service
public class  CertificateTypeServiceImpl implements  CertificateTypeService{
	@Autowired
	 CertificateTypeDao certificateTypeDao;
	 
	 @Override
	public CertificateType add(CertificateType certificateType) {
		return certificateTypeDao.saveAndFlush(certificateType);
	}

	@Override
	public void delete(String id) {
		CertificateType certificateType = certificateTypeDao.findById(id).get();
		if (certificateType != null) {
			certificateType.setDeleted(Constants.DELETED_TRUE);
		}
		certificateTypeDao.saveAndFlush(certificateType);
	}

	@Override
	public CertificateType modify(CertificateType certificateType) {
		return certificateTypeDao.saveAndFlush(certificateType);
	}

	@Override
	public CertificateType findById(String id) {
		return certificateTypeDao.getOne(id);
	}

	@Override
	public List<CertificateType> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return certificateTypeDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<CertificateType> listPage(PageRequest<CertificateType> prq) {
		Example<CertificateType> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(certificateTypeDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
