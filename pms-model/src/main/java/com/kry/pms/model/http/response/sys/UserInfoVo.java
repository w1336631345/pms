package com.kry.pms.model.http.response.sys;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.kry.pms.model.http.response.org.DepartmentVo;
import com.kry.pms.model.http.response.org.EmployeeSummaryVo;
import com.kry.pms.model.http.response.org.HotelSummaryVo;
import com.kry.pms.model.persistence.sys.Role;

import lombok.Data;
@Data
public class UserInfoVo {
	private Role role;
	private EmployeeSummaryVo employee;
	private HotelSummaryVo hotel;
	private DepartmentVo department;
	private String nickname;
	private String username;
	private String mobile; 
	private String avatar;
	private String shiftCode;
	private String roleCode;
	private LocalDate businessDate;
	private List<Map<String,String>> functions;
}
