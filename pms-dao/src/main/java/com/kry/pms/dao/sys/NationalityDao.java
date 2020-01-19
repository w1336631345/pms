package com.kry.pms.dao.sys;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.sys.Nationality;

import java.util.List;

public interface NationalityDao extends BaseDao<Nationality>{

    List<Nationality> getById(String id);
}
