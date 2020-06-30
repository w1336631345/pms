package com.kry.pms.service.sys.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kry.pms.util.SqlUtil;
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
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

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
//        SqlTemplate st = sqlTemplateDao.findByHotelCodeAndEntityNameAndMethodName(hotelCode, entityName, methodName);
        SqlTemplate st = sqlTemplateDao.findByEntityNameAndMethodName(entityName, methodName);
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
        stringWriter.close();
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
    public List<Map<String, Object>> processTemplateQuery(String templateName, String templateValue, Map<String, Object> parmrs) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Template template = new Template(templateName, templateValue, configuration);
        template.process(parmrs, stringWriter);
        String data = stringWriter.toString();
        Query query = entityManager.createNativeQuery(data);
        query.unwrap(NativeQueryImpl.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        stringWriter.close();
        return query.getResultList();
    }

    @Override
    public List<Map<String, Object>> processByCode(String hotelCode, String code, Map<String, Object> parmrs) throws IOException, TemplateException {
//        SqlTemplate st = sqlTemplateDao.findByHotelCodeAndCode(hotelCode, code);
        SqlTemplate st = sqlTemplateDao.findByCode(code);
        if (st != null) {
            String templateValue = st.getSql();
            parmrs.put("hotelCode", hotelCode);
            StringWriter stringWriter = new StringWriter();
            Template template = new Template(hotelCode, templateValue, configuration);
            template.process(parmrs, stringWriter);
            String data = stringWriter.toString();
            stringWriter.close();
            Query query = entityManager.createNativeQuery(data);
            query.unwrap(NativeQueryImpl.class)
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            return query.getResultList();
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> storedProcedure(String hotelCode, LocalDate businessDate, String name, Map<String, Object> params) {
        StringWriter stringWriter = new StringWriter();
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery(name);
        query.registerStoredProcedureParameter("hotelCode", String.class, ParameterMode.IN);
        query.setParameter("hotelCode", hotelCode);
        query.registerStoredProcedureParameter("businessDate", LocalDate.class, ParameterMode.IN);
        query.setParameter("businessDate", businessDate);
        for (Map.Entry<String, Object> param : params.entrySet()) {
            String mapKey = param.getKey();
            Object mapValue = param.getValue();
            if (mapValue instanceof Integer) {
                int value = ((Integer) mapValue).intValue();
                query.registerStoredProcedureParameter(mapKey, Integer.class, ParameterMode.IN);
                query.setParameter(mapKey, value);
            } else if (mapValue instanceof String) {
                String s = (String) mapValue;
                query.registerStoredProcedureParameter(mapKey, String.class, ParameterMode.IN);
                query.setParameter(mapKey, s);
            } else if (mapValue instanceof Double) {
                double d = ((Double) mapValue).doubleValue();
                query.registerStoredProcedureParameter(mapKey, Double.class, ParameterMode.IN);
                query.setParameter(mapKey, d);
            } else if (mapValue instanceof Float) {
                float f = ((Float) mapValue).floatValue();
                query.registerStoredProcedureParameter(mapKey, Float.class, ParameterMode.IN);
                query.setParameter(mapKey, f);
            } else if (mapValue instanceof Long) {
                long l = ((Long) mapValue).longValue();
                query.registerStoredProcedureParameter(mapKey, Long.class, ParameterMode.IN);
                query.setParameter(mapKey, l);
            } else if (mapValue instanceof Boolean) {
                boolean b = ((Boolean) mapValue).booleanValue();
                query.registerStoredProcedureParameter(mapKey, Boolean.class, ParameterMode.IN);
                query.setParameter(mapKey, b);
            } else if (mapValue instanceof LocalDate) {
                LocalDate d = (LocalDate) mapValue;
                query.registerStoredProcedureParameter(mapKey, LocalDate.class, ParameterMode.IN);
                query.setParameter(mapKey, d);
            } else if (mapValue instanceof LocalDateTime) {
                LocalDateTime dt = (LocalDateTime) mapValue;
                query.registerStoredProcedureParameter(mapKey, LocalDateTime.class, ParameterMode.IN);
                query.setParameter(mapKey, dt);
            }
        }
        List<Map<String, Object>> list = new ArrayList<>();
//        list = query.getResultList();
        query.execute();
        entityManager.close();
        return list;
    }
    @Override
    public List<Map<String, Object>> callProcedure(String name, Object[] params) {
        StringWriter stringWriter = new StringWriter();
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery(name);
        int i = 0;
        for (Object param : params) {
            query.registerStoredProcedureParameter(i, param.getClass(), ParameterMode.IN);
            query.setParameter(i, param);
            i++;
        }
        List<Map<String, Object>> list = new ArrayList<>();
        query.execute();
        entityManager.close();
        return list;
    }


    @Override
    public PageResponse<Map<String, Object>> queryForPage(String hotelCode, String code, PageRequest<Map<String, Object>> pageRequest) throws IOException, TemplateException {
//        SqlTemplate st = sqlTemplateDao.findByHotelCodeAndCode(hotelCode, code);
        SqlTemplate st = sqlTemplateDao.findByCode(code);
        pageRequest.getExb().put("hotelCode", hotelCode);
        if (st != null) {
            return queryForPage(st.getSql(), pageRequest);
        }
        return null;
    }

    @Override
    public PageResponse<Map<String, Object>> queryForPage(String sql, PageRequest<Map<String, Object>> pageRequest) throws IOException, TemplateException {
        PageResponse<Map<String, Object>> rep = new PageResponse<>();
        if (sql != null) {
            String templateValue = sql;
            StringWriter stringWriter = new StringWriter();
            Template template = new Template("temp", templateValue, configuration);
            template.process(pageRequest.getExb(), stringWriter);
            String data = stringWriter.toString();
            stringWriter.close();
            String countSql = SqlUtil.getCountSQL(data);
            Query countQuery = entityManager.createNativeQuery(countSql);
            int count = Integer.parseInt(countQuery.getSingleResult().toString());
            Query query = entityManager.createNativeQuery(data);
            query.unwrap(NativeQueryImpl.class)
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            rep.setTotal(count);
            rep.setPageCount(count / pageRequest.getPageSize() + 1);
            rep.setCurrentPage(pageRequest.getPageNum() + 1);
            rep.setPageSize(pageRequest.getPageSize());
            query.setFirstResult(pageRequest.getPageNum() * pageRequest.getPageSize());
            query.setMaxResults(pageRequest.getPageSize());
            rep.setContent(query.getResultList());
        }
        return rep;
    }

}
