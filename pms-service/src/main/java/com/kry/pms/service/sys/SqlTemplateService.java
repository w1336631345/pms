package com.kry.pms.service.sys;

import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.sys.SqlTemplate;
import com.kry.pms.service.BaseService;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface SqlTemplateService extends BaseService<SqlTemplate> {
    public String getSqlTemplate(String hotelCode, String entityName, String methodName);

    public String getSql(String hotelCode, String entityName, String methodName, Object object);

    public String getSql(String sql, Object object);

    public List processTemplateQuery(String hotelCode, String templateName, String templateValue, Object object);

    public List processTemplateQuery(String hotelCode, String templateName, String templateValue, Object object, Class resultClass);

    List<Map<String, Object>> processTemplateQuery(String templateName, String templateValue, Map<String, Object> parmrs) throws IOException, TemplateException;

    List<Map<String, Object>> processByCode(String hotelCode, String code, Map<String, Object> parmrs) throws IOException, TemplateException;

    List<Map<String, Object>> storedProcedure(String hotelCode, LocalDate businessDate, String name, Map<String, Object> params);

    List<Map<String, Object>> callProcedure(String name, Object... params);

    PageResponse<Map<String, Object>> queryForPage(String hotelCode, String code, PageRequest<Map<String, Object>> pageRequest) throws IOException, TemplateException;

    PageResponse<Map<String, Object>> queryForPage(String sql, PageRequest<Map<String, Object>> pageRequest) throws IOException, TemplateException;
}