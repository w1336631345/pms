package com.kry.pms.model.http.request.busi;

import java.util.List;

import com.kry.pms.model.http.request.room.BaseBo;

import lombok.Data;
@Data
public class BillSettleBo extends BaseBo {
	private List<BillBo> bills;
	private String type;
	private String payChannel;
	private String roomId;
	private Double tatal;
}
