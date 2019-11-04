package com.kry.pms.service.sys.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.sys.BusinessSeqDao;
import com.kry.pms.model.persistence.sys.BusinessSeq;
import com.kry.pms.service.sys.BusinessSeqService;

@Service
public class  BusinessSeqServiceImpl implements  BusinessSeqService{
	@Autowired
	 BusinessSeqDao businessSeqDao;
	 
	 @Override
	public BusinessSeq add(BusinessSeq businessSeq) {
		return businessSeqDao.saveAndFlush(businessSeq);
	}

	@Override
	public void delete(String id) {
		BusinessSeq businessSeq = businessSeqDao.findById(id).get();
		if (businessSeq != null) {
			businessSeq.setDeleted(Constants.DELETED_TRUE);
		}
		modify(businessSeq);
	}

	@Override
	public BusinessSeq modify(BusinessSeq businessSeq) {
		return businessSeqDao.saveAndFlush(businessSeq);
	}
	@Override
	public LocalDate getBuinessDate(String hotelCode) {
		BusinessSeq bs = businessSeqDao.findByHotelCodeAndSeqKey(hotelCode, Constants.Key.BUSINESS_BUSINESS_DATE_SEQ_KEY);
		if(bs!=null) {
			return LocalDate.parse(bs.getCurrentSeq().toString(),DateTimeFormatter.ofPattern("yyyyMMdd"));
		}
		return null;
		
	}
	
	@Override
	public BusinessSeq findById(String id) {
		return businessSeqDao.getOne(id);
	}
	@Override
	public BusinessSeq fetchNextSeq(String hotelCode,String seqKey) {
		BusinessSeq bs = businessSeqDao.findByHotelCodeAndSeqKey(hotelCode,seqKey);
		bs.setCurrentSeq(bs.getCurrentSeq()+1);
		return modify(bs);
	}

	@Override
	public List<BusinessSeq> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return businessSeqDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<BusinessSeq> listPage(PageRequest<BusinessSeq> prq) {
		Example<BusinessSeq> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(businessSeqDao.findAll(ex, req));
	}

	@Override
	public String fetchNextSeqNum(String hotelCode, String seqKey) {
		BusinessSeq dbs = businessSeqDao.findByHotelCodeAndSeqKey(hotelCode, Constants.Key.BUSINESS_BUSINESS_DATE_SEQ_KEY);
		BusinessSeq bs = businessSeqDao.findByHotelCodeAndSeqKey(hotelCode,seqKey);
		bs.setCurrentSeq(bs.getCurrentSeq()+1);
		StringBuilder sb = new StringBuilder();
		String snf = "%0"+bs.getSnLength()+"d";
		switch (bs.getType()) {
		case Constants.Type.BUSINESS_SEQ_PDS:
			sb.append(bs.getPrefix());
			if(bs.getYearLength()==2) {				
				sb.append(dbs.getCurrentSeq().toString().substring(2));
			}else {
				sb.append(dbs.getCurrentSeq());
			}
			sb.append(String.format(snf, bs.getCurrentSeq()));
			break;
		case Constants.Type.BUSINESS_SEQ_DS:
			if(bs.getYearLength()==2) {				
				sb.append(dbs.getCurrentSeq().toString().substring(2));
			}else {
				sb.append(dbs.getCurrentSeq());
			}
			sb.append(String.format(snf, bs.getCurrentSeq()));
			break;
		case Constants.Type.BUSINESS_SEQ_PS:
			sb.append(bs.getPrefix());
			sb.append(String.format(snf, bs.getCurrentSeq()));
			break;
		default:
			break;
		}
		return sb.toString();
	}
	 
	 
	 
	 
}
