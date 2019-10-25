package com.kry.pms.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;

public interface BaseService<T> {
	public default PageResponse<T> convent(Page<T> page) {
		PageResponse<T> p = new PageResponse<>();
		p.setPageSize(page.getNumberOfElements());
		p.setPageCount(page.getTotalPages());
		p.setTotal(page.getTotalElements());
		p.setCurrentPage(page.getNumber());
		p.setContent(page.getContent());
		return p;
	}

	/**
	 * 添加 entity
	 * 
	 * @param entity
	 * @return
	 */
	public T add(T entity);

	/**
	 * 通过id删除制定对象，请根据实际情况选择实现
	 * 
	 * @param id
	 */
	public void delete(String id);

	/**
	 * 修改
	 * 
	 * @param t
	 * @return
	 */
	public T modify(T t);

	/**
	 * 通过id查找
	 * 
	 * @param id
	 * @return
	 */
	public T findById(String id);

	/**
	 * 查找当前酒店的所有数据 部分实现
	 * 
	 * @return
	 */
	public List<T> getAllByHotelCode(String code);

	/**
	 * 分页查询
	 * 
	 * @param prq
	 * @return
	 */
	public PageResponse<T> listPage(PageRequest<T> prq);
}
