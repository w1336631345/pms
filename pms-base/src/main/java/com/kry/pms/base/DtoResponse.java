package com.kry.pms.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoResponse<T> {
	public boolean success = true;
	
	private int code = 0;

	private int status = 0;

	private String message = "success";

	private T data;

	public DtoResponse<T> addData(T data) {
		this.data = data;
		return this;
	}
}
