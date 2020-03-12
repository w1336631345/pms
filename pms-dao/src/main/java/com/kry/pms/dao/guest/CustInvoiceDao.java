package com.kry.pms.dao.guest;

import com.kry.pms.dao.BaseDao;
import com.kry.pms.model.persistence.guest.CustInvoice;

import java.util.List;

public interface CustInvoiceDao extends BaseDao<CustInvoice>{

	List<CustInvoice> findByHotelCode(String hotelCode);

	List<CustInvoice> findByCustomerId(String customerId);

}
