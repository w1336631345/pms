package com.kry.pms.job;

import com.kry.pms.base.Constants;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.controller.SessionController;
import com.kry.pms.dao.ScheduleJobDaoRepository;
import com.kry.pms.dao.busi.CheckInRecordDao;
import com.kry.pms.model.ScheduleJobModel;
import com.kry.pms.model.persistence.busi.DailyVerify;
import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.model.persistence.quartz.QuartzSet;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.busi.BillService;
import com.kry.pms.service.busi.CheckInRecordService;
import com.kry.pms.service.busi.DailyVerifyService;
import com.kry.pms.service.busi.RoomRecordService;
import com.kry.pms.service.goods.ProductService;
import com.kry.pms.service.quratz.impl.QuartzSetService;
import com.kry.pms.service.sys.BusinessSeqService;
import com.kry.pms.service.sys.UserService;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 功能描述: <br>自动夜审功能实现类
 * 〈〉
 * @Author: huanghaibin
 * @Date: 2019/11/7 10:16
 */
@Component
public class AutomaticNightTrial {

    @Autowired
    QuartzSetService quartzSetService;
    @Autowired
    CheckInRecordService checkInRecordService;
    @Autowired
    BillService billService;
    @Autowired
    RoomRecordService roomRecordService;
    @Autowired
    ProductService productService;
    @Autowired
    SessionController sessionController;
    @Autowired
    UserService userService;
    @Autowired
    ScheduleJobDaoRepository scheduleJobDaoRepository;
    @Autowired
    BusinessSeqService businessSeqService;
    @Autowired
    DailyVerifyService dailyVerifyService;
    @Autowired
    CheckInRecordDao checkInRecordDao;

    //查询所有开启自动夜审的酒店
    public List<QuartzSet> getAll(){
        List<QuartzSet> list = quartzSetService.getAll();
        return list;
    }
    public List<ScheduleJobModel> getAllStart(){
        //查询所有设置自动启动的定时任务
        List<ScheduleJobModel> list = scheduleJobDaoRepository.findAllByStatus(0);
        return list;
    }
    public List<ScheduleJobModel> getStartByHotelCode(String hotelCode){
        //查询所有设置自动启动的定时任务
        List<ScheduleJobModel> list = scheduleJobDaoRepository.findByHotelCodeAndStatus(hotelCode, 0);
        return list;
    }
    //自动夜审入账
    public void accountEntryListAll(String hotelCode) {
        List<ScheduleJobModel> listQ = getStartByHotelCode(hotelCode);
        for(int i=0; i<listQ.size(); i++){
            //自动夜审入账，所以暂定所有用户都踢出下线
            List<String> sessionIds = sessionController.getSysAllLoginSession(listQ.get(i).getHotelCode());
            if(sessionIds != null && !sessionIds.isEmpty()){
                HttpResponse rep = sessionController.sessionLoginOut(sessionIds);
            }
            //踢下线后修改所有用户为禁止登录状态
            List<User> users = userService.getAllByHotelCode(listQ.get(i).getHotelCode());
            for(int u=0; u<users.size(); u++){
                User user = users.get(i);
                user.setAllowLogin("audit");//normal：正常可登录状态，audit：夜审不可登录
                userService.modify(user);
            }
            LocalDate businessDate = businessSeqService.getBuinessDate(hotelCode);
            //在入账之前，先要处理需要处理的数据（应到未到，应退未退）
            List<Map<String, Object>> p = checkInRecordDao.unreturnedGuests("N", "R", hotelCode);
            String ids[] = new String[1];
            //处理应到未到
            for(int j=0; j<p.size(); j++){
                ids[0] = MapUtils.getString(p.get(j), "id");
                checkInRecordService.callOffReserveAudit(ids);
            }
            List<Map<String, Object>> out = checkInRecordDao.unreturnedGuests("A", "I", hotelCode);
            //处理应退未退
            for(int o=0; o<out.size(); o++){
                String id = MapUtils.getString(out.get(o), "id");
                checkInRecordService.hangUp(id);
            }
            //查询需要自动入账的记录（注：自动入账为最近营业日期的账，入账后营业日期+1，下面是查询的所有记录，后期整改）
            List<RoomRecord> list = roomRecordService.accountEntryListAll(listQ.get(i).getHotelCode(), businessDate);
            billService.putAcountAUTO(list, businessDate);//保存到bill
            //同时添加入账记录（t_daily_verify）
            DailyVerify dv = new DailyVerify();
            dv.setBusinessDate(businessDate);
            dv.setType(Constants.auditNightMode.NIGHT_AUDIT_AUTO);//夜审方式，自动
            dv.setHotelCode(listQ.get(i).getHotelCode());
            dv.setVerifyDate(LocalDate.now());
            dv.setCreateDate(LocalDateTime.now());
            dv.setStatus("success");
            dailyVerifyService.add(dv);
            //一个酒店夜审入账完毕后将用户登录禁止状态修改为正常
            for(int u=0; u<users.size(); u++){
                User user = users.get(i);
                user.setAllowLogin("normal");//normal：正常可登录状态，audit：夜审不可登录
                userService.modify(user);
            }
        }
    }

    private String getDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}
