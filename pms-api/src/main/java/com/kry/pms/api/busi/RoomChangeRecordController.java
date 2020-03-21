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

	/**
	 * 功能描述: <br>换房
	 * 〈〉
	 * @Param: [roomChangeRecord]
	 * @Return: com.kry.pms.base.HttpResponse<com.kry.pms.model.persistence.busi.RoomChangeRecord>
	 * @Author: huanghaibin
	 * @Date: 2020/3/16 15:48
	 */
	@PostMapping
	public HttpResponse<RoomChangeRecord> add(@RequestBody  RoomChangeRecord roomChangeRecord) {
		HttpResponse hr = new HttpResponse();
		User user = getUser();
		if(user == null){
			return hr.loginError();
		}
		hr = roomChangeRecordService.save(user.getHotelCode(), roomChangeRecord);
		return hr;
	}

}
