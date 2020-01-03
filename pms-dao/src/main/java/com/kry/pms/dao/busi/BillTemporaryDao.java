package com.kry.pms.dao.busi;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.busi.Bill;
import com.kry.pms.model.persistence.busi.BillTemporary;
import com.kry.pms.model.persistence.sys.Account;

import java.util.List;

public interface BillTemporaryDao extends BaseDao<BillTemporary> {

	public List<BillTemporary> findByHotelCode(String hotelCode);

	public List<BillTemporary> findByAccountId(String id);

	public List<BillTemporary> findByAccountAndStatus(Account account, String status);

}
