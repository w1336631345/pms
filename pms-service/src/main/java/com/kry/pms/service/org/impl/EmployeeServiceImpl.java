package com.kry.pms.service.org.impl;

import java.util.List;
import java.util.Map;

import com.kry.pms.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.DtoResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.org.EmployeeDao;
import com.kry.pms.model.persistence.marketing.SalesMen;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.marketing.SalesMenService;
import com.kry.pms.service.org.EmployeeService;
import com.kry.pms.service.sys.AccountService;
import com.kry.pms.service.sys.RoleService;
import com.kry.pms.service.sys.SystemConfigService;
import com.kry.pms.service.sys.UserService;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    EmployeeDao employeeDao;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    SalesMenService salesMenService;

    @Override
    public Employee add(Employee employee) {
        return employeeDao.saveAndFlush(employee);
    }

    @Override
    public void delete(String id) {
        Employee employee = employeeDao.findById(id).get();
        if (employee != null) {
            employee.setDeleted(Constants.DELETED_TRUE);
            userService.delete(employee.getUser().getId());
        }
        employeeDao.saveAndFlush(employee);
    }

    @Override
    public Employee modify(Employee employee) {
        return employeeDao.saveAndFlush(employee);
    }

    @Override
    public Employee findById(String id) {
        return employeeDao.getOne(id);
    }

    @Override
    public List<Employee> getAllByHotelCode(String code) {
//		return null;// 默认不实现
        return employeeDao.findByHotelCode(code);
    }

    @Override
    public List<Employee> getByHotelCodeAndDelete(String code) {
        return employeeDao.findByHotelCodeAndDeleted(code, Constants.DELETED_FALSE);
    }

    @Override
    public PageResponse<Employee> listPage(PageRequest<Employee> prq) {
        Example<Employee> ex = Example.of(prq.getExb());
        org.springframework.data.domain.PageRequest req;
        if (prq.getOrderBy() != null) {
            Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
            req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
        } else {
            req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
        }
        return convent(employeeDao.findAll(ex, req));
    }

    @Override
    public Employee findByUser(User user) {
        return employeeDao.findByUserId(user.getId());
    }

    @Override
    public DtoResponse<Employee> createEmployee(Employee employee) {
        DtoResponse<Employee> rep = new DtoResponse<Employee>();
        if (userService.queryExist(employee.getHotelCode(), employee.getCode())) {
            rep.error(Constants.BusinessCode.CODE_PARAMETER_INVALID, "编码重复");
            return rep;
        } else {
            User user = new User();
            user.setUsername(employee.getCode());
            user.setNickname(employee.getName());
            user.setMobile(employee.getMobile());
            user.setHotelCode(employee.getHotelCode());
            user.setCorporationCode(employee.getCorporationCode());
            user.setCreateUser(employee.getCreateUser());
            user.setRole(employee.getRole());
            user.setPassword(MD5Utils.encrypt(employee.getCode(), employee.getHotelCode(), systemConfigService
                    .getByHotelCodeAndKey(employee.getHotelCode(), Constants.SystemConfig.CODE_DEFAULT_ACCOUNT_PASSWORD)
                    .getValue()));
            user = userService.add(user);
            employee.setUser(user);
            employee.setStatus(Constants.Status.NORMAL);
            employee = add(employee);
            if (employee.getIsSalesMen()) {
                salesMenService.createByEmployee(employee);
            }
            return rep.addData(employee);
        }
    }

    @Override
    public List<Employee> findEmployeeByDeptCode(String deptMarketingDefaultCode, String currentHotleCode) {
        return employeeDao.findByDepartmentCodeAndHotelCode(deptMarketingDefaultCode, currentHotleCode);
    }

    @Override
    public List<Map<String, Object>> getListMap(String hotelCode) {
        return employeeDao.getListMap(hotelCode);
    }

}
