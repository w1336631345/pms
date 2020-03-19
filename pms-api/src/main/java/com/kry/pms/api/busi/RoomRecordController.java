package com.kry.pms.api.busi;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.kry.pms.model.persistence.sys.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.service.busi.RoomRecordService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/busi/roomRecord")
public class RoomRecordController extends BaseController<RoomRecord> {
	@Autowired
	RoomRecordService roomRecordService;
	@PostMapping
	public HttpResponse<RoomRecord> add(@RequestBody  RoomRecord roomRecord) {
		return getDefaultResponse().addData(roomRecordService.add(roomRecord));
	}

	@PutMapping
	public HttpResponse<RoomRecord> modify(@RequestBody RoomRecord roomRecord) {
		return getDefaultResponse().addData(roomRecordService.modify(roomRecord));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		roomRecordService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<RoomRecord>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<RoomRecord>> rep = new HttpResponse<PageResponse<RoomRecord>>();
		PageRequest<RoomRecord> req = parse2PageRequest(request);
		return rep.addData(roomRecordService.listPage(req));
	}

	/**
	 * 功能描述: <br>主单修改每日房价根据入住记录id查询的房间记录列表
	 * 〈〉
	 * @Param: [checkInRecordId]
	 * @Return: com.kry.pms.base.HttpResponse
	 * @Author: huanghaibin
	 * @Date: 2019/12/26 17:54
	 */
	@GetMapping(value = "/getByCheckInRecord")
	public HttpResponse getByCheckInRecord(String checkInRecordId){
		HttpResponse rep = new HttpResponse();
		User user = getUser();
		if(user == null){
			rep.loginError();
		}
		List<RoomRecord> list = roomRecordService.findByHotelCodeAndCheckInRecord(user.getHotelCode(), checkInRecordId);
		return rep.addData(list);
	}

	/**
	 * 功能描述: <br>根据时间和入住记录id查询当日房价
	 * 〈〉
	 * @Param: [recordDate, checkInRecordId]
	 * @Return: com.kry.pms.base.HttpResponse
	 * @Author: huanghaibin
	 * @Date: 2020/3/4 14:17
	 */
	@GetMapping(value = "/recordDateAndRoomPrice")
	public HttpResponse recordDateAndRoomPrice(String recordDate, String checkInRecordId){
		HttpResponse rep = new HttpResponse();
		Map<String, Object> map = roomRecordService.recordDateAndRoomPrice(recordDate, checkInRecordId);
		return rep.addData(map);
	}

	/**
	 * 功能描述: <br>查询对应记录(checkInRecord)某一天之后的所有记录
	 * 〈〉
	 * @Param: [recorDate, checkInRecordId]
	 * @Return: com.kry.pms.base.HttpResponse<java.util.List<com.kry.pms.model.persistence.busi.RoomRecord>>
	 * @Author: huanghaibin
	 * @Date: 2020/3/19 10:11
	 */
	@GetMapping(path = "/getByTimeAndCheckId")
	public HttpResponse<List<RoomRecord>> getByTimeAndCheckId(String recordDate, String checkInRecordId) throws InstantiationException, IllegalAccessException{
		HttpResponse<List<RoomRecord>> rep = new HttpResponse<List<RoomRecord>>();
		List<RoomRecord> list = roomRecordService.getByTimeAndCheckId(recordDate, checkInRecordId);
		rep.setData(list);
		return rep;
	}
}
