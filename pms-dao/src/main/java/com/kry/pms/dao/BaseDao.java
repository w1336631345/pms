package com.kry.pms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 
 * @author Louis Lueng
 *
 * @param <T>
 */
@NoRepositoryBean
public interface BaseDao<T>
		extends JpaRepository<T, String>, JpaSpecificationExecutor<T>{

}
