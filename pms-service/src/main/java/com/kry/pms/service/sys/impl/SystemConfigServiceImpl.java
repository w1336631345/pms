package com.kry.pms.service.sys.impl;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.sys.SystemConfigDao;
import com.kry.pms.model.persistence.sys.SystemConfig;
import com.kry.pms.service.sys.SystemConfigService;

@Service
public class SystemConfigServiceImpl implements SystemConfigService {
	@Autowired
	SystemConfigDao systemConfigDao;

	@Override
	public SystemConfig add(SystemConfig systemConfig) {
		return systemConfigDao.saveAndFlush(systemConfig);
	}

	@Override
	public void delete(String id) {
		SystemConfig systemConfig = systemConfigDao.findById(id).get();
		if (systemConfig != null) {
			systemConfig.setDeleted(Constants.DELETED_TRUE);
		}
		systemConfigDao.saveAndFlush(systemConfig);
	}

	@Override
	public SystemConfig modify(SystemConfig systemConfig) {
		return systemConfigDao.saveAndFlush(systemConfig);
	}

	@Override
	public SystemConfig findById(String id) {
		return systemConfigDao.getOne(id);
	}

	@Override
	public List<SystemConfig> getAllByHotelCode(String code) {
		return null;// 默认不实现
		// return systemConfigDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<SystemConfig> listPage(PageRequest<SystemConfig> prq) {
		Example<SystemConfig> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(systemConfigDao.findAll(ex, req));
	}

	@Override
	public SystemConfig getByHotelCodeAndKey(String hotelCode, String key) {
		// TODO Auto-generated method stub
		return systemConfigDao.findByHotelCodeAndKey(hotelCode, key);
	}

	/**
	 * 获取夜审时间
	 */
	@Override
	public LocalTime getAuditTime(String hotelCode) {
		SystemConfig sc = systemConfigDao.findByHotelCodeAndKey(hotelCode, Constants.SystemConfig.CODE_AUDIT_TIME);
		if (sc != null) {
			return LocalTime.parse(sc.getValue());
		}
		return Constants.SystemConfig.VALUE_DEFAULT_AUDIT_TIME;
	}

	/**
	 * 获取过夜临界时间
	 */
	@Override
	public LocalTime getCriticalTime(String hotelCode) {
		SystemConfig sc = systemConfigDao.findByHotelCodeAndKey(hotelCode, Constants.SystemConfig.CODE_CRITICAL_TIME);
		if (sc != null) {
			return LocalTime.parse(sc.getValue());
		}
		return Constants.SystemConfig.VALUE_DEFAULT_CRITICAL_TIME;
	}

}
