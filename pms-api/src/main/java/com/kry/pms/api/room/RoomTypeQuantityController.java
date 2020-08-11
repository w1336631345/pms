package com.kry.pms.api.room;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import freemarker.template.TemplateException;
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
	@GetMapping(path = "/recount")
	public HttpResponse<String> recount(){
		HttpResponse<String> rep = new HttpResponse<>();
		roomTypeQuantityService.recount(getCurrentHotleCode());
		rep.setMessage("房类资源重算完成");
		return rep;
	}


	@GetMapping(path="/day")
	public HttpResponse<List<RoomTypeQuantityVo>> query(String startDate,String endDate){
		HttpResponse<List<RoomTypeQuantityVo>> rep = new HttpResponse<>();
		List<RoomTypeQuantityVo> data = roomTypeQuantityService.queryByDay(getCurrentHotleCode(),LocalDate.parse(startDate),LocalDate.parse(endDate));
		return rep.addData(data);
	}
	/**
	 * 功能描述: <br>首页房态实时统计
	 * 〈〉
	 * @Param: [dateTime]
	 * @Return: com.kry.pms.base.HttpResponse<java.util.List<com.kry.pms.model.http.response.room.RoomTypeQuantityVo>>
	 * @Author: huanghaibin
	 * @Date: 2020/8/11 15:48
	 */
	@GetMapping(path="/homeRoom")
	public HttpResponse<List<RoomTypeQuantityVo>> query(String dateTime){
		HttpResponse<List<RoomTypeQuantityVo>> rep = new HttpResponse<>();
		List<RoomTypeQuantityVo> data = roomTypeQuantityService.queryByDay2(getCurrentHotleCode(),LocalDate.parse(dateTime));
		return rep.addData(data);
	}
	@GetMapping(path="/today")
	public HttpResponse<List<RoomTypeQuantityVo>> today(){
		HttpResponse<List<RoomTypeQuantityVo>> rep = new HttpResponse<>();
		List<RoomTypeQuantityVo> data = roomTypeQuantityService.queryOneDay(getCurrentHotleCode(),LocalDate.now());
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
	public HttpResponse<List<RoomTypeQuantityPredictableVo>> queryPredictable(String startDate, String endDate, String roomPriceSchemeId) {
		HttpResponse<List<RoomTypeQuantityPredictableVo>> response = new HttpResponse<List<RoomTypeQuantityPredictableVo>>();
		LocalDate sDate = LocalDate.parse(startDate);
		LocalDate eDate = LocalDate.parse(endDate);
//		if (sDate.isBefore(LocalDate.now())) {
//			sDate = LocalDate.now();
//		}
		if (!eDate.isAfter(sDate)) {
			response.setStatus(Constants.BusinessCode.CODE_PARAMETER_INVALID);
			response.setMessage("到离店时间错误，请重新选择");
		} else {
			List<RoomTypeQuantityPredictableVo> data = roomTypeQuantityService.queryPredictable(getCurrentHotleCode(),
					sDate, eDate, roomPriceSchemeId);
			response.addData(data);
		}
		return response;
	}

	/**
	 * 功能描述: <br>微信小程序可预订列表查询
	 * 〈〉
	 * @Param: [startDate, endDate]
	 * @Return: com.kry.pms.base.HttpResponse<java.util.List<com.kry.pms.model.http.response.busi.RoomTypeQuantityPredictableVo>>
	 * @Author: huanghaibin
	 * @Date: 2020/5/12 16:04
	 */
	@GetMapping(path = "/predictableWchat")
	public HttpResponse<List<RoomTypeQuantityPredictableVo>> predictableWchat(String startDate, String endDate) {
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
			List<RoomTypeQuantityPredictableVo> data = roomTypeQuantityService.predictableWchat(getCurrentHotleCode(), sDate, eDate);
			response.addData(data);
		}
		return response;
	}

	@GetMapping(path = "/predic/{id}")
	public HttpResponse<RoomTypeQuantityPredictableVo> queryPredic(@PathVariable("id") String roomTypeId,
			String startDate, String endDate, String roomPriceSchemeId) {
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
					sDate, eDate, roomPriceSchemeId);
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

	@GetMapping(path="/roomPriceByShecme")
	public HttpResponse<List<Map<String, Object>>> roomPriceByShecme(String times, String roomTypeId){
		HttpResponse<List<Map<String, Object>>> rep = new HttpResponse<>();
		List<Map<String, Object>> data = roomTypeQuantityService.getByTimeAndRoomType(getCurrentHotleCode(),times, roomTypeId);
		return rep.addData(data);
	}

	@GetMapping(path="/roomPriceByShecme2")
	public HttpResponse<List<Map<String, Object>>> roomPriceByShecme2(String startTimes, String[] buildIds, String[] roomTypeIds){
		HttpResponse<List<Map<String, Object>>> rep = new HttpResponse<>();
		List<Map<String, Object>> data = roomTypeQuantityService.getByTimeAndRoomType2(getCurrentHotleCode(),startTimes, buildIds, roomTypeIds);
		return rep.addData(data);
	}

	/**
	 * 功能描述: <br>房类资源点击数字的详情列表
	 * 〈〉
	 * @Param: [request]
	 * @Return: com.kry.pms.base.HttpResponse<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
	 * @Author: huanghaibin
	 * @Date: 2020/6/10 14:58
	 */
	@GetMapping(path = "/resourcesInfo")
	public HttpResponse<List<Map<String, Object>>> resourcesInfo(HttpServletRequest request) throws IOException, TemplateException {
		HttpResponse<List<Map<String, Object>>> rep = new HttpResponse<List<Map<String, Object>>>();
		return rep.addData(roomTypeQuantityService.resourcesInfo(getCurrentHotleCode(), parse2Map(request)));
	}

}
