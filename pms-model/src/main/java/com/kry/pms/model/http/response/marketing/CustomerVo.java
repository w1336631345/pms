package com.kry.pms.model.http.response.marketing;

import org.springframework.beans.BeanUtils;

import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.model.persistence.guest.GuestInfo;

import lombok.Data;
@Data
public class CustomerVo {
	private String name;
	private String mobile;
//	private GuestInfo guestInfo;
	private Integer checkInCount;
	private String currentStatus;
	private String nationality;//国籍
	private String nation;//民族

	public static CustomerVo convert(Customer c) {
		CustomerVo cv = new CustomerVo();
		BeanUtils.copyProperties(c, cv);
		return cv;
	}

}
