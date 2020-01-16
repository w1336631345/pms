package com.kry.pms.api.busi;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.model.persistence.busi.Arrangement;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.busi.ArrangementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/busi/arrangement")
public class ArrangementController extends BaseController<Arrangement> {
	@Autowired
	ArrangementService arrangementService;

	@GetMapping(path = "/list")
	public HttpResponse<List<Arrangement>> queryByGroupId(String id) {
		HttpResponse<List<Arrangement>> rep = new HttpResponse<List<Arrangement>>();
		User user =getUser();
		List<Arrangement> data = arrangementService.getAllByHotelCode(user.getHotelCode());
		rep.setData(data);
		return rep;
	}
}
