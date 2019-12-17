package com.kry.pms.service.busi;

import com.kry.pms.base.DtoResponse;
import com.kry.pms.model.http.request.busi.BillCheckBo;
import com.kry.pms.model.persistence.busi.SettleAccountRecord;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.service.BaseService;

public interface SettleAccountRecordService extends BaseService<SettleAccountRecord>{

	SettleAccountRecord create(BillCheckBo billCheckBo, Account account);

	DtoResponse<String> cancle(String id);
	
	

}