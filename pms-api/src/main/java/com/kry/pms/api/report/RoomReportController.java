package com.kry.pms.api.report;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.report.BusinessReport;
import com.kry.pms.model.persistence.report.RoomReport;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.busi.RoomRecordService;
import com.kry.pms.service.report.BusinessReportService;
import com.kry.pms.service.report.RoomReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/report/roomReport")
public class RoomReportController extends BaseController {

    @Autowired
    RoomReportService roomReportService;

    @GetMapping("/copyData")
    public HttpResponse auditNight(String businessDate){
        HttpResponse hr = new HttpResponse();
        roomReportService.copyData(businessDate);
        return hr;
    }

    /**
     * 功能描述: <br>夜审时，生成营业日报表-客房状态统计
     * 〈〉
     * @Param: [businessDate]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2019/11/19 11:46
     */
    @GetMapping("/saveRoomStatus")
    public HttpResponse saveRoomStatus(String businessDate){
        HttpResponse hr = new HttpResponse();
        User user = getUser();
        if(user == null){
            return hr.loginError();
        }
        roomReportService.saveRoomStatus(user, businessDate);
        return hr.ok();
    }

    /**
     * 功能描述: <br>添加所有房屋统计数据进入报表表-a、客房总数
     * 〈〉
     * @Param: [businessDate]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2019/11/20 15:23
     */
    @GetMapping("/totalRoomStatusAll")
    public HttpResponse totalRoomStatusAll(String businessDate){
        HttpResponse hr = new HttpResponse();
        User user = getUser();
        if(user == null){
            return hr.loginError();
        }
        hr = roomReportService.totalRoomStatusAll(user.getHotelCode(), businessDate);
        return hr.ok();
    }
    /**
     * 功能描述: <br>添加 b、出租总数 统计到报表
     * 〈〉
     * @Param: [businessDate]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2019/11/21 17:59
     */
    @GetMapping("/totalCheckInType")
    public HttpResponse totalCheckInType(String businessDate){
        HttpResponse hr = new HttpResponse();
        User user = getUser();
        if(user == null){
            return hr.loginError();
        }
        hr = roomReportService.totalCheckInType(user.getHotelCode(), businessDate);
        return hr.ok();
    }
    /**
     * 功能描述: <br>添加 c、售卖率 统计到报表
     * 〈〉
     * @Param: [businessDate]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2019/11/21 17:59
     */
    @GetMapping("/availableTotal")
    public HttpResponse availableTotal(String businessDate){
        HttpResponse hr = new HttpResponse();
        User user = getUser();
        if(user == null){
            return hr.loginError();
        }
        hr = roomReportService.availableTotal(user.getHotelCode(), businessDate);
        return hr.ok();
    }

    /**
     * 功能描述: <br>查询房间状态列表
     * 〈〉
     * @Param: [businessDate]
     * @Return: com.kry.pms.base.HttpResponse<java.util.List<com.kry.pms.model.persistence.report.RoomReport>>
     * @Author: huanghaibin
     * @Date: 2019/11/27 10:43
     */
    @GetMapping("/getList")
    public HttpResponse<List<RoomReport>> getByHotelCodeAndBusinessDate(String businessDate){
        HttpResponse<List<RoomReport>> hr = new HttpResponse();
        User user = getUser();
        if(user == null){
            return hr.loginError();
        }

        List<RoomReport> list = roomReportService.getByHotelCodeAndBusinessDate(user, LocalDate.parse(businessDate));
        hr.addData(list);
        return hr.ok();
    }

}
