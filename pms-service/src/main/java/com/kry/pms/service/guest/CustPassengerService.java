package com.kry.pms.service.guest;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.guest.CustPassenger;
import com.kry.pms.service.BaseService;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface CustPassengerService{

    public default PageResponse<CustPassenger> convent(Page<CustPassenger> page) {
        PageResponse<CustPassenger> p = new PageResponse<>();
        p.setPageSize(page.getNumberOfElements());
        p.setPageCount(page.getTotalPages());
        p.setTotal(page.getTotalElements());
        p.setCurrentPage(page.getNumber());
        p.setContent(page.getContent());
        return p;
    }

    CustPassenger add(CustPassenger entity);

    void delete(String id);

    CustPassenger modify(CustPassenger custPassenger);

    CustPassenger findById(String id);

    List<CustPassenger> getAllByHotelCode(String code);

    PageResponse<CustPassenger> listPage(PageRequest<CustPassenger> prq);

    List<CustPassenger> getByCustomerId(String customerId);

    List<Map<String, Object>> getPassengerList(String customerId);
}