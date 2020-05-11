package com.kry.pms.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
	private List<String> orderBy;
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

	public List<String> getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(List<String> orderBy) {
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
	public static PageRequest<Map<String,Object>> parseFormMap(Map<String,Object> data){
		PageRequest<Map<String, Object>> pr = new PageRequest<>();
		if (data.containsKey(Constants.KEY_PAGE_SIZE)) {
			pr.setPageSize(Integer.valueOf(data.get(Constants.KEY_PAGE_SIZE).toString()));
		}
		if (data.containsKey(Constants.KEY_PAGE_NUM)) {
			pr.setPageNum(Integer.valueOf(data.get(Constants.KEY_PAGE_NUM).toString()) - 1);
		}
//		if (data.containsKey(Constants.KEY_ORDER)) {
//			pr.setOrderBy(Arrays.asList(data.get(Constants.KEY_ORDER)));
//		}
		if (data.containsKey(Constants.KEY_SHORT_ASC)) {
			pr.setAsc(Boolean.valueOf(data.get(Constants.KEY_SHORT_ASC).toString()));
		}
		pr.setExb(data);
		return pr;
	}
}
