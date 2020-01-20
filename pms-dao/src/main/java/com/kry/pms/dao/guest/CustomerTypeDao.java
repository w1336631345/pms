package com.kry.pms.dao.guest;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.guest.CustomerType;

import java.util.List;

public interface CustomerTypeDao extends BaseDao<CustomerType>{

    List<CustomerType> findByHotelCode(String hotelCode);

}
