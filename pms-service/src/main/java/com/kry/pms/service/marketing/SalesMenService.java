package com.kry.pms.service.marketing;

import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.marketing.SalesMen;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.service.BaseService;

public interface SalesMenService extends BaseService<SalesMen>{

    PageResponse<SalesMen> listPage2(int pageIndex, int pageSize, String type, String status, String name, String contactMobile, String hotelCode);

    SalesMen createByEmployee(Employee employee);

}