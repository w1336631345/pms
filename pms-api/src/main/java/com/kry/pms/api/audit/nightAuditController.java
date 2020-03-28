package com.kry.pms.api.audit;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.model.persistence.sys.SystemConfig;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.audit.NightAuditService;
import com.kry.pms.service.busi.BillService;
import com.kry.pms.service.busi.CheckInRecordService;
import com.kry.pms.service.busi.RoomRecordService;
import com.kry.pms.service.sys.BusinessSeqService;
import com.kry.pms.service.sys.SystemConfigService;
import com.kry.pms.utils.ShiroUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@RestController
@RequestMapping(path = "/api/v1/audit/nightAudit")
public class nightAuditController extends BaseController {

    @Autowired
    CheckInRecordService checkInRecordService;
    @Autowired
    RoomRecordService roomRecordService;
    @Autowired
    BillService billService;
    @Autowired
    SessionDAO sessionDAO;
    @Autowired
    BusinessSeqService businessSeqService;
    @Autowired
    NightAuditService nightAuditService;
    @Autowired
    SystemConfigService systemConfigService;

    /**
     * 功能描述: <br>夜间稽核列表(没用，用的unreturnedGuests)
     * 〈〉状态（R：预订，I：入住，O：退房，D：历史订单，N：未到，S：退房未结账，X：取消）
     * @Param: [pageNum, pageSize, status]
     * @Return: com.kry.pms.base.HttpResponse<com.kry.pms.base.PageResponse<com.kry.pms.model.persistence.busi.CheckInRecord>>
     * @Author: huanghaibin
     * @Date: 2019/10/23 14:54
     */
    @GetMapping("/notYet")
    public HttpResponse<PageResponse<CheckInRecord>> notYet(@RequestParam(value = "pageNum", defaultValue = "1")Integer pageNum,
                                                            @RequestParam(value = "pageSize", defaultValue = "10")Integer pageSize,
                                                            String status) {
        HttpResponse<PageResponse<CheckInRecord>> rep = new HttpResponse<>();
        User user = getUser();
        PageResponse<CheckInRecord> page = checkInRecordService.notYet(pageNum, pageSize,status, user);
        rep.addData(page);
        return rep;
    }
    /**
     * 功能描述: <br>夜间稽核列表(没用，用的unreturnedGuests)
     * 〈〉
     * @Param: [pageNum, pageSize, status]
     * @Return: com.kry.pms.base.HttpResponse<com.kry.pms.base.PageResponse<java.util.Map<java.lang.String,java.lang.Object>>>
     * @Author: huanghaibin
     * @Date: 2020/3/27 10:37
     */
    @GetMapping("/notYetMap")
    public HttpResponse<PageResponse<Map<String, Object>>> notYetMap(@RequestParam(value = "pageNum", defaultValue = "1")Integer pageNum,
                                                                            @RequestParam(value = "pageSize", defaultValue = "10")Integer pageSize,
                                                                            String status) {
        HttpResponse<PageResponse<Map<String, Object>>> rep = new HttpResponse<>();
        User user = getUser();
        PageResponse<Map<String, Object>> page = checkInRecordService.notYetMap(pageNum, pageSize,status, user);
        rep.addData(page);
        return rep;
    }
    /**
     * 功能描述: <br>应退未退客人（时间过了依旧在住）
     * 〈〉
     * @Param: [pageNum, pageSize, status]
     * @Return: com.kry.pms.base.HttpResponse<com.kry.pms.base.PageResponse<java.util.Map<java.lang.String,java.lang.Object>>>
     * @Author: huanghaibin
     * @Date: 2019/10/23 16:07
     */
    @GetMapping("/unreturnedGuests")
    public HttpResponse<PageResponse<Map<String, Object>>> unreturnedGuests(@RequestParam(value = "pageNum", defaultValue = "1")Integer pageNum,
                                                                    @RequestParam(value = "pageSize", defaultValue = "10")Integer pageSize,
                                                                    String mainNum, String status) {
        HttpResponse<PageResponse<Map<String, Object>>> rep = new HttpResponse<>();
        User user = getUser();
        PageResponse<Map<String, Object>> page = checkInRecordService.unreturnedGuests(pageNum, pageSize,mainNum,status, user);
        rep.addData(page);
        return rep;
    }

    /**
     * 功能描述: <br>查询房租预审及入账列表（夜间稽核可入账的入住（I））
     * 〈〉
     * @Param: [pageNum, pageSize]
     * @Return: com.kry.pms.base.HttpResponse<com.kry.pms.base.PageResponse<com.kry.pms.model.persistence.busi.CheckInRecord>>
     * @Author: huanghaibin
     * @Date: 2019/10/26 16:58
     */
    @GetMapping("/accountEntryList")
    public HttpResponse<PageResponse<CheckInRecord>> accountEntryList(@RequestParam(value = "pageNum", defaultValue = "1")Integer pageNum,
                                                            @RequestParam(value = "pageSize", defaultValue = "10")Integer pageSize) {
        HttpResponse<PageResponse<CheckInRecord>> rep = new HttpResponse<>();
        User user = getUser();
        if(user == null){
            return rep.error(403, "未登录");
        }
        PageResponse<CheckInRecord> page = checkInRecordService.accountEntryList(pageNum, pageSize, user);
        rep.addData(page);
        return rep;
    }

    /**
     * 功能描述: <br>查询房租预审及入账列表(roomRecord)停用
     * 〈〉
     * @Param: [pageNum, pageSize]
     * @Return: com.kry.pms.base.HttpResponse<com.kry.pms.base.PageResponse<com.kry.pms.model.persistence.busi.RoomRecord>>
     * @Author: huanghaibin
     * @Date: 2019/10/30 18:17
     */
    @GetMapping("/accountEntryListTo")
    public HttpResponse<PageResponse<RoomRecord>> accountEntryListTo(@RequestParam(value = "pageNum", defaultValue = "1")Integer pageNum,
                                                                     @RequestParam(value = "pageSize", defaultValue = "10")Integer pageSize) {
        HttpResponse<PageResponse<RoomRecord>> rep = new HttpResponse<>();
        User user = getUser();
        if(user == null){
            return rep.error(403, "未登录");
        }
        PageResponse<RoomRecord> page = roomRecordService.accountEntryListTest(pageNum, pageSize, user);
        rep.addData(page);
        return rep;
    }

    /**
     * 功能描述: <br>夜间稽核各项状态数量统计
     * 〈〉
     * @Param: []
     * @Return: com.kry.pms.base.HttpResponse<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
     * @Author: huanghaibin
     * @Date: 2019/10/23 17:28
     */
    @GetMapping("/getStatistics")
    public HttpResponse<List<Map<String, Object>>> getStatistics() {
        HttpResponse<List<Map<String, Object>>> rep = new HttpResponse<>();
        User user = getUser();
        List<Map<String, Object>> list = checkInRecordService.getStatistics(user);
        rep.addData(list);
        return rep;
    }


    /**
     * 功能描述: <br>手动夜审入账
     * 〈〉
     * @Param: [ids]
     * @Return: com.kry.pms.base.HttpResponse<java.lang.String>
     * @Author: huanghaibin
     * @Date: 2019/11/1 9:48
     */
    @PostMapping("/manualAdd")
    public HttpResponse<String> manualAdd(@RequestBody String[] ids) {
        HttpResponse<String> rep = getDefaultResponse();
        LocalTime lt = LocalTime.now();
        SystemConfig systemConfig = systemConfigService.getByHotelCodeAndKey(getCurrentHotleCode(), "manualAddTime");
        if(systemConfig != null && systemConfig.getValue() != null){
            LocalTime localTime = LocalTime.parse(systemConfig.getValue());
            if(lt.isBefore(localTime)){
//            return rep.error("夜审时间未到");
            }
        }
        if(ids != null){
            System.out.println("传过来的id数："+ids.length);
        }

        User loginUser = getUser();
        if(loginUser == null){
            return rep.loginError();
        }
        Collection<Session> sessions =  sessionDAO.getActiveSessions();
        for (Session session : sessions) {
            User user;
            SimplePrincipalCollection principalCollection;
            if(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY )== null){
                continue;
            }else {
                principalCollection = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                user = (User) principalCollection.getPrimaryPrincipal();
                if(user != null){
                    if(!user.getId().equals(loginUser.getId())){
                        return rep.error("还有其他用户在线，不能入账");
                    }
                }
            }
        }
        String shiftCode = getShiftCode();
        rep = nightAuditService.manualAdd(loginUser, ids, shiftCode);
        return rep;
    }

    /**
     * 功能描述: <br>夜审入账后生产报表（下一步）
     * 〈〉
     * @Param: []
     * @Return: com.kry.pms.base.HttpResponse<java.lang.String>
     * @Author: huanghaibin
     * @Date: 2019/11/25 10:18
     */
    @PostMapping("/addReportAll")
    public HttpResponse<String> addReportAll() {
        HttpResponse<String> rep = getDefaultResponse();
        User loginUser = getUser();
        if(loginUser == null){
            return rep.error(403, "未登录");
        }
        rep = nightAuditService.addReportAll(loginUser);
        return rep;
    }

}
