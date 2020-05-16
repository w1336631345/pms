package com.kry.pms.dao.sys;

import java.util.List;
import java.util.Map;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.sys.User;

public interface UserDao extends BaseDao<User>{

	List<User> getByUsernameAndPassword(String username, String password);

    List<User> findByHotelCode(String code);

    User findByUsernameAndPasswordAndHotelCode(String username, String password, String hotelCode);

    User findByOpenIdAndStatus(String openId, String normal);

    User findByUnionIdAndHotelCode(String unionId, String hotelCode);
}
