package com.kry.pms.model.http.response.org;

import lombok.Data;

@Data
public class HotelSummaryVo {
	private String code;
	private String name;
	private String wechatCodeUrl;
	private String address;
	private String mobile;

}
