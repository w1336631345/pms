package com.kry.pms.model.http.request.busi;

import com.kry.pms.model.http.request.room.BaseBo;

import lombok.Data;

@Data
public class BillItemBo extends BaseBo {
	private String type;
	private String roomRecordId;
	private String productId;
	private String discountSchemeId;
	private Double total;
	private Double quantity;
	private Double price;
	private String statusPayment;
}
