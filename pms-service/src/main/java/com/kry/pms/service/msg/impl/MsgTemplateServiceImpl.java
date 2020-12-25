package com.kry.pms.service.msg.impl;

import com.kry.pms.base.Constants;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.msg.MsgAccountDao;
import com.kry.pms.dao.msg.MsgTemplateDao;
import com.kry.pms.model.persistence.msg.MsgAccount;
import com.kry.pms.model.persistence.msg.MsgTemplate;
import com.kry.pms.service.msg.MsgAccountService;
import com.kry.pms.service.msg.MsgTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MsgTemplateServiceImpl implements MsgTemplateService {
	@Autowired
	MsgTemplateDao msgTemplateDao;
	 
	@Override
	public MsgTemplate add(MsgTemplate msgTemplate) {
	 	String hotleCode = msgTemplate.getHotelCode();
		return msgTemplateDao.saveAndFlush(msgTemplate);
	}

	@Override
	public void delete(String id) {
		MsgTemplate msgTemplate = msgTemplateDao.findById(id).get();
		if (msgTemplate != null) {
			msgTemplate.setDeleted(Constants.DELETED_TRUE);
		}
		msgTemplateDao.saveAndFlush(msgTemplate);
	}
	@Override
	public void deleteTrue(String id) {
		msgTemplateDao.deleteById(id);
	}

	@Override
	public MsgTemplate modify(MsgTemplate msgTemplate) {
		return msgTemplateDao.saveAndFlush(msgTemplate);
	}

	@Override
	public MsgTemplate findById(String id) {
		return msgTemplateDao.getOne(id);
	}

	@Override
	public List<MsgTemplate> getAllByHotelCode(String code) {
//		return null;//默认不实现
		return msgTemplateDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<MsgTemplate> listPage(PageRequest<MsgTemplate> prq) {
		Example<MsgTemplate> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(msgTemplateDao.findAll(ex, req));
	}
	 
	 
	 
	 
}
