package com.kry.pms.api.room;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
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
import com.kry.pms.base.Constants;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.http.response.room.GuestRoomStatusVo;
import com.kry.pms.model.http.response.room.RoomStatusTableVo;
import com.kry.pms.model.persistence.room.GuestRoomStatus;
import com.kry.pms.service.room.GuestRoomStatusService;

@RestController
@RequestMapping(path = "/api/v1/room/guestRoomStatus")
public class GuestRoomStatusController extends BaseController<GuestRoomStatus> {
	@Autowired
	GuestRoomStatusService guestRoomStatusService;

	@PostMapping
	public HttpResponse<GuestRoomStatus> add(@RequestBody GuestRoomStatus guestRoomStatus) {
		return getDefaultResponse().addData(guestRoomStatusService.add(guestRoomStatus));
	}

	@PutMapping
	public HttpResponse<GuestRoomStatus> modify(@RequestBody GuestRoomStatus guestRoomStatus) {
		return getDefaultResponse().addData(guestRoomStatusService.modify(guestRoomStatus));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		guestRoomStatusService.delete(id);
		return rep;
	}

	@GetMapping(path = "/table")
	public HttpResponse<RoomStatusTableVo> table(HttpServletRequest request) {
		HttpResponse<RoomStatusTableVo> rep = new HttpResponse<>();
		rep.setData(guestRoomStatusService.table(getCurrentHotleCode()));
		return rep;
	}
	
	@GetMapping(path = "/detail/{id}")
	public HttpResponse<GuestRoomStatusVo> detail(@PathVariable("id")String id) {
		HttpResponse<GuestRoomStatusVo> rep = new HttpResponse<>();
		rep.setData(guestRoomStatusService.detail(id));
		return rep;
	}
	@GetMapping(path = "/detail/guestRoom/{id}")
	public HttpResponse<GuestRoomStatusVo> detailGuestRoom(@PathVariable("id")String id) {
		HttpResponse<GuestRoomStatusVo> rep = new HttpResponse<>();
		rep.setData(guestRoomStatusService.detailGuestRoom(id));
		return rep;
	}
	@GetMapping(path = "/detail/roomNum/{num}")
	public HttpResponse<GuestRoomStatusVo> detailGuestRoomNum(@PathVariable("num")String num) {
		HttpResponse<GuestRoomStatusVo> rep = new HttpResponse<>();
		GuestRoomStatusVo grsv = guestRoomStatusService.detailGuestRoomNum(num,getCurrentHotleCode());
		if(grsv==null) {
			rep.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
			rep.setMessage("??????????????????????????????");
		}else {			
			rep.setData(grsv);
		}
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<GuestRoomStatus>> query(HttpServletRequest request)
			throws InstantiationException, IllegalAccessException {
		HttpResponse<PageResponse<GuestRoomStatus>> rep = new HttpResponse<PageResponse<GuestRoomStatus>>();
		PageRequest<GuestRoomStatus> req = parse2PageRequest(request);
		return rep.addData(guestRoomStatusService.listPage(req));
	}

}
