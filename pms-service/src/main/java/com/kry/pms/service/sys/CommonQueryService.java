package com.kry.pms.service.sys;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface CommonQueryService {

    List<Map<String, Object>> commonQuery(String hotelCode, String id, Map<String,Object> parameterMap) throws IOException, TemplateException;

    PageResponse<Map<String, Object>> commonPageQuery(String currentHotleCode, String id, PageRequest pageRequest) throws IOException, TemplateException;
}
