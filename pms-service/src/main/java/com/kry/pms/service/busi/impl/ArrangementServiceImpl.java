package com.kry.pms.service.busi.impl;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.busi.ArrangementDao;
import com.kry.pms.model.persistence.busi.Arrangement;
import com.kry.pms.service.busi.ArrangementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArrangementServiceImpl implements ArrangementService {
	@Autowired
	ArrangementDao arrangementDao;


	@Override
	public Arrangement add(Arrangement entity) {
		return null;
	}

	@Override
	public void delete(String id) {

	}

	@Override
	public Arrangement modify(Arrangement arrangement) {
		return null;
	}

	@Override
	public Arrangement findById(String id) {
		return null;
	}

	@Override
	public List<Arrangement> getAllByHotelCode(String code) {
		List<Arrangement> list = arrangementDao.findByHotelCode(code);
		return list;
	}

	@Override
	public PageResponse<Arrangement> listPage(PageRequest<Arrangement> prq) {
		return null;
	}
}
