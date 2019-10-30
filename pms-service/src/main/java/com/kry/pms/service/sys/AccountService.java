package com.kry.pms.service.sys;

import java.util.Collection;
import java.util.List;

import com.kry.pms.model.http.response.busi.AccountSummaryVo;
import com.kry.pms.model.persistence.busi.Bill;
import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.service.BaseService;

public interface AccountService extends BaseService<Account> {
	/**
	 * 如客房帐
	 * 
	 * @param rr
	 * @return
	 */
	Account billEntry(RoomRecord rr);
	
	Account billEntry(Bill bill);
	
	Collection<AccountSummaryVo> getAccountSummaryByOrderNum(String orderNum, String checkInType);

}