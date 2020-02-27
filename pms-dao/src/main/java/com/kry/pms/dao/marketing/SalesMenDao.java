package com.kry.pms.dao.marketing;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.marketing.SalesMen;

import java.util.List;

public interface SalesMenDao extends BaseDao<SalesMen>{

    List<SalesMen> findByHotelCodeAndDeleted(String HotelCode, int deleted);
}
