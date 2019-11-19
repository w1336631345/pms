package com.kry.pms.service.report.impl;

import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.report.BusinessReportDao;
import com.kry.pms.dao.report.RoomReportDao;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.busi.DailyVerify;
import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.model.persistence.report.BusinessReport;
import com.kry.pms.model.persistence.report.RoomReport;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.busi.RoomRecordService;
import com.kry.pms.service.report.BusinessReportService;
import com.kry.pms.service.report.RoomReportService;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RoomReportServiceImpl implements RoomReportService {

    @Autowired
    RoomReportDao roomReportDao;

    @Override
    public HttpResponse copyData(String businessDate) {
        //每天一次定时在凌晨2-6点
        HttpResponse hr = new HttpResponse();
        roomReportDao.copyData(businessDate);
        return hr.ok("房间状态数据copy完成");
    }

    @Override
    public RoomReport add(RoomReport entity) {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public RoomReport modify(RoomReport roomReport) {
        return null;
    }

    @Override
    public RoomReport findById(String id) {
        return null;
    }

    @Override
    public List<RoomReport> getAllByHotelCode(String code) {
        return null;
    }

    @Override
    public PageResponse<RoomReport> listPage(PageRequest<RoomReport> prq) {
        return null;
    }

    @Override
    public List<RoomReport> getByHotelCodeAndBusinessDate(User user, String businessDate){
        List<RoomReport> list = roomReportDao.getByHotelCodeAndBusinessDate(user.getHotelCode(), businessDate);
        return list;
    }
    @Override
    public List<Map<String, Object>> getRoomStatus(User user, String businessDate){
        List<Map<String, Object>> list = roomReportDao.getTotalRoomStatus(user.getHotelCode(), businessDate);
        return list;
    }
}
