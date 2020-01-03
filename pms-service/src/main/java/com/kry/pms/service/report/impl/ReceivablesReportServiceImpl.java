package com.kry.pms.service.report.impl;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.report.ReceivablesReportDao;
import com.kry.pms.model.persistence.report.ReceivablesReport;
import com.kry.pms.model.persistence.report.ReportBase;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.report.ReceivablesReportService;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ReceivablesReportServiceImpl implements ReceivablesReportService {

    @Autowired
    ReceivablesReportDao receivablesReportDao;

    @Override
    public List<ReceivablesReport> findByHotelCodeAndBusinessDate(String hotelCode, LocalDate businessDate){
        List<ReceivablesReport> list = receivablesReportDao.findByHotelCodeAndBusinessDate(hotelCode, businessDate);
        return list;
    }

    @Override
    public ReceivablesReport add(ReceivablesReport entity) {
        ReceivablesReport receivablesReport = receivablesReportDao.save(entity);
        return receivablesReport;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public ReceivablesReport modify(ReceivablesReport receivablesReport) {
        return null;
    }

    @Override
    public ReceivablesReport findById(String id) {
        return null;
    }

    @Override
    public List<ReceivablesReport> getAllByHotelCode(String code) {
        return null;
    }

    @Override
    public PageResponse<ReceivablesReport> listPage(PageRequest<ReceivablesReport> prq) {
        return null;
    }

    @Override
    public HttpResponse totalByTypeName(String hotelCode, String businessDate){
        HttpResponse hr = new HttpResponse();
        List<ReceivablesReport> rrs = receivablesReportDao.findByHotelCodeAndBusinessDate(hotelCode, LocalDate.parse(businessDate));
        if(rrs != null && !rrs.isEmpty()){
            return hr.ok("今日收款汇总已经生成");
        }
        List<Map<String, Object>> listAll = receivablesReportDao.totalByTypeNameAll(hotelCode, businessDate);
        List<Map<String, Object>> list = receivablesReportDao.totalByTypeName(hotelCode, businessDate);
        LocalDateTime ldt = LocalDateTime.now();
        for(int i=0; i<listAll.size(); i++){
            Map<String, Object> mapTotal = listAll.get(i);
            ReceivablesReport total = new ReceivablesReport();
            total.setAuditDate(ldt);
            total.setBusinessDate(LocalDate.parse(businessDate));
            total.setHotelCode(hotelCode);
//            total.setUsername(user.getUsername());

            total.setAlipay(MapUtils.getString(mapTotal,"alipay"));
            total.setCash(MapUtils.getString(mapTotal,"cash"));
            total.setBankCard(MapUtils.getString(mapTotal,"bankCard"));
            total.setOtherPay(MapUtils.getString(mapTotal,"otherPay"));
            total.setWechatPay(MapUtils.getString(mapTotal,"wechatPay"));
            total.setDempId(MapUtils.getString(mapTotal,"deptId"));
            total.setDemp(MapUtils.getString(mapTotal,"dept"));
            add(total);
        }
        for(int j=0; j<list.size(); j++){
            Map<String, Object> map = list.get(j);
            ReceivablesReport rr = new ReceivablesReport();
            rr.setAuditDate(ldt);
            rr.setBusinessDate(LocalDate.parse(businessDate));
            rr.setHotelCode(hotelCode);
//            rr.setUsername(user.getUsername());

            rr.setAlipay(MapUtils.getString(map,"alipay"));
            rr.setCash(MapUtils.getString(map,"cash"));
            rr.setBankCard(MapUtils.getString(map,"bankCard"));
            rr.setOtherPay(MapUtils.getString(map,"otherPay"));
            rr.setWechatPay(MapUtils.getString(map,"wechatPay"));
            rr.setDempId(MapUtils.getString(map,"deptId"));
            rr.setGroupType(MapUtils.getString(map,"shift_code"));
            rr.setCashier(MapUtils.getString(map,"name"));
            add(rr);
        }
        return hr.ok();
    }

}
