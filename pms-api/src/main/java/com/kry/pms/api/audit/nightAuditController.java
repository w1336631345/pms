package com.kry.pms.api.audit;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.busi.CheckInRecordService;
import com.kry.pms.utils.ShiroUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.*;

@RestController
@RequestMapping(path = "/api/v1/audit/nightAudit")
public class nightAuditController extends BaseController {

    @Autowired
    CheckInRecordService checkInRecordService;

    /**
     * 功能描述: <br>夜间稽核列表
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
                                                                    String status) {
        HttpResponse<PageResponse<Map<String, Object>>> rep = new HttpResponse<>();
        User user = getUser();
        PageResponse<Map<String, Object>> page = checkInRecordService.unreturnedGuests(pageNum, pageSize,status, user);
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

}
