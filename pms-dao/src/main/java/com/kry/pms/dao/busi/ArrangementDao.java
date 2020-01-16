package com.kry.pms.dao.busi;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.busi.Arrangement;

import java.util.List;

public interface ArrangementDao extends BaseDao<Arrangement>{

    List<Arrangement> findByHotelCode(String hotelCode);
}
