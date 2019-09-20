package com.kry.pms.model.http.response.sys;

import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.sys.Role;

import lombok.Data;
@Data
public class UserInfoVo {
	private Role role;
	private Employee employee;
	private String nickname;
	private String username;
	private String mobile; 
	private String avatar;
}
