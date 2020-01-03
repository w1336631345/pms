package com.kry.pms.service.sys;

import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.BaseService;

public interface UserService extends BaseService<User>{

    User getAuditUser(String username, String password, String hotelCode);
}