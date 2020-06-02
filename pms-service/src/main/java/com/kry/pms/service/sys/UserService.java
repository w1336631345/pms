package com.kry.pms.service.sys;

import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.BaseService;

import java.util.List;

public interface UserService extends BaseService<User> {

    User getAuditUser(String username, String password, String hotelCode);

    User findByOpenId(String openId);

    User findByUnionIdAndHotelCode(String unionId, String hotelCode);

    boolean bindWx(User user, String openId);

    List<User> findByUnionId(String unionId);

    boolean bindWxUnionId(User user, String unionId);

    boolean queryExist(String hotelCode, String code);
}