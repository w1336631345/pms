package com.kry.pms.service.guest;

import com.kry.pms.model.persistence.guest.CustInvoice;
import com.kry.pms.service.BaseService;

import java.util.List;

public interface CustInvoiceService extends BaseService<CustInvoice>{

    List<CustInvoice> getByCustomerId(String customerId);

}