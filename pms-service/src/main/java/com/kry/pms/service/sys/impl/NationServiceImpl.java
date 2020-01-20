package com.kry.pms.service.sys.impl;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.sys.NationDao;
import com.kry.pms.dao.sys.RoleDao;
import com.kry.pms.model.persistence.sys.Nation;
import com.kry.pms.model.persistence.sys.Nationality;
import com.kry.pms.model.persistence.sys.Role;
import com.kry.pms.service.sys.NationService;
import com.kry.pms.service.sys.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NationServiceImpl implements NationService {
	@Autowired
	NationDao nationDao;

	@Override
	public Nation add(Nation entity) {
		return null;
	}

	@Override
	public void delete(String id) {

	}

	@Override
	public Nation modify(Nation nation) {
		return null;
	}

	@Override
	public Nation findById(String id) {
		return null;
	}

	@Override
	public PageResponse<Nation> listPage(PageRequest<Nation> prq) {
		return null;
	}
}
