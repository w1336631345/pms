package com.kry.pms.service.report.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.kry.pms.model.persistence.report.ReportBaseValueDefinition;
import com.kry.pms.service.report.ReportBaseValueDefinitionService;
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
import com.kry.pms.dao.report.ReportBaseValueDao;
import com.kry.pms.model.persistence.report.ReportBaseValue;
import com.kry.pms.service.report.ReportBaseValueService;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Service
public class ReportBaseValueServiceImpl implements ReportBaseValueService {
    @Autowired
    ReportBaseValueDao reportBaseValueDao;
    @Autowired
    ReportBaseValueDefinitionService reportBaseValueDefinitionService;
    @Autowired
    EntityManager entityManager;

    @Override
    public ReportBaseValue add(ReportBaseValue reportBaseValue) {
        return reportBaseValueDao.saveAndFlush(reportBaseValue);
    }

    @Override
    public void delete(String id) {
        ReportBaseValue reportBaseValue = reportBaseValueDao.findById(id).get();
        if (reportBaseValue != null) {
            reportBaseValue.setDeleted(Constants.DELETED_TRUE);
        }
        modify(reportBaseValue);
    }

    @Override
    public ReportBaseValue modify(ReportBaseValue reportBaseValue) {
        return reportBaseValueDao.saveAndFlush(reportBaseValue);
    }

    @Override
    public ReportBaseValue findById(String id) {
        return reportBaseValueDao.getOne(id);
    }

    @Override
    public List<ReportBaseValue> getAllByHotelCode(String code) {
        return null;//默认不实现
        //return reportBaseValueDao.findByHotelCode(code);
    }

    @Override
    public PageResponse<ReportBaseValue> listPage(PageRequest<ReportBaseValue> prq) {
        Example<ReportBaseValue> ex = Example.of(prq.getExb());
        org.springframework.data.domain.PageRequest req;
        if (prq.getOrderBy() != null) {
            Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
            req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
        } else {
            req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
        }
        return convent(reportBaseValueDao.findAll(ex, req));
    }


    @Override
    public boolean executeAfterNightAudit(String hotelCode) {
        List<ReportBaseValueDefinition> data = reportBaseValueDefinitionService.queryAfterNightAudt(hotelCode);
        for (ReportBaseValueDefinition rbvd : data) {
            String sql = rbvd.getDataValue();
            Query query = entityManager.createNativeQuery(sql);
            query.unwrap(NativeQueryImpl.class)
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List<Map<String,Object>> vals = query.getResultList();
            if(vals!=null&&!vals.isEmpty()){
                Map<String,Object> val = vals.get(0);
                LocalDate quantityDate = LocalDate.parse(val.get("quantity_date").toString());
                if(rbvd.getType().equals("g")){

                }else{

                }
                ReportBaseValue rbv = new ReportBaseValue();

            }
        }
        return true;
    }
}
