package com.kry.pms.service.sys.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.sys.ShiftDao;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.sys.Shift;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.org.EmployeeService;
import com.kry.pms.service.sys.BusinessSeqService;
import com.kry.pms.service.sys.ShiftService;

@Service
public class ShiftServiceImpl implements ShiftService {
	@Autowired
	ShiftDao shiftDao;
	@Autowired
	EmployeeService employeeService;
	@Autowired
	BusinessSeqService businessSeqService;

	@Override
	public Shift add(Shift shift) {
		return shiftDao.saveAndFlush(shift);
	}

	@Override
	public void delete(String id) {
		Shift shift = shiftDao.findById(id).get();
		if (shift != null) {
			shift.setDeleted(Constants.DELETED_TRUE);
		}
		modify(shift);
	}

	@Override
	public Shift modify(Shift shift) {
		return shiftDao.saveAndFlush(shift);
	}

	@Override
	public Shift findById(String id) {
		return shiftDao.getOne(id);
	}

	@Override
	public List<Shift> getAllByHotelCode(String code) {
		return null;// 默认不实现
		// return shiftDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<Shift> listPage(PageRequest<Shift> prq) {
		Example<Shift> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(shiftDao.findAll(ex, req));
	}
	@Override
	public Shift getCurrentShift(String shiftCode, Employee employee) {
		return shiftDao.findCurrentShift(shiftCode, businessSeqService.getBuinessDate(employee.getHotelCode()),
				employee.getId());
	}

	@Override
	public Shift createOrUpdate(String shiftCode, User user) {
		Employee employee = employeeService.findByUser(user);
		LocalDate businessDate = businessSeqService.getBuinessDate(user.getHotelCode());
		Shift shift = shiftDao.findCurrentShift(shiftCode, businessDate, employee.getId());
		if (shift == null) {
			shift = new Shift();
			shift.setBusinessDate(businessDate);
			shift.setHotelCode(user.getHotelCode());
			shift.setEmployee(employee);
			shift.setShiftCode(shiftCode);
			shift.setCode(businessDate.toString() + "@" + shiftCode);
			shift = add(shift);
		}
		return shift;
	}

}
