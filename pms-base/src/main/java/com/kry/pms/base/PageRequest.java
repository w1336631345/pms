package com.kry.pms.base;

import java.util.ArrayList;

/**
 * 
 * @author Louis Lueng
 *
 * @param <T>
 */
public class PageRequest<T> {
	T exb;
	private int pageSize = 10;
	private int pageNum = 0;
	private ArrayList<String> orderBy;
	private boolean isAsc = true;

	public T getExb() {
		return exb;
	}

	public void setExb(T exb) {
		this.exb = exb;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNum() {
		return pageNum;
	}

	public int getSqlPageNum() {
		if (pageNum == 0) {
			return pageNum;
		}
		return pageNum - 1;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public ArrayList<String> getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(ArrayList<String> orderBy) {
		this.orderBy = orderBy;
	}

	public boolean isAsc() {
		return isAsc;
	}

	public void setAsc(boolean isAsc) {
		this.isAsc = isAsc;
	}
	public void addOrderBy(String order) {
		if(this.orderBy==null) {
			this.orderBy = new ArrayList<String>();
		}
		this.orderBy.add(order);
	}

}
