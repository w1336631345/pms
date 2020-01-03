package com.kry.pms.model.http.request.busi;

import java.util.List;

import lombok.Data;

@Data
public class BillBo {
	private String billId;
	private List<String> items;
}
