package com.kry.pms.base;

import java.util.List;

/**
 * 
 * @author Louis Lueng
 *
 * @param <T>
 */
public class PageResponse<T> {
	private int pageSize;
	private int pageCount;
	private long total;
	private int currentPage;
	private List<T> content;

	public PageResponse() {
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

}
