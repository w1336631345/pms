package com.kry.pms.base;

import java.util.Map;

/**
 * 
 * @author Louis Lueng
 *
 * @param <T>
 */

public class HttpResponse<T> {

	public boolean success = true;

	private int statusCode = 0;

	private String message = "success";

	private T data;

//	private Map<String, String> validResult;

	public HttpResponse() {
	}

	public HttpResponse<T> addData(T data) {
		this.data = data;
		return this;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}



}
