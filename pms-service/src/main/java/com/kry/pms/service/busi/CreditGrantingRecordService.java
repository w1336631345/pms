package com.kry.pms.service.busi;

import java.util.List;

import com.kry.pms.base.DtoResponse;
import com.kry.pms.model.persistence.busi.CreditGrantingRecord;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.service.BaseService;

public interface CreditGrantingRecordService extends BaseService<CreditGrantingRecord>{

	List<CreditGrantingRecord> queryByGrantingAccountId(String id);

	List<CreditGrantingRecord> queryByAccountId(String id);

	DtoResponse<CreditGrantingRecord> createRecord(CreditGrantingRecord creditGrantingRecord);

	boolean disableAllCreditGranting(Account account);

	DtoResponse<CreditGrantingRecord> cancle(String id);
}