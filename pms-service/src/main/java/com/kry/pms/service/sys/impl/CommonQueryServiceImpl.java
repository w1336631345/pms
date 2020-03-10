package com.kry.pms.service.sys.impl;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.service.sys.CommonQueryService;
import com.kry.pms.service.sys.SqlTemplateService;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class CommonQueryServiceImpl implements CommonQueryService {
    @Autowired
    SqlTemplateService sqlTemplateService;

    @Override
    public List<Map<String, Object>> commonQuery(String hotelCode, String code, Map<String, Object> parameterMap) throws IOException, TemplateException {
        return sqlTemplateService.processByCode(hotelCode, code, parameterMap);
    }

    @Override
    public PageResponse<Map<String, Object>> commonPageQuery(String hotelCode, String code, PageRequest pageRequest) throws IOException, TemplateException {
        return sqlTemplateService.queryForPage(hotelCode, code, pageRequest);
    }
}
