package com.kry.pms.service.msg.impl;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.msg.MsgRecordsDao;
import com.kry.pms.dao.msg.MsgTemplateDao;
import com.kry.pms.model.persistence.msg.MsgRecords;
import com.kry.pms.model.persistence.msg.MsgTemplate;
import com.kry.pms.service.msg.MsgRecordsService;
import com.kry.pms.service.msg.MsgTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MsgRecordsServiceImpl implements MsgRecordsService {
	@Autowired
	MsgRecordsDao msgRecordsDao;
	 
	@Override
	public MsgRecords add(MsgRecords msgRecords) {
		return msgRecordsDao.saveAndFlush(msgRecords);
	}

	@Override
	public void delete(String id) {
		MsgRecords msgRecords = msgRecordsDao.findById(id).get();
		if (msgRecords != null) {
			msgRecords.setDeleted(Constants.DELETED_TRUE);
		}
		msgRecordsDao.saveAndFlush(msgRecords);
	}

	@Override
	public MsgRecords modify(MsgRecords msgRecords) {
		return msgRecordsDao.saveAndFlush(msgRecords);
	}

	@Override
	public MsgRecords findById(String id) {
		return msgRecordsDao.getOne(id);
	}

	@Override
	public List<MsgRecords> getAllByHotelCode(String code) {
		return null;//默认不实现
//		return msgAccountDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<MsgRecords> listPage(PageRequest<MsgRecords> prq) {
		Example<MsgRecords> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(msgRecordsDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
