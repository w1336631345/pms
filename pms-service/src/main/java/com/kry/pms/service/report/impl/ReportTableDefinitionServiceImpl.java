package com.kry.pms.service.report.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.kry.pms.base.DtoResponse;
import com.kry.pms.model.http.response.report.ReportTableDefinitionListVo;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.report.ReportTableDefinitionDao;
import com.kry.pms.model.persistence.report.ReportTableDefinition;
import com.kry.pms.service.report.ReportTableDefinitionService;

@Service
public class ReportTableDefinitionServiceImpl implements ReportTableDefinitionService {
    @Autowired
    ReportTableDefinitionDao reportTableDefinitionDao;
    @Autowired
    Configuration configuration;

    @Override
    public ReportTableDefinition add(ReportTableDefinition reportTableDefinition) {
        return reportTableDefinitionDao.saveAndFlush(reportTableDefinition);
    }

    @Override
    public void delete(String id) {
        ReportTableDefinition reportTableDefinition = reportTableDefinitionDao.findById(id).get();
        if (reportTableDefinition != null) {
            reportTableDefinition.setDeleted(Constants.DELETED_TRUE);
        }
        modify(reportTableDefinition);
    }

    @Override
    public ReportTableDefinition modify(ReportTableDefinition reportTableDefinition) {
        return reportTableDefinitionDao.saveAndFlush(reportTableDefinition);
    }

    @Override
    public ReportTableDefinition findById(String id) {
        return reportTableDefinitionDao.getOne(id);
    }

    @Override
    public List<ReportTableDefinition> getAllByHotelCode(String code) {
        return null;//默认不实现
        //return reportTableDefinitionDao.findByHotelCode(code);
    }

    @Override
    public PageResponse<ReportTableDefinition> listPage(PageRequest<ReportTableDefinition> prq) {
        Example<ReportTableDefinition> ex = Example.of(prq.getExb());
        org.springframework.data.domain.PageRequest req;
        if (prq.getOrderBy() != null) {
            Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
            req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
        } else {
            req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
        }
        return convent(reportTableDefinitionDao.findAll(ex, req));
    }


    @Override
    public DtoResponse<String> buildTemplate(String id) {
        DtoResponse<String> rep = new DtoResponse<>();
        ReportTableDefinition ptd = findById(id);
        if (ptd != null) {
            try {
                Template template = null;
                template = new Template(ptd.getHotelCode(), ptd.getBaseTemplete(), configuration);
                StringWriter stringWriter = new StringWriter();
                template.process(ptd, stringWriter);
                String data = stringWriter.toString();
                ptd.setWebTemplate(data);
                stringWriter.close();
                modify(ptd);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TemplateException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public DtoResponse<List<Map<String, Object>>> fetchData(String id, LocalDate quantityDate, String hotelCode) {
        return null;
    }

    @Override
    public List<ReportTableDefinitionListVo> groupKey(String groupKey) {
        return reportTableDefinitionDao.groupKey(groupKey);
    }
}
