package com.kry.pms.api.room;

import java.time.LocalDate;
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
import com.kry.pms.base.Constants;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.http.response.busi.RoomTypeQuantityPredictableVo;
import com.kry.pms.model.http.response.room.RoomTypeQuantityVo;
import com.kry.pms.model.persistence.room.RoomTypeQuantity;
import com.kry.pms.service.room.RoomTypeQuantityService;

@RestController
@RequestMapping(path = "/api/v1/room/roomTypeQuantity")
public class RoomTypeQuantityController extends BaseController<RoomTypeQuantity> {
	@Autowired
	RoomTypeQuantityService roomTypeQuantityService;

	@PostMapping
	public HttpResponse<RoomTypeQuantity> add(@RequestBody RoomTypeQuantity roomTypeQuantity) {
		return getDefaultResponse().addData(roomTypeQuantityService.add(roomTypeQuantity));
	}
	@GetMapping(path="/day")
	public HttpResponse<List<RoomTypeQuantityVo>> query(String startDate,String endDate){
		HttpResponse<List<RoomTypeQuantityVo>> rep = new HttpResponse<>();
		List<RoomTypeQuantityVo> data = roomTypeQuantityService.queryByDay(getCurrentHotleCode(),LocalDate.parse(startDate),LocalDate.parse(endDate));
		return rep.addData(data);
	}
	@PutMapping
	public HttpResponse<RoomTypeQuantity> modify(@RequestBody RoomTypeQuantity roomTypeQuantity) {
		return getDefaultResponse().addData(roomTypeQuantityService.modify(roomTypeQuantity));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		roomTypeQuantityService.delete(id);
		return rep;
	}

	@GetMapping(path = "/predictable")
	public HttpResponse<List<RoomTypeQuantityPredictableVo>> queryPredictable(String startDate, String endDate) {
		HttpResponse<List<RoomTypeQuantityPredictableVo>> response = new HttpResponse<List<RoomTypeQuantityPredictableVo>>();
		LocalDate sDate = LocalDate.parse(startDate);
		LocalDate eDate = LocalDate.parse(endDate);
		if (sDate.isBefore(LocalDate.now())) {
			sDate = LocalDate.now();
		}
		if (!eDate.isAfter(sDate)) {
			response.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
			response.setMessage("到离店时间错误，请重新选择");
		} else {
			List<RoomTypeQuantityPredictableVo> data = roomTypeQuantityService.queryPredictable(getCurrentHotleCode(),
					sDate, eDate);
			response.addData(data);
		}
		return response;
	}

	@GetMapping(path = "/predic/{id}")
	public HttpResponse<RoomTypeQuantityPredictableVo> queryPredic(@PathVariable("id") String roomTypeId,
			String startDate, String endDate) {
		HttpResponse<RoomTypeQuantityPredictableVo> response = new HttpResponse<RoomTypeQuantityPredictableVo>();
		LocalDate sDate = LocalDate.parse(startDate);
		LocalDate eDate = LocalDate.parse(endDate);
		if (sDate.isBefore(LocalDate.now())) {
			sDate = LocalDate.now();
		}
		if (!eDate.isAfter(sDate)) {
			response.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
			response.setMessage("到离店时间错误，请重新选择");
		} else {
			RoomTypeQuantityPredictableVo data = roomTypeQuantityService.queryPredic(getCurrentHotleCode(), roomTypeId,
					sDate, eDate);
			response.addData(data);
		}
		return response;
	}

	@GetMapping
	public HttpResponse<PageResponse<RoomTypeQuantity>> query(HttpServletRequest request)
			throws InstantiationException, IllegalAccessException {
		HttpResponse<PageResponse<RoomTypeQuantity>> rep = new HttpResponse<PageResponse<RoomTypeQuantity>>();
		PageRequest<RoomTypeQuantity> req = parse2PageRequest(request);
		return rep.addData(roomTypeQuantityService.listPage(req));
	}

}
