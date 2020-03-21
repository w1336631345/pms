package com.kry.pms.api.room;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.http.response.room.RoomUsageListVo;
import com.kry.pms.model.http.response.room.RoomUsageVo;
import com.kry.pms.model.persistence.room.RoomUsage;
import com.kry.pms.service.room.RoomUsageService;
import com.kry.pms.util.DateTimeUtil;

@RestController
@RequestMapping(path = "/api/v1/room/roomUsage")
public class RoomUsageController extends BaseController<RoomUsage> {
	@Autowired
	RoomUsageService roomUsageService;

	@PostMapping
	public HttpResponse<RoomUsage> add(@RequestBody RoomUsage roomUsage) {
		return getDefaultResponse().addData(roomUsageService.add(roomUsage));
	}

	@PutMapping
	public HttpResponse<RoomUsage> modify(@RequestBody RoomUsage roomUsage) {
		return getDefaultResponse().addData(roomUsageService.modify(roomUsage));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		roomUsageService.delete(id);
		return rep;
	}
	@GetMapping(path="/roomType/{id}")
	public HttpResponse<List<RoomUsageListVo>> queryUsableGuestRooms(@PathVariable("id")String roomTypeId, String startTime, String endTime) {
		HttpResponse<List<RoomUsageListVo>> rep = new HttpResponse<List<RoomUsageListVo>>();
		rep.addData(roomUsageService.queryUsableGuestRooms(roomTypeId, DateTimeUtil.parse(startTime),
				DateTimeUtil.parse(endTime)));
		return rep;
	}
	/**
	 * 功能描述: <br>换房：房间空闲查询-与上述方法比较（当前时间-离店时间）
	 * 〈〉
	 * @Param: [roomTypeId, startTime, endTime]
	 * @Return: com.kry.pms.base.HttpResponse<java.util.List<com.kry.pms.model.http.response.room.RoomUsageListVo>>
	 * @Author: huanghaibin
	 * @Date: 2020/3/21 15:07
	 */
	@GetMapping(path="/roomTypeTo/{id}")
	public HttpResponse<List<RoomUsageListVo>> queryUsableGuestRoomsTo(@PathVariable("id")String roomTypeId, String startTime, String endTime) {
		HttpResponse<List<RoomUsageListVo>> rep = new HttpResponse<List<RoomUsageListVo>>();
		rep.addData(roomUsageService.queryUsableGuestRoomsTo(roomTypeId, DateTimeUtil.parse(startTime),
				DateTimeUtil.parse(endTime)));
		return rep;
	}
	@GetMapping(path = "/bookItem/{id}")
	public HttpResponse<List<RoomUsageListVo>> queryUsableGuestRoomsByBookItemId(@PathVariable("id")String bookItemId) {
		HttpResponse<List<RoomUsageListVo>> rep = new HttpResponse<List<RoomUsageListVo>>();
		rep.addData(roomUsageService.queryUsableGuestRoomsByBookItemId(bookItemId));
		return rep;
	}
	@GetMapping(path = "/checkInRecord/{id}")
	public HttpResponse<List<RoomUsageListVo>> queryUsableGuestRoomsByCheckInRecordId(@PathVariable("id")String checkInRecordId,String roomTypeId,String roomNum) {
		HttpResponse<List<RoomUsageListVo>> rep = new HttpResponse<List<RoomUsageListVo>>();
		rep.addData(roomUsageService.queryUsableGuestRoomsByCheckInRecordId(checkInRecordId,roomTypeId,roomNum));
		return rep;
	}
	
	
	

	@GetMapping
	public HttpResponse<PageResponse<RoomUsage>> query(HttpServletRequest request)
			throws InstantiationException, IllegalAccessException {
		HttpResponse<PageResponse<RoomUsage>> rep = new HttpResponse<PageResponse<RoomUsage>>();
		PageRequest<RoomUsage> req = parse2PageRequest(request);
		return rep.addData(roomUsageService.listPage(req));
	}

}
