package com.kry.pms.model.http.request.busi;

import javax.validation.constraints.NotBlank;

import lombok.Data;
@Data
public class BookOperationBo {
	@NotBlank
	private String bookId;
	private String reasonId;
	private String remark;
	@NotBlank // 支持的操作为：cancle verify_pass verify_refuse
	private String operation;
}
