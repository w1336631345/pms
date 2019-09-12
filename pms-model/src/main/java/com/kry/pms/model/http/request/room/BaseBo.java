package com.kry.pms.model.http.request.room;

import java.util.Date;

import lombok.Data;
@Data
public class BaseBo {
	protected String id;
	protected Date createDate;
	protected String createUser;
	protected Date updateDate;
	protected String updateUser;
	protected String status ="normal";
	protected String corporationCode;
	protected String hotelCode;
}


