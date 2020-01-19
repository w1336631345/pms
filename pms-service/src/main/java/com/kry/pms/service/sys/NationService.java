package com.kry.pms.service.sys;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.sys.Nation;
import com.kry.pms.model.persistence.sys.Nationality;
import com.kry.pms.service.BaseService;

import java.util.List;

public interface NationService{

    Nation add(Nation entity);

    void delete(String id);

    Nation modify(Nation nation);

    Nation findById(String id);

    PageResponse<Nation> listPage(PageRequest<Nation> prq);
}