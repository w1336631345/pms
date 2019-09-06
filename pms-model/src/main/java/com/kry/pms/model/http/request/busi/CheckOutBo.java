package com.kry.pms.model.http.request.busi;

import com.kry.pms.model.http.request.room.BaseBo;

import lombok.Data;
@Data
public class CheckOutBo extends BaseBo{
	
	private String hotelCode;
	private String type;
	private String payChannel;
	private String roomId;
	private Double tatal;
	
}
