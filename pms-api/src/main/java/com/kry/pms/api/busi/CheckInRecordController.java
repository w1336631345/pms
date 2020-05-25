package com.kry.pms.api.busi;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.DtoResponse;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.annotation.OperationLog;
import com.kry.pms.model.http.request.busi.CheckInRecordListBo;
import com.kry.pms.model.http.request.busi.CheckUpdateItemTestBo;
import com.kry.pms.model.http.request.busi.TogetherBo;
import com.kry.pms.model.http.response.busi.CheckInRecordListVo;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.busi.CheckInRecordService;
import freemarker.template.TemplateException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/busi/checkInRecord")
public class CheckInRecordController extends BaseController<CheckInRecord> {
    @Autowired
    CheckInRecordService checkInRecordService;

    @PostMapping
    public HttpResponse<CheckInRecord> add(@RequestBody CheckInRecord checkInRecord) {
        return getDefaultResponse().addData(checkInRecordService.add(checkInRecord));
    }

    @PutMapping
    public HttpResponse<CheckInRecord> modify(@RequestBody CheckInRecord checkInRecord) {
        return getDefaultResponse().addData(checkInRecordService.modify(checkInRecord));
    }

    /**
     * 功能描述: <br>修改主单时，判断是否修改到店离店时间，做相应处理
     * 〈〉
     *
     * @Param: [checkInRecord]
     * @Return: com.kry.pms.base.HttpResponse<com.kry.pms.model.persistence.busi.CheckInRecord>
     * @Author: huanghaibin
     * @Date: 2020/1/2 14:48
     */
    @PutMapping(path = "/modifyInfo")
//    @OperationLog(remark = "主单修改操作")
    public HttpResponse<CheckInRecord> modifyInfo(@RequestBody CheckInRecord checkInRecord) {
        HttpResponse hr = checkInRecordService.modifyInfo(checkInRecord);
        return hr;
    }

    /**
     * hangUp
     * 功能描述: <br>保存-预订
     * 〈〉
     *
     * @Param: [checkInRecord]
     * @Return: com.kry.pms.base.HttpResponse<com.kry.pms.model.persistence.busi.CheckInRecord>
     * @Author: huanghaibin
     * @Date: 2020/1/13 15:21
     */
    @PostMapping(path = "/book")
    @OperationLog(remark = "团队/多人预订")
    public HttpResponse<CheckInRecord> book(@RequestBody CheckInRecord checkInRecord) {
        checkInRecord.setHotelCode(getCurrentHotleCode());
        HttpResponse hr = checkInRecordService.book(checkInRecord);
        return hr;
    }

    /**
     * 功能描述: <br>直接预订或入住
     * 〈〉
     *
     * @Param: [checkInRecord]
     * @Return: com.kry.pms.base.HttpResponse<com.kry.pms.model.persistence.busi.CheckInRecord>
     * @Author: huanghaibin
     * @Date: 2020/1/8 10:45
     */
    @PostMapping(path = "/bookByRoom")
    @OperationLog(remark = "房态图直接预订或入住")
    public HttpResponse<CheckInRecord> bookByRoom(@RequestBody CheckInRecord checkInRecord) {
        HttpResponse hr = new HttpResponse();
        User user = getUser();
        checkInRecord.setHotelCode(user.getHotelCode());
        hr = checkInRecordService.bookByRoomTypeTest(checkInRecord);
        return hr;
    }

    /**
     * 功能描述: <br>散客的单房预订
     * 〈〉
     *
     * @Param: [checkInRecord]
     * @Return: com.kry.pms.base.HttpResponse<com.kry.pms.model.persistence.busi.CheckInRecord>
     * @Author: huanghaibin
     * @Date: 2020/2/26 14:26
     */
    @PostMapping(path = "/bookOne")
    @OperationLog(remark = "散客单房预订")
    public HttpResponse<CheckInRecord> bookOne(@RequestBody CheckInRecord checkInRecord) {
        User user = getUser();
        checkInRecord.setHotelCode(user.getHotelCode());
        HttpResponse hr = checkInRecordService.singleRoom(checkInRecord);
        return hr;
    }

    @DeleteMapping
    public HttpResponse<String> delete(String id) {
        HttpResponse<String> rep = new HttpResponse<>();
        checkInRecordService.delete(id);
        return rep;
    }

    @GetMapping(path = "/book/{id}")
    public HttpResponse<List<CheckInRecord>> findByBookingId(@PathVariable("id") String bookId) {
        HttpResponse<List<CheckInRecord>> rep = new HttpResponse<List<CheckInRecord>>();
        rep.addData(checkInRecordService.findByBookId(bookId));
        return rep;
    }

    @GetMapping(path = "/detail/book/{id}")
    public HttpResponse<List<CheckInRecord>> findDetailByBookingId(@PathVariable("id") String bookId) {
        HttpResponse<List<CheckInRecord>> rep = new HttpResponse<List<CheckInRecord>>();
        rep.addData(checkInRecordService.findDetailByBookingId(bookId));
        return rep;
    }

    @GetMapping(path = "/detail/{id}")
    public HttpResponse<CheckInRecord> findDetailById(@PathVariable("id") String id) {
        HttpResponse<CheckInRecord> rep = new HttpResponse<CheckInRecord>();
        rep.addData(checkInRecordService.findById(id));
        return rep;
    }

    @GetMapping(path = "/orderNum/{orderNum}")
    public HttpResponse<List<CheckInRecord>> findDetailByOrderNum(@PathVariable("orderNum") String orderNum) {
        HttpResponse<List<CheckInRecord>> rep = new HttpResponse<List<CheckInRecord>>();
        rep.addData(checkInRecordService.findByOrderNum(getCurrentHotleCode(),orderNum));
        return rep;
    }

    /**
     * 功能描述: <br>只查询宾客的数据（不查询主单和预订单）jpa关联查询，效率低，弃用
     * 〈〉
     *
     * @Param: [orderNum]
     * @Return: com.kry.pms.base.HttpResponse<java.util.List < com.kry.pms.model.persistence.busi.CheckInRecord>>
     * @Author: huanghaibin
     * @Date: 2020/3/9 18:42
     */
    @Deprecated
    @GetMapping(path = "/orderNumC/{orderNum}")
    public HttpResponse<List<CheckInRecord>> findDetailByOrderNumC(@PathVariable("orderNum") String orderNum) {
        HttpResponse<List<CheckInRecord>> rep = new HttpResponse<List<CheckInRecord>>();
        rep.addData(checkInRecordService.findByOrderNumC(getCurrentHotleCode(),orderNum));
        return rep;
    }

    /**
     * 功能描述: <br>只查询宾客的数据（不查询主单和预订单） 在用
     * 〈〉
     *
     * @Param: [orderNum]
     * @Return: com.kry.pms.base.HttpResponse<java.util.List < java.util.Map < java.lang.String, java.lang.Object>>>
     * @Author: huanghaibin
     * @Date: 2020/3/10 10:25
     */
    @GetMapping(path = "/orderNumC2/{orderNum}")
    public HttpResponse<List<Map<String, Object>>> findDetailByOrderNumCSQL(@PathVariable("orderNum") String orderNum) throws IOException, TemplateException {
        HttpResponse<List<Map<String, Object>>> rep = new HttpResponse<List<Map<String, Object>>>();
        rep.addData(checkInRecordService.findByOrderNumC2(getCurrentHotleCode(), orderNum));
        return rep;
    }

    @GetMapping(path = "/orderNum2/{orderNum}")
    public HttpResponse<List<Map<String, Object>>> findDetailByOrderNumSQL(@PathVariable("orderNum") String orderNum) throws IOException, TemplateException {
        HttpResponse<List<Map<String, Object>>> rep = new HttpResponse<List<Map<String, Object>>>();
        rep.addData(checkInRecordService.findByOrderNum2(getCurrentHotleCode(), orderNum));
        return rep;
    }

    @GetMapping(path = "/noRole/orderNum2")
    public HttpResponse<List<Map<String, Object>>> noRole(String hotelCode, String orderNum) throws IOException, TemplateException {
        HttpResponse<List<Map<String, Object>>> rep = new HttpResponse<List<Map<String, Object>>>();
        rep.addData(checkInRecordService.findByOrderNum2(hotelCode, orderNum));
        return rep;
    }

    @PostMapping(path = "/together")
    public HttpResponse<CheckInRecord> addCustomerTogether(@RequestBody TogetherBo togetherBo) {
        return getDefaultResponse().addData(checkInRecordService.addTogether(togetherBo));
    }

    /**
     * 功能描述: <br>批量修改
     * 〈〉
     *
     * @Param: [checkUpdateItemBo]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2019/12/11 15:44
     */
    @PostMapping(path = "/updateItem")
    @OperationLog(remark = "批量修改保存")
    public HttpResponse updateItem(@RequestBody CheckUpdateItemTestBo checkUpdateItemTestBo) {
        HttpResponse hr = new HttpResponse();
        User user = getUser();
        if (user == null) {
            return hr.loginError();
        }
        hr = checkInRecordService.updateAll(checkUpdateItemTestBo);
        return hr;
    }

    /**
     * 功能描述: <br>批量取消入住
     * 〈〉
     *
     * @Param: [ids]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2019/12/12 18:15
     */
    @PostMapping(path = "/cancelIn")
    @OperationLog(remark = "批量取消入住")
    public HttpResponse cancelIn(@RequestBody String[] ids) {
        HttpResponse hr = new HttpResponse();
        User user = getUser();
        if (user == null) {
            return hr.loginError();
        }
        hr = checkInRecordService.cancelIn(ids, getCurrentHotleCode());
        return hr;
    }

    /**
     * 功能描述: <br>批量取消排房
     * 〈〉
     *
     * @Param: [roomAssignBo]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2019/12/16 11:09
     */
    @PostMapping(path = "/callOffAssignRoom")
    @OperationLog(remark = "批量取消排房")
    public HttpResponse callOffAssignRoom(@RequestBody String[] ids) {
        HttpResponse rep = new HttpResponse();
        rep = checkInRecordService.callOffAssignRoom(ids);
        return rep;
    }

    /**
     * 功能描述: <br>批量取消预订
     * 〈〉
     *
     * @Param: [ids]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2019/12/17 11:59
     */
    @PostMapping(path = "/callOffReserve")
    @OperationLog(remark = "批量取消预订")
    public HttpResponse callOffReserve(@RequestBody String[] ids) {
        HttpResponse rep = new HttpResponse();
        rep = checkInRecordService.callOffReserve(ids);
        return rep;
    }
    /**
     * 功能描述: <br>夜审中的取消预订
     * 〈〉
     * @Param: [ids]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2020/3/27 15:43
     */
    @PostMapping(path = "/callOffReserveAudit")
    @OperationLog(remark = "夜审中的取消预订")
    public HttpResponse callOffReserveAudit(@RequestBody String[] ids) {
        HttpResponse rep = new HttpResponse();
        rep = checkInRecordService.callOffReserveAudit(ids);
        return rep;
    }
    /**
     * 功能描述: <br>主单的取消预订
     * 〈〉
     * @Param: [id]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2020/3/31 19:14
     */
    @GetMapping(path = "/callOffG")
    @OperationLog(remark = "主单的取消预订")
    public HttpResponse callOffG(String id) {
        HttpResponse rep = new HttpResponse();
        rep = checkInRecordService.callOffG(id);
        return rep;
    }
    /**
     * 功能描述: <br>主单恢复
     * 〈〉
     * @Param: [id]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2020/3/31 19:48
     */
    @GetMapping(path = "/recovery")
    @OperationLog(remark = "主单恢复")
    public HttpResponse recovery(String id) {
        HttpResponse rep = new HttpResponse();
        rep = checkInRecordService.recovery(id);
        return rep;
    }

    /**
     * 修改预留的删除（其实就是取消预留）
     *
     * @param id
     * @return
     */
    @GetMapping(path = "/offReserve")
    @OperationLog(remark = "修改预留的删除（其实就是取消预留）")
    public HttpResponse offReserve(String id) {
        HttpResponse rep = new HttpResponse();
        rep = checkInRecordService.offReserve(id);
        return rep;
    }

    /**
     * 功能描述: <br>房态图批量操作的，快速入住
     * 〈〉
     *
     * @Param: [checkInRecordListBo]
     * @Return: com.kry.pms.base.HttpResponse63
     * @Author: huanghaibin
     * @Date: 2020/1/9 10:22
     */
    @PostMapping(path = "/bookByRoomList")
    @OperationLog(remark = "房态图批量操作的，快速入住")
    public HttpResponse bookByRoomList(@RequestBody CheckInRecordListBo checkInRecordListBo) {
        HttpResponse hr = new HttpResponse();
        List<CheckInRecord> list = checkInRecordService.bookByRoomList(checkInRecordListBo, getCurrentHotleCode());
        hr.addData(list);
        return hr.ok();
    }

    public HttpResponse<PageResponse<CheckInRecordListVo>> queryHistory() {
        HttpResponse<PageResponse<CheckInRecordListVo>> rep = new HttpResponse<PageResponse<CheckInRecordListVo>>();
        return rep;
    }

    @GetMapping("/hangUp/account/{accountId}")
    public HttpResponse<String> hangUp(@PathVariable("accountId") String id,String type,String extFee,String orderNum) {
        HttpResponse<String> rep = new HttpResponse<>();
        DtoResponse<String> response = checkInRecordService.hangUp(id,type,extFee,orderNum);
        BeanUtils.copyProperties(response, rep);
        return rep;
    }

    @GetMapping("/checkOut/{type}/{id}")
    public HttpResponse<String> checkOut(@PathVariable("type") String type, @PathVariable("id") String id,String orderNum) {
        HttpResponse<String> rep = new HttpResponse<>();
        DtoResponse<List<CheckInRecord>> response = checkInRecordService.checkOut(type, id,orderNum);
        BeanUtils.copyProperties(response, rep);
        rep.setData(null);
        return rep;
    }

    @GetMapping
    public HttpResponse<PageResponse<CheckInRecord>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException {
        HttpResponse<PageResponse<CheckInRecord>> rep = new HttpResponse<PageResponse<CheckInRecord>>();
        PageRequest<CheckInRecord> req = parse2PageRequest(request);
        return rep.addData(checkInRecordService.listPage(req));
    }

    /**
     * 功能描述: <br>预订列表（old）
     * 〈〉
     *
     * @Param: [request]
     * @Return: com.kry.pms.base.HttpResponse<com.kry.pms.base.PageResponse < com.kry.pms.model.http.response.busi.CheckInRecordListVo>>
     * @Author: huanghaibin
     * @Date: 2020/2/29 14:54
     */
    @GetMapping(path = "/summary")
    public HttpResponse<PageResponse<CheckInRecordListVo>> querySummaryList(HttpServletRequest request) throws InstantiationException, IllegalAccessException {
        HttpResponse<PageResponse<CheckInRecordListVo>> rep = new HttpResponse<PageResponse<CheckInRecordListVo>>();
        PageRequest<CheckInRecord> req = parse2PageRequest(request);
        return rep.addData(checkInRecordService.querySummaryList(req));
    }

    /**
     * 功能描述: <br>预订列表（new）
     * 〈〉
     *
     * @Param: [request]
     * @Return: com.kry.pms.base.HttpResponse<com.kry.pms.base.PageResponse < com.kry.pms.model.http.response.busi.CheckInRecordListVo>>
     * @Author: huanghaibin
     * @Date: 2020/2/29 14:55
     */
//	@GetMapping(path = "/summaryTo")
//	public HttpResponse<PageResponse<Map<String, Object>>> querySummaryListTo(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
//		HttpResponse<PageResponse<Map<String, Object>>> rep = new HttpResponse<PageResponse<Map<String, Object>>>();
//		PageRequest<CheckInRecord> req = parse2PageRequest(request);
//		return rep.addData(checkInRecordService.querySummaryListTo(req));
//	}
    @GetMapping(path = "/summaryTo")
    public HttpResponse<PageResponse<Map<String, Object>>> querySummaryListTo(HttpServletRequest request) throws IOException, TemplateException, IllegalAccessException, InstantiationException {
        HttpResponse<PageResponse<Map<String, Object>>> rep = new HttpResponse<PageResponse<Map<String, Object>>>();
        return rep.addData(checkInRecordService.querySummaryListToBySql(getCurrentHotleCode(), parse2CommonPageRequest(request)));
    }
    /**
     * 功能描述: <br>历史住客
     * 〈〉
     * @Param: [request]
     * @Return: com.kry.pms.base.HttpResponse<com.kry.pms.base.PageResponse<java.util.Map<java.lang.String,java.lang.Object>>>
     * @Author: huanghaibin
     * @Date: 2020/5/6 16:59
     */
    @GetMapping(path = "/summaryToHis")
    public HttpResponse<PageResponse<Map<String, Object>>> resverListHis(HttpServletRequest request) throws IOException, TemplateException, IllegalAccessException, InstantiationException {
        HttpResponse<PageResponse<Map<String, Object>>> rep = new HttpResponse<PageResponse<Map<String, Object>>>();
        return rep.addData(checkInRecordService.resverListHis(getCurrentHotleCode(), parse2CommonPageRequest(request)));
    }
    /**
     * 功能描述: <br>预订列表的合计
     * 〈〉
     * @Param: [request]
     * @Return: com.kry.pms.base.HttpResponse<com.kry.pms.base.PageResponse<java.util.Map<java.lang.String,java.lang.Object>>>
     * @Author: huanghaibin
     * @Date: 2020/4/23 16:07
     */
    @GetMapping(path = "/summaryTotal")
    public HttpResponse<List<Map<String, Object>>> summaryTotal(HttpServletRequest request) throws IOException, TemplateException, IllegalAccessException, InstantiationException {
        HttpResponse<List<Map<String, Object>>> rep = new HttpResponse<List<Map<String, Object>>>();
        return rep.addData(checkInRecordService.querySummaryListToBySqlTotal(getCurrentHotleCode(), parse2Map(request)));
    }

    /**
     * 功能描述: <br>同住列表
     * 〈〉
     *
     * @Param: [roomLinkId]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2019/12/23 14:38
     */
    @GetMapping(value = "/checkInTogether")
    public HttpResponse checkInTogether(String orderNum, String guestRoomId) {
        HttpResponse hr = new HttpResponse();
        User user = getUser();
        List<CheckInRecord> list = checkInRecordService.checkInTogether(user.getHotelCode(), orderNum, guestRoomId);
        hr.setData(list);
        return hr;
    }

    /**
     * 功能描述: <br>同住列表（根据同住编码查询）暂时没用
     * 〈〉
     *
     * @Param: [orderNum]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2019/12/23 16:34
     */
    @GetMapping(value = "/togetherByCode")
    public HttpResponse togetherByCode(String orderNum) {
        HttpResponse hr = new HttpResponse();
        User user = getUser();
//		List<CheckInRecord> list = checkInRecordService.checkInTogether(user.getHotelCode(),orderNum);
//		hr.setData(list);
        return hr;
    }

    /**
     * 功能描述: <br>添加同住
     * 〈〉
     *
     * @Param: [orderNum, customerId]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2019/12/23 17:38
     */
    @GetMapping(value = "/addTogether")
    @OperationLog(remark = "添加同住")
    public HttpResponse addTogether(String orderNum, String customerId, String status, String guestRoomId) {
        HttpResponse hr = new HttpResponse();
        User user = getUser();
        hr = checkInRecordService.addTogether(user.getHotelCode(), orderNum, customerId, status, guestRoomId);
        return hr;
    }

    /**
     * 功能描述: <br>独担房价
     * 〈〉
     *
     * @Param: [orderNum, customerId, status]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2019/12/24 17:40
     */
    @GetMapping(value = "/roomPriceAllocation")
    @OperationLog(remark = "独担房价")
    public HttpResponse roomPriceAllocation(String orderNum, String checkInRecordId, String guestRoomId) {
        HttpResponse hr = new HttpResponse();
        User user = getUser();
        hr = checkInRecordService.roomPriceAllocation(user.getHotelCode(), orderNum, checkInRecordId, guestRoomId);
        return hr;
    }

    /**
     * 功能描述: <br>平均房价
     * 〈〉
     *
     * @Param: [orderNum, customerId, status]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2019/12/24 17:40
     */
    @GetMapping(value = "/roomPriceAvg")
    @OperationLog(remark = "平均房价")
    public HttpResponse roomPriceAvg(String orderNum, String guestRoomId) {
        HttpResponse hr = new HttpResponse();
        User user = getUser();
        hr = checkInRecordService.roomPriceAvg(user.getHotelCode(), orderNum, guestRoomId);
        return hr;
    }

    /**
     * 功能描述: <br>查询团队
     * 〈〉
     *
     * @Param: [arriveTime, leaveTime, name_, code_]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2020/1/9 14:27
     */
    @GetMapping(value = "/getGroup")
    public HttpResponse getGroup(String arriveTime, String leaveTime, String name_, String code_) {
        HttpResponse hr = new HttpResponse();
        User user = getUser();
        List<Map<String, Object>> list = checkInRecordService.getGroup(user.getHotelCode(), arriveTime, leaveTime, name_, code_);
        hr.addData(list);
        return hr.ok();
    }

    /**
     * 功能描述: <br>入团
     * 〈〉
     *
     * @Param:
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2020/1/9 17:19
     */
    @GetMapping(value = "/inGroup")
    public HttpResponse inGroup(String[] cir, String cirG, Boolean isFollowGroup) {
        HttpResponse hr = new HttpResponse();
        hr = checkInRecordService.inGroup(cir, cirG, isFollowGroup);
        return hr;
    }

    /**
     * 功能描述: <br>出团
     * 〈〉
     *
     * @Param: [cir, cirG]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2020/1/9 17:50
     */
    @GetMapping(value = "/outGroup")
    @OperationLog(remark = "出团")
    public HttpResponse outGroup(String[] cir, String cirG, Boolean isFollowGroup, String roomPriceId) {
        HttpResponse hr = new HttpResponse();
        hr = checkInRecordService.outGroup(cir, cirG, isFollowGroup, roomPriceId);
        return hr;
    }

    /**
     * 功能描述: <br>转团
     * 〈〉
     *
     * @Param: [cir, cirG, isFollowGroup]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2020/1/10 17:09
     */
    @GetMapping(value = "/updateGroup")
    @OperationLog(remark = "转团")
    public HttpResponse updateGroup(String[] cir, String cirG, String cirU, Boolean isFollowGroup) {
        HttpResponse hr = new HttpResponse();
        hr = checkInRecordService.updateGroup(cir, cirG, cirU, isFollowGroup);
        return hr;
    }

    /**
     * 功能描述: <br>新增预留
     * 〈〉
     * @Param: [checkInRecords]
     * @Return: com.kry.pms.base.HttpResponse<java.util.List<com.kry.pms.model.persistence.busi.CheckInRecord>>
     * @Author: huanghaibin
     * @Date: 2020/3/27 20:34
     */
    @PostMapping(path = "/reserve")
    @OperationLog(remark = "新增预留")
    public HttpResponse<List<CheckInRecord>> addReserve(@RequestBody List<CheckInRecord> checkInRecords) {
        HttpResponse<List<CheckInRecord>> rep = new HttpResponse<List<CheckInRecord>>();
        rep = checkInRecordService.addReserve(checkInRecords);
        return rep;

    }
    /**
     * 功能描述: <br>修改预留
     * 〈〉
     *
     * @Param: [checkInRecordListBo]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2020/1/14 11:28
     */
    @PostMapping(path = "/updateReserve")
    @OperationLog(remark = "修改预留")
    public HttpResponse updateReserve(@RequestBody CheckInRecord checkInRecord) {
        HttpResponse hr = new HttpResponse();
        hr = checkInRecordService.updateReserve(checkInRecord);
        return hr.ok();
    }

    /**
     * 功能描述: <br>单独查询房间布置（防止一次查询效率低问题）
     * 〈〉
     *
     * @Param: [checkInId]
     * @Return: com.kry.pms.base.HttpResponse<java.util.List < java.lang.String>>
     * @Author: huanghaibin
     * @Date: 2020/3/10 15:46
     */
    @GetMapping(path = "/getRoomLayout")
    public HttpResponse<List<String>> getRoomLayout(String checkInId) {
        HttpResponse<List<String>> rep = new HttpResponse<List<String>>();
        rep.addData(checkInRecordService.getRoomLayout(checkInId));
        return rep;
    }

    /**
     * 功能描述: <br>单独查询房间要求（防止一次查询效率低问题）
     * 〈〉
     *
     * @Param: [checkInId]
     * @Return: com.kry.pms.base.HttpResponse<java.util.List < java.lang.String>>
     * @Author: huanghaibin
     * @Date: 2020/3/10 15:47
     */
    @GetMapping(path = "/getRoomRequirement")
    public HttpResponse<List<String>> getRoomRequirement(String checkInId) {
        HttpResponse<List<String>> rep = new HttpResponse<List<String>>();
        rep.addData(checkInRecordService.getRoomRequirement(checkInId));
        return rep;
    }

    /**
     * 功能描述: <br>资源统计
     * 〈〉
     *
     * @Param: [orderNum, arriveTime, leaveTime]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2020/3/20 17:40
     */
    @GetMapping(value = "/resourceStatistics")
    public HttpResponse resourceStatistics(String orderNum, String arriveTime, String leaveTime) {
        HttpResponse hr = new HttpResponse();
        User user = getUser();
//        List<Map<String, Object>> list = checkInRecordService.resourceStatistics(orderNum, arriveTime, leaveTime);
        List<Map<String, Object>> list = checkInRecordService.resourceStatisticsTo(orderNum, arriveTime, leaveTime);
        hr.addData(list);
        return hr.ok();
    }

    /**
     * 功能描述: <br>只查询CheckInRecord对象，不查询外间关系的对象
     * 〈〉
     * @Param: [id]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2020/4/3 14:23
     */
    @GetMapping(value = "/byId")
    public HttpResponse byId(String id) {
        HttpResponse hr = new HttpResponse();
        CheckInRecord cir = checkInRecordService.byId(id);
        hr.addData(cir);
        return hr.ok();
    }

    /**
     * 功能描述: <br>主单打印要的数据
     * 〈〉
     * @Param: [checkInRecordId]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2020/4/16 16:59
     */
    @GetMapping(value = "/printing")
    public HttpResponse printing(String checkInRecordId) {
        HttpResponse hr = new HttpResponse();
        hr = checkInRecordService.printing(checkInRecordId);
        return hr.ok();
    }
}
