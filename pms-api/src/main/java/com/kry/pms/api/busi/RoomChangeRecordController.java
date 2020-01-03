package com.kry.pms.api.busi;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.model.persistence.busi.RoomChangeRecord;
import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.busi.RoomChangeRecordService;
import com.kry.pms.service.busi.RoomRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/api/v1/busi/roomChangeRecord")
public class RoomChangeRecordController extends BaseController<RoomChangeRecord> {
	@Autowired
	RoomChangeRecordService roomChangeRecordService;
	@PostMapping
	public HttpResponse<RoomChangeRecord> add(@RequestBody  RoomChangeRecord roomChangeRecord) {
		HttpResponse hr = new HttpResponse();
		User user = getUser();
		if(user == null){
			return hr.loginError();
		}
		return hr.addData(roomChangeRecordService.save(user.getHotelCode(), roomChangeRecord));
	}

}
