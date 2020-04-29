package com.kry.pms.api.busi;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.Constants;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.annotation.OperationLog;
import com.kry.pms.model.http.request.busi.RoomLinkBo;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.busi.RoomLink;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.busi.CheckInRecordService;
import com.kry.pms.service.busi.RoomLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/busi/roomLink")
public class RoomLinkController extends BaseController<RoomLink> {

    @Autowired
    RoomLinkService roomLinkService;
    @Autowired
    CheckInRecordService checkInRecordService;

    @PostMapping(value = "/add")
    public HttpResponse add(@RequestBody RoomLink roomLink){
        HttpResponse hr = new HttpResponse();
        RoomLink rl = roomLinkService.add(roomLink);
        return hr;
    }

    /**
     * 功能描述: <br>添加联房
     * 〈〉
     * @Param: [roomLinkBo]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2019/12/19 16:02
     */
    @PostMapping(value = "/addRoomLink")
    @OperationLog(remark = "添加联房")
    public HttpResponse addRoomLink(@RequestBody RoomLinkBo roomLinkBo){
        HttpResponse hr = roomLinkService.addRoomLink(roomLinkBo);
        return hr;
    }

    /**
     * 功能描述: <br>查询可联房
     * 〈〉
     * @Param: [pageNum, pageSize, name, roomNum]
     * @Return: com.kry.pms.base.HttpResponse<com.kry.pms.base.PageResponse<com.kry.pms.model.persistence.busi.CheckInRecord>>
     * @Author: huanghaibin
     * @Date: 2019/12/19 14:42
     */
    @GetMapping(value = "/hRoomLink")
    public HttpResponse<PageResponse<CheckInRecord>> getHRoomLink(@RequestParam(value = "pageNum", defaultValue = "1")Integer pageNum,
                                                                  @RequestParam(value = "pageSize", defaultValue = "20")Integer pageSize,
                                                                  String name, String roomNum, String groupTyp, String corp, String status, String account){
        HttpResponse<PageResponse<CheckInRecord>> rep = new HttpResponse<>();
        User user = getUser();
        PageResponse<CheckInRecord> page = checkInRecordService.getNRoomLink(pageNum, pageSize,name, roomNum, user.getHotelCode(), groupTyp, corp, status, account);
        rep.addData(page);
        return rep;

    }

    /**
     * 功能描述: <br>查询已经联房的数据
     * 〈〉
     * @Param: [roomLinkId]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2019/12/19 15:28
     */
    @GetMapping(value = "/roomLinkListTo")
    public HttpResponse roomLinkListTo(String id, String orderNum){
        HttpResponse hr = new HttpResponse();
        List<CheckInRecord> list = checkInRecordService.getRoomLinkListTo(id, orderNum);
        hr.setData(list);
        return hr;
    }

    /**
     * 功能描述: <br>删除联房
     * 〈〉
     * @Param: [ids]
     * @Return: com.kry.pms.base.HttpResponse
     * @Author: huanghaibin
     * @Date: 2019/12/20 15:42
     */
    @PostMapping(path = "/deleteRoomLink")
    public HttpResponse deleteRoomLink(@RequestBody String[] ids) {
        HttpResponse hr = roomLinkService.deleteRoomLink(ids);
        return hr;
    }


}
