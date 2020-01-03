package com.kry.pms.model.http.request.busi;

import java.util.List;

import javax.validation.Valid;

import com.kry.pms.model.http.request.room.BaseBo;

import lombok.Data;
@Data
public class CheckInBo extends BaseBo{
	
	private String hotelCode;
	
	private String bookingId;
	
	private String operation;
	
	private String type;
	@Valid
	private List<CheckInItemBo> items;
}
