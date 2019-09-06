package com.kry.pms.service.sys;

import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.service.BaseService;

public interface AccountService extends BaseService<Account>{

	Account findTopByMobileOrUsername(String username);

}