package com.kry.pms.service.sys;

import com.kry.pms.model.persistence.sys.SqlTemplate;
import com.kry.pms.service.BaseService;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface SqlTemplateService extends BaseService<SqlTemplate> {
    public String getSqlTemplate(String hotelCode, String entityName, String methodName);

    public String getSql(String hotelCode, String entityName, String methodName, Object object);

    public String getSql(String sql, Object object);

    public List processTemplateQuery(String hotelCode, String templateName, String templateValue, Object object);

    public List processTemplateQuery(String hotelCode, String templateName, String templateValue, Object object, Class resultClass);

    List<Map<String, Object>> processTemplateQuery(String templateName, String templateValue, Map<String, String[]> parmrs) throws IOException, TemplateException;

    List<Map<String, Object>> processByCode(String hotelCode, String code, Map<String, Object> parmrs) throws IOException, TemplateException;
}