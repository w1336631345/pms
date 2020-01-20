package com.kry.pms.service.sys;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.sys.Nationality;
import com.kry.pms.service.BaseService;

import java.util.List;

public interface NationalityService{

    Nationality add(Nationality entity);

    void delete(String id);

    Nationality modify(Nationality nationality);

    Nationality findById(String id);

    List<Nationality> getAll();

}