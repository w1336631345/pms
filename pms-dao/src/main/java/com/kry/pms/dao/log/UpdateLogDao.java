package com.kry.pms.dao.log;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.log.UpdateLog;

import java.util.List;

public interface UpdateLogDao extends BaseDao<UpdateLog> {

    List<UpdateLog> findByHotelCodeAndProductType(String hotelCode, String productType);

}
