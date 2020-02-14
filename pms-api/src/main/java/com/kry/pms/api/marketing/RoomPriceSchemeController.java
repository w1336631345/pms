package com.kry.pms.api.marketing;

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
import com.kry.pms.model.http.response.marketing.RoomPriceSchemeVo;
import com.kry.pms.model.persistence.marketing.RoomPriceScheme;
import com.kry.pms.service.marketing.RoomPriceSchemeService;

@RestController
@RequestMapping(path = "/api/v1/marketing/roomPriceScheme")
public class RoomPriceSchemeController extends BaseController<RoomPriceScheme> {
	@Autowired
	RoomPriceSchemeService roomPriceSchemeService;

	@PostMapping
	public HttpResponse<RoomPriceScheme> add(@RequestBody RoomPriceScheme roomPriceScheme) {
		return getDefaultResponse().addData(roomPriceSchemeService.add(roomPriceScheme));
	}

	@PutMapping
	public HttpResponse<RoomPriceScheme> modify(@RequestBody RoomPriceScheme roomPriceScheme) {
		return getDefaultResponse().addData(roomPriceSchemeService.modify(roomPriceScheme));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		roomPriceSchemeService.delete(id);
		return rep;
	}

	@GetMapping(path = "/corpation/{corpId}")
	public HttpResponse<List<RoomPriceSchemeVo>> corpation(@PathVariable("corpId") String corpId) {
		HttpResponse<List<RoomPriceSchemeVo>> rep = new HttpResponse<>();
		List<RoomPriceSchemeVo> data = roomPriceSchemeService.findCorpationScheme(getCurrentHotleCode(), corpId);
		rep.addData(data);
		return rep;
	}
	@GetMapping(path = "/default")
	public HttpResponse<List<RoomPriceSchemeVo>> corpation() {
		HttpResponse<List<RoomPriceSchemeVo>> rep = new HttpResponse<>();
		List<RoomPriceSchemeVo> data = roomPriceSchemeService.findDefaultScheme(getCurrentHotleCode());
		rep.addData(data);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<RoomPriceScheme>> query(HttpServletRequest request)
			throws InstantiationException, IllegalAccessException {
		HttpResponse<PageResponse<RoomPriceScheme>> rep = new HttpResponse<PageResponse<RoomPriceScheme>>();
		PageRequest<RoomPriceScheme> req = parse2PageRequest(request);
		return rep.addData(roomPriceSchemeService.listPage(req));
	}

	@GetMapping(path = "/byCorpId")
	public HttpResponse<List<RoomPriceScheme>> getByCorpation(String protocolCId) {
		HttpResponse<List<RoomPriceScheme>> rep = new HttpResponse<>();
		List<RoomPriceScheme> data = roomPriceSchemeService.getByCorpation(protocolCId);
		rep.addData(data);
		return rep;
	}

}
