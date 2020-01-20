package com.kry.pms.service.sys.impl;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.sys.NationalityDao;
import com.kry.pms.model.persistence.sys.Nationality;
import com.kry.pms.service.sys.NationalityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NationalityServiceImpl implements NationalityService {
	@Autowired
	NationalityDao nationalityDao;

	@Override
	public Nationality add(Nationality entity) {
		return null;
	}

	@Override
	public void delete(String id) {

	}

	@Override
	public Nationality modify(Nationality nationality) {
		return null;
	}

	@Override
	public Nationality findById(String id) {
		return null;
	}

	@Override
	public List<Nationality> getAll() {
		return nationalityDao.findAll();
	}

}
