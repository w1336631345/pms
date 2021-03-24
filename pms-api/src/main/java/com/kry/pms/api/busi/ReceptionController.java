package com.kry.pms.api.busi;

import com.kry.pms.api.BaseController;
import com.kry.pms.base.DtoResponse;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.dao.busi.SettleAccountRecordDao;
import com.kry.pms.model.annotation.OperationLog;
import com.kry.pms.model.http.request.busi.*;
import com.kry.pms.model.http.response.busi.AccountSummaryVo;
import com.kry.pms.model.persistence.busi.BookingRecord;
import com.kry.pms.model.persistence.busi.SettleAccountRecord;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.service.busi.ReceptionService;
import com.kry.pms.service.org.EmployeeService;
import com.kry.pms.service.room.GuestRoomService;
import com.kry.pms.service.sys.AccountService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

/**
 * 预定接待
 *
 * @author Louis
 *
 */
@RestController
@RequestMapping(path = "/api/v1/busi/reception")
public class ReceptionController extends BaseController<String> {
	@Autowired
	ReceptionService receptionService;
	@Autowired
	GuestRoomService guestRoomService;
	@Autowired
	EmployeeService employeeService;
	@Autowired
	AccountService accountService;
	@Autowired
	SettleAccountRecordDao settleAccountRecordDao;

	/**
	 * 入住
	 *
	 * @param checkIn
	 * @return
	 */
	@PostMapping(path = "/in")
	public HttpResponse<String> checkIn(@RequestBody @Valid CheckInBo checkIn) {
		HttpResponse<String> rep = new HttpResponse<String>();
		BeanUtils.copyProperties(receptionService.checkIn(checkIn), rep);
		return rep;
	}

	/**
	 * 出
	 *
	 * @param
	 * @return
	 */
	@PostMapping(path = "/checkout")
	public HttpResponse<String> checkOut(@RequestBody @Valid CheckOutBo checkOutIn) {
		HttpResponse<String> rep = new HttpResponse<String>();
		return rep;
	}

	/**
	 * 功能描述: <br>批量入住
	 * 〈〉
	 * @Param: [ids]
	 * @Return: com.kry.pms.base.HttpResponse
	 * @Author: huanghaibin
	 * @Date: 2019/12/17 15:52
	 */
	@PostMapping(path = "/checkInAll")
//	@OperationLog(remark = "批量入住")
	public HttpResponse checkIn(@RequestBody String[] ids) {
		HttpResponse<String> rep = new HttpResponse<String>();
		BeanUtils.copyProperties(receptionService.checkInAll(ids,getUser()), rep);
		return rep;
	}
	/**
	 * 功能描述: <br>入住（在用）
	 * 〈〉
	 * @Param: [id]
	 * @Return: com.kry.pms.base.HttpResponse<java.lang.String>
	 * @Author: huanghaibin
	 * @Date: 2020/2/28 10:42
	 */
	@GetMapping(path = "/checkIn/{id}")
	@OperationLog(remark = "入住")
	public HttpResponse<String> checkIn(@PathVariable String id) {
		HttpResponse<String> rep = new HttpResponse<String>();
		BeanUtils.copyProperties(receptionService.checkInM(id, getUser()), rep);
		return rep;
	}

	/**
	 * 功能描述: <br>获取重新入住房间状态
	 * 〈〉
	 * @Param: [id]
	 * @Return: com.kry.pms.base.HttpResponse<java.lang.String>
	 * @Author: huanghaibin
	 * @Date: 2020/8/5 10:14
	 */
	@GetMapping(path = "/getRoomStatus")
	public HttpResponse<String> getRoomStatus(String guestRoomId) {
		HttpResponse<String> rep = new HttpResponse<String>();
		rep = receptionService.getRoomStatus(guestRoomId);
		return rep;
	}
	/**
	 * 功能描述: <br>重新入住
	 * 〈〉
	 * @Param: [guestRoomId]
	 * @Return: com.kry.pms.base.HttpResponse<java.lang.String>
	 * @Author: huanghaibin
	 * @Date: 2020/8/5 10:15
	 */
	@GetMapping(path = "/overCheckId")
	public HttpResponse overCheckId(String cirId) {
		HttpResponse rep = new HttpResponse();
		rep = receptionService.overCheckId(cirId);
		return rep;
	}

	@GetMapping(path="/account/summary/{id}")
	public HttpResponse<AccountSummaryVo> getAccountSummary(@PathVariable String id){
		HttpResponse<AccountSummaryVo> rep = new HttpResponse<>();
		AccountSummaryVo accountSummaryVo = receptionService.getAccountSummaryByCheckRecordId(id);
		initActualLeaveTime(accountSummaryVo);
		rep.setData(accountSummaryVo);
		return rep;
	}


	/**
	 * @desc: 因为AccountSummaryVo 是嵌套的，所以说递归去初始化离店时间（不为空设为当前时间）
	 * @author: WangXinHao
	 * @date: 2021/3/18 0018 15:09
	 */
	public  void initActualLeaveTime(AccountSummaryVo vo){
	    if (null == vo){
	        return;
        }

		if (null == vo.getActualTimeOfLeave()){    //先初始化当前元素
			vo.setActualTimeOfLeave(LocalDateTime.now());
		}

		// 再初始化一下每个账号的最后结账时间
		SettleAccountRecord sar = settleAccountRecordDao.findLastSettleRecord(getCurrentHotleCode(),vo.getId());
		if (null != sar){
			vo.setLastSettleTime(sar.getSettleTime());
		}

	    if (null == vo.getChildren() || 0 == vo.getChildren().size()){  //没有子元素了
			return;
        }else{   //否则遍历递归下去
			for (AccountSummaryVo aVo : vo.getChildren()){
				initActualLeaveTime(aVo);
			}
		}
    }


	@GetMapping(path="/account/summary/code/{code}")
	public HttpResponse<AccountSummaryVo> getAccountSummaryByCode(@PathVariable String code){
		HttpResponse<AccountSummaryVo> rep = new HttpResponse<>();
		AccountSummaryVo accountSummaryVo = receptionService.getAccountSummaryByAccountCode(getCurrentHotleCode(),code);
		rep.setData(accountSummaryVo);
		return rep;
	}

	/**
	 * 结账确认
	 * @return
	 */
	@GetMapping(path="/bill/check/confirm/group/{id}")
	@OperationLog(remark = "结账确认")
	public HttpResponse<List<AccountSummaryVo>> groupCheckBillConfirm(@PathVariable String id){
		HttpResponse<List<AccountSummaryVo>> rep = new HttpResponse<List<AccountSummaryVo>>();
		DtoResponse<List<AccountSummaryVo>> data = receptionService.groupCheckBillConfirm(id);
		BeanUtils.copyProperties(data, rep);
		rep.setData(data.getData());
		return rep;
	}
	/**
	 * 宾客结账
	 *
	 * @return
	 */
	@PostMapping(path="/bill/check")
//	@OperationLog(remark = "宾客结账")
	public HttpResponse customerCheckBill(@RequestBody BillCheckBo	billCheckBo){
		billCheckBo.setHotelCode(getCurrentHotleCode());
		billCheckBo.setShiftCode(getShiftCode());
		billCheckBo.setOperationEmployee(getCurrentEmployee());
		HttpResponse rep = new HttpResponse();
		DtoResponse<Account> data = accountService.checkCustomerBill(billCheckBo);
		rep.setMessage(data.getMessage());
		rep.setStatus(data.getStatus());
		rep.setCode(data.getCode());
//		rep.setSuccess(data.success);
//		BeanUtils.copyProperties(data, rep);
		if(data.getData()!=null){
			if(data.getStatus() == 0){
				rep.setData(new AccountSummaryVo(data.getData()));
			}else{
				rep.setMessage(data.getMessage());
//				rep.setData(data.getData());
			}
		}
		return rep;
	}

	/**
	 * 排房
	 *
	 * @param
	 * @return
	 */
	@PostMapping(path = "/assign")
	@OperationLog(remark = "排房")
	public HttpResponse<String> assignRoom(@RequestBody @Valid RoomAssignBo roomAssignBo) {
		HttpResponse<String> rep = new HttpResponse<String>();
		BeanUtils.copyProperties(receptionService.assignRoom(roomAssignBo), rep);
		return rep;
	}

	/**
	 * 预定
	 *
	 * @param
	 * @return
	 */
	@PostMapping(path = "/newBook")
	public HttpResponse<String> book(@RequestBody BookingRecord br) {
		br.setHotelCode(getCurrentHotleCode());
		br.setOperationEmployee(getCurrentEmployee());
		DtoResponse<BookingRecord> dtoRep = receptionService.groupBook(br);
		HttpResponse<String> rep = new HttpResponse<String>(dtoRep);
		return rep;
	}

	/**
	 * 续住
	 *
	 * @param renew
	 * @return
	 */
	@PostMapping(path = "/renew")
	public HttpResponse<String> renew(@RequestBody RenewBo renew) {
		HttpResponse<String> rep = new HttpResponse<String>();
		return rep;
	}

}
