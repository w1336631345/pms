package com.kry.pms.model.http.request.busi;

import lombok.Data;

@Data
public class BookingItemBo {
	private String roomTypeId;
	private Integer roomCount;
	private String priceSchemeItemId;
	private Double purchasePrice;
	private String remark;
}
