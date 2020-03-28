package com.kry.pms.api.guest;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.guest.MemberRoomType;
import com.kry.pms.service.guest.MemberRoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/guest/memberRoomType")
public class MemberRoomTypeController extends BaseController<MemberRoomType> {
	@Autowired
	MemberRoomTypeService memberRoomTypeService;
	@PostMapping
	public HttpResponse<MemberRoomType> add(@RequestBody MemberRoomType vipRoomType) {
		vipRoomType.setHotelCode(getCurrentHotleCode());
		return getDefaultResponse().addData(memberRoomTypeService.add(vipRoomType));
	}

	@PutMapping
	public HttpResponse<MemberRoomType> modify(@RequestBody MemberRoomType memberRoomType) {
		return getDefaultResponse().addData(memberRoomTypeService.modify(memberRoomType));
	}

	@DeleteMapping
	public HttpResponse<String> delete(String id) {
		HttpResponse<String> rep = new HttpResponse<>();
		memberRoomTypeService.delete(id);
		return rep;
	}

	@GetMapping
	public HttpResponse<PageResponse<MemberRoomType>> query(HttpServletRequest request) throws InstantiationException, IllegalAccessException{
		HttpResponse<PageResponse<MemberRoomType>> rep = new HttpResponse<PageResponse<MemberRoomType>>();
		PageRequest<MemberRoomType> req = parse2PageRequest(request);
		return rep.addData(memberRoomTypeService.listPage(req));
	}

	@GetMapping(path = "/list")
	public HttpResponse<List<MemberRoomType>> getByHotelCode(HttpServletRequest request){
		HttpResponse<List<MemberRoomType>> rep = new HttpResponse<List<MemberRoomType>>();
		List<MemberRoomType> list = memberRoomTypeService.getAllByHotelCode(getCurrentHotleCode());
		return rep.addData(list);
	}

}
