package com.kry.pms.service.sys;

import com.kry.pms.model.persistence.sys.Role;
import com.kry.pms.service.BaseService;

public interface RoleService extends BaseService<Role>{

	Role modifyFunction(String id, String[] functions);

}