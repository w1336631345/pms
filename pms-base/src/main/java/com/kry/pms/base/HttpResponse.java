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

	private int status = 0;
	
	private int code = 0;

	private String message = "success";

	private T data;

//	private Map<String, String> validResult;

	public HttpResponse() {
	}
	public HttpResponse(DtoResponse rep) {
		this.status = rep.getStatus();
		this.message = rep.getMessage();
		this.code = rep.getCode();
		this.success = rep.isSuccess()&&rep.getStatus()==0;
	}
	
	public HttpResponse<T> error(int status, String message) {
		HttpResponse<T> hr = new HttpResponse<T>();
		hr.setStatus(status);
		hr.setMessage(message);
		return hr;
	}
	
	public HttpResponse<T> ok(String message) {
//		HttpResponse<T> hr = new HttpResponse<T>();
		this.setMessage(message);
		return this;
	}
	public HttpResponse<T> ok() {
//		HttpResponse<T> hr = new HttpResponse<T>();
		this.setMessage(message);
		return this;
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

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
}
