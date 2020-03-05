package com.kry.pms.service.sys.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.sys.SqlTemplateDao;
import com.kry.pms.model.persistence.sys.SqlTemplate;
import com.kry.pms.service.sys.SqlTemplateService;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Service
public class SqlTemplateServiceImpl implements SqlTemplateService {
    @Autowired
    SqlTemplateDao sqlTemplateDao;
    @Autowired
    private Configuration configuration;
    @Autowired
    EntityManager entityManager;

    @Override
    public SqlTemplate add(SqlTemplate sqlTemplate) {
        return sqlTemplateDao.saveAndFlush(sqlTemplate);
    }

    @Override
    public void delete(String id) {
        SqlTemplate sqlTemplate = sqlTemplateDao.findById(id).get();
        if (sqlTemplate != null) {
            sqlTemplate.setDeleted(Constants.DELETED_TRUE);
        }
        modify(sqlTemplate);
    }

    @Override
    public SqlTemplate modify(SqlTemplate sqlTemplate) {
        return sqlTemplateDao.saveAndFlush(sqlTemplate);
    }

    @Override
    public SqlTemplate findById(String id) {
        return sqlTemplateDao.getOne(id);
    }

    @Override
    public List<SqlTemplate> getAllByHotelCode(String code) {
        return null;//默认不实现
        //return sqlTemplateDao.findByHotelCode(code);
    }

    @Override
    public PageResponse<SqlTemplate> listPage(PageRequest<SqlTemplate> prq) {
        Example<SqlTemplate> ex = Example.of(prq.getExb());
        org.springframework.data.domain.PageRequest req;
        if (prq.getOrderBy() != null) {
            Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
            req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
        } else {
            req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
        }
        return convent(sqlTemplateDao.findAll(ex, req));
    }

    @Override
    public String getSqlTemplate(String hotelCode, String entityName, String methodName) {
        SqlTemplate st = sqlTemplateDao.findByHotelCodeAndEntityNameAndMethodName(hotelCode, entityName, methodName);
        if (st != null) {
            return st.getSql();
        }
        return null;
    }

    @Override
    public String getSql(String hotelCode, String entityName, String methodName, Object object) {
        SqlTemplate st = sqlTemplateDao.findByHotelCodeAndEntityNameAndMethodName(hotelCode, entityName, methodName);
        if (st != null) {
            String templateValue = st.getSql();
            try {
                return processTemplate(st.getEntityName(), templateValue, object);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TemplateException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public String getSql(String sql, Object object) {
        return null;
    }

    private String processTemplate(String templateName, String templateValue, Object object) throws IOException, TemplateException {
        Map<String, Object> root = new HashMap<>(4);
        root.put("data", object);
        StringWriter stringWriter = new StringWriter();
        Template template = new Template(templateName, templateValue, configuration);
        template.process(root, stringWriter);
        String data = stringWriter.toString();
        return data;
    }

    @Override
    public List<Map<String, Object>> processTemplateQuery(String hotelCode, String templateName, String templateValue, Object object) {
        String sql = getSql(hotelCode, templateName, templateValue, object);
        Query query = entityManager.createNativeQuery(sql);
        query.unwrap(NativeQueryImpl.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    @Override
    public List processTemplateQuery(String hotelCode, String templateName, String templateValue, Object object, Class resultClass) {
        String sql = getSql(hotelCode, templateName, templateValue, object);
        Query query = entityManager.createNativeQuery(sql, resultClass);
        return query.getResultList();
    }

    @Override
    public List<Map<String, Object>> processTemplateQuery(String templateName, String templateValue, Map<String, String[]> parmrs) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Template template = new Template(templateName, templateValue, configuration);
        template.process(parmrs, stringWriter);
        String data = stringWriter.toString();
        Query query = entityManager.createNativeQuery(data);
        query.unwrap(NativeQueryImpl.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    @Override
    public List<Map<String, Object>> processByCode(String hotelCode, String code, Map<String, Object> parmrs) throws IOException, TemplateException {
        SqlTemplate st = sqlTemplateDao.findByHotelCodeAndCode(hotelCode, code);
        if (st != null) {
            String templateValue = st.getSql();
            StringWriter stringWriter = new StringWriter();
            Template template = new Template(hotelCode, templateValue, configuration);
            template.process(parmrs, stringWriter);
            String data = stringWriter.toString();
            Query query = entityManager.createNativeQuery(data);
            query.unwrap(NativeQueryImpl.class)
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            return query.getResultList();
        }
        return null;
    }
}
