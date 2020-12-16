package com.kry.pms.dao.sys;

import java.util.List;
import java.util.Map;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.sys.Function;
import org.springframework.data.jpa.repository.Query;

public interface FunctionDao extends BaseDao<Function>{

	List<Function> findByStatusAndDeleted(String normal, int deletedFalse);

	@Query(nativeQuery = true, value = " select type_ as name from t_function where deleted = 0 and `status` = 'normal' group by type_ ")
	List<Map<String, Object>> typeGroup();

	@Query(nativeQuery = true, value = " select * from t_function where deleted = 0 and `status` = 'normal' and type_ = ?1 ")
	List<Map<String, Object>> listChild(String type);

}
