package com.kry.pms.service.report.impl;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.report.FrontEntryReportDao;
import com.kry.pms.model.persistence.report.FrontEntryReport;
import com.kry.pms.service.report.FrontEntryReportService;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FrontEntryReportServiceImpl implements FrontEntryReportService {

    @Autowired
    FrontEntryReportDao frontEntryReportDao;

    @Override
    public FrontEntryReport add(FrontEntryReport entity) {
        return frontEntryReportDao.save(entity);
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public FrontEntryReport modify(FrontEntryReport frontEntryReport) {
        return null;
    }

    @Override
    public FrontEntryReport findById(String id) {
        return null;
    }

    @Override
    public List<FrontEntryReport> getAllByHotelCode(String code) {
        return null;
    }

    @Override
    public List<FrontEntryReport> findByHotelCodeAndBusinessDate(String hotelCode, String businessDate) {
        List<FrontEntryReport> list = frontEntryReportDao.findByHotelCodeAndBusinessDate(hotelCode, LocalDate.parse(businessDate));
        return list;
    }

    @Override
    public PageResponse<FrontEntryReport> listPage(PageRequest<FrontEntryReport> prq) {
        return null;
    }

    //自动夜审生成报表，前台入账报表（方案1-没用）
    @Override
    @Transactional(rollbackFor=Exception.class)
    public HttpResponse frontEntryList(String hotelCode, String businessDate){
        HttpResponse hr = new HttpResponse();
        List<FrontEntryReport> listfer = frontEntryReportDao.findByHotelCodeAndBusinessDate(hotelCode, LocalDate.parse(businessDate));
        if(listfer != null && !listfer.isEmpty()){
            return hr.ok("今日入账报表已经生成");
        }
        List<Map<String, Object>> listTotal = frontEntryReportDao.getTotal(hotelCode, businessDate);
        int sort = 0;
        for(int i=0; i<listTotal.size(); i++){
            FrontEntryReport fer = new FrontEntryReport();
            Map<String, Object> map = listTotal.get(i);
            fer.setAccountNum(MapUtils.getString(map, "code_name"));
            fer.setIsCrossColumn("1");
            fer.setBusinessDate(LocalDate.parse(businessDate));
            fer.setCategoryId(MapUtils.getString(map, "category_id"));
            fer.setCodeNum(MapUtils.getString(map, "code_"));
            fer.setHotelCode(hotelCode);
            fer.setSort(sort);
            add(fer);
            sort++;
            String code_ = MapUtils.getString(map, "code_");
            List<Map<String, Object>> list = frontEntryReportDao.getList(hotelCode, businessDate, code_);
            for(int j=0; j<list.size(); j++){
                FrontEntryReport ferl = new FrontEntryReport();
                ferl.setAccountNum(MapUtils.getString(list.get(j), "accountNum"));
                ferl.setRoomNum(MapUtils.getString(list.get(j), "room_num"));
                ferl.setCustName(MapUtils.getString(list.get(j), "custName"));
                ferl.setCodeName(MapUtils.getString(list.get(j), "code_name"));
                ferl.setCost(MapUtils.getString(list.get(j), "cost"));
                ferl.setCreateDate(MapUtils.getString(list.get(j), "create_date"));
                ferl.setCashier(MapUtils.getString(list.get(j), "cashier"));
                ferl.setShiftCode(MapUtils.getString(list.get(j), "shift_code"));
                ferl.setCategoryId(MapUtils.getString(map, "category_id"));
                ferl.setCodeNum(MapUtils.getString(map, "codeNum"));
                ferl.setHotelCode(hotelCode);
                ferl.setBusinessDate(LocalDate.parse(businessDate));
                ferl.setSort(sort);
                add(ferl);
                sort++;
            }
            FrontEntryReport ferTotal = new FrontEntryReport();
            ferTotal.setAccountNum("小计");
            ferTotal.setCost(MapUtils.getString(map, "cost"));
            ferTotal.setBusinessDate(LocalDate.parse(businessDate));
            ferTotal.setCategoryId(MapUtils.getString(map, "category_id"));
            ferTotal.setCodeNum(MapUtils.getString(map, "codeNum"));
            ferTotal.setHotelCode(hotelCode);
            ferTotal.setSort(sort);
            add(ferTotal);
            sort++;
        }
        return hr;
    }

    //自动夜审生成报表，前台入账报表（方案2-在用）
    @Override
    @Transactional(rollbackFor=Exception.class)
    public HttpResponse frontEntryList2(String hotelCode, String businessDate){
        HttpResponse hr = new HttpResponse();
        List<FrontEntryReport> listfer = frontEntryReportDao.findByHotelCodeAndBusinessDate(hotelCode, LocalDate.parse(businessDate));
        if(listfer != null && !listfer.isEmpty()){
            return hr.ok("今日入账报表已经生成");
        }
        int sort = 0;
        List<Map<String, Object>> list = frontEntryReportDao.getList(hotelCode, businessDate, null);
        for(int j=0; j<list.size(); j++){
            FrontEntryReport ferl = new FrontEntryReport();
            ferl.setAccountNum(MapUtils.getString(list.get(j), "accountNum"));
            ferl.setRoomNum(MapUtils.getString(list.get(j), "room_num"));
            ferl.setCustName(MapUtils.getString(list.get(j), "custName"));
            ferl.setCodeName(MapUtils.getString(list.get(j), "code_name"));
            ferl.setCost(MapUtils.getString(list.get(j), "cost"));
            ferl.setCreateDate(MapUtils.getString(list.get(j), "create_date"));
            ferl.setCashier(MapUtils.getString(list.get(j), "cashier"));
            ferl.setShiftCode(MapUtils.getString(list.get(j), "shift_code"));
            ferl.setCategoryId(MapUtils.getString(list.get(j), "category_id"));
            ferl.setCodeNum(MapUtils.getString(list.get(j), "codeNum"));
            ferl.setHotelCode(hotelCode);
            ferl.setBusinessDate(LocalDate.parse(businessDate));
            ferl.setSort(sort);
            add(ferl);
            sort++;
            }
        return hr;
    }

    @Override
    public List<FrontEntryReport> getFrontEntryList(String hotelCode, String businessDate, String cashier){
        List<Map<String, Object>> listTotal = frontEntryReportDao.getTotal2(hotelCode, businessDate, cashier);
        List<FrontEntryReport> reList = new ArrayList<>();
        for(int i=0; i<listTotal.size(); i++){
            FrontEntryReport fer = new FrontEntryReport();
            Map<String, Object> map = listTotal.get(i);
            fer.setAccountNum(MapUtils.getString(map, "code_name"));
            fer.setIsCrossColumn("1");
            fer.setBusinessDate(LocalDate.parse(businessDate));
            fer.setCategoryId(MapUtils.getString(map, "category_id"));
            fer.setCodeNum(MapUtils.getString(map, "code_num"));
            fer.setHotelCode(hotelCode);
            reList.add(fer);
            String code_ = MapUtils.getString(map, "code_num");
            List<FrontEntryReport> list = frontEntryReportDao.getFrontList(hotelCode, businessDate, code_, cashier);
            for(int j=0; j<list.size(); j++){
                reList.add(list.get(j));
            }
            FrontEntryReport ferTotal = new FrontEntryReport();
            ferTotal.setAccountNum("小计");
            ferTotal.setCost(MapUtils.getString(map, "cost"));
            ferTotal.setBusinessDate(LocalDate.parse(businessDate));
            ferTotal.setCategoryId(MapUtils.getString(map, "category_id"));
            ferTotal.setCodeNum(MapUtils.getString(map, "codeNum"));
            ferTotal.setHotelCode(hotelCode);
            reList.add(ferTotal);
        }
        return reList;
    }
}
