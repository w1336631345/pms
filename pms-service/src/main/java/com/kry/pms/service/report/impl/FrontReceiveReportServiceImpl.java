package com.kry.pms.service.report.impl;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.report.FrontEntryReportDao;
import com.kry.pms.dao.report.FrontReceiveReportDao;
import com.kry.pms.model.persistence.report.FrontEntryReport;
import com.kry.pms.model.persistence.report.FrontReceiveReport;
import com.kry.pms.service.report.FrontEntryReportService;
import com.kry.pms.service.report.FrontReceiveReportService;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FrontReceiveReportServiceImpl implements FrontReceiveReportService {

    @Autowired
    FrontReceiveReportDao frontReceiveReportDao;

    @Override
    public FrontReceiveReport add(FrontReceiveReport entity) {
        return frontReceiveReportDao.save(entity);
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public FrontReceiveReport modify(FrontReceiveReport frontReceiveReport) {
        return null;
    }

    @Override
    public FrontReceiveReport findById(String id) {
        return null;
    }

    @Override
    public List<FrontReceiveReport> getAllByHotelCode(String code) {
        return null;
    }

    @Override
    public PageResponse<FrontReceiveReport> listPage(PageRequest<FrontReceiveReport> prq) {
        return null;
    }

    @Override
    public HttpResponse receiveList(String hotelCode, String businessDate){
        HttpResponse hr = new HttpResponse();
        List<FrontReceiveReport> listre = frontReceiveReportDao.findByHotelCodeAndBusinessDate(hotelCode, LocalDate.parse(businessDate));
        if(listre != null && !listre.isEmpty()){
            return hr.ok("今日收款报表已经生成");
        }
        List<Map<String, Object>> list = frontReceiveReportDao.getList(hotelCode, businessDate);
        for(int i=0; i<list.size(); i++){
            FrontReceiveReport frr = new FrontReceiveReport();
            Map<String, Object> map = list.get(i);
            frr.setPay(MapUtils.getString(map, "pay"));
            frr.setEmpId(MapUtils.getString(map, "empId"));
            frr.setEmpName(MapUtils.getString(map, "empName"));
            frr.setShiftCode(MapUtils.getString(map, "shiftCode"));
            frr.setCodeName(MapUtils.getString(map, "codeName"));
            frr.setDept(MapUtils.getString(map, "dept"));
            frr.setDeptId(MapUtils.getString(map, "deptId"));
            frr.setHotelCode(hotelCode);
            frr.setBusinessDate(LocalDate.parse(businessDate));
            add(frr);
        }
        return hr.ok();
    }

    @Override
    public List<Map<String, Object>> getList(String hotelCode, String businessDate, String empName){
        List<Map<String, Object>> list = frontReceiveReportDao.getFrontReceiveList(hotelCode, businessDate, "1", empName);
        return list;
    }
}
