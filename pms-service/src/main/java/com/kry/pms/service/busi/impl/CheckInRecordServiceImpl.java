package com.kry.pms.service.busi.impl;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import com.kry.pms.model.persistence.busi.Arrangement;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.DtoResponse;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.base.ParamSpecification;
import com.kry.pms.dao.busi.CheckInRecordDao;
import com.kry.pms.dao.busi.RoomLinkDao;
import com.kry.pms.model.http.request.busi.CheckInBo;
import com.kry.pms.model.http.request.busi.CheckInItemBo;
import com.kry.pms.model.http.request.busi.CheckInRecordListBo;
import com.kry.pms.model.http.request.busi.CheckUpdateItemTestBo;
import com.kry.pms.model.http.request.busi.TogetherBo;
import com.kry.pms.model.http.response.busi.AccountSummaryVo;
import com.kry.pms.model.http.response.busi.CheckInRecordListVo;
import com.kry.pms.model.other.wrapper.CheckInRecordWrapper;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.busi.RoomLink;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.model.persistence.room.GuestRoom;
import com.kry.pms.model.persistence.room.RoomTag;
import com.kry.pms.model.persistence.room.RoomUsage;
import com.kry.pms.model.persistence.sys.Account;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.busi.CheckInRecordService;
import com.kry.pms.service.busi.RoomRecordService;
import com.kry.pms.service.guest.CustomerService;
import com.kry.pms.service.room.GuestRoomService;
import com.kry.pms.service.room.GuestRoomStatusService;
import com.kry.pms.service.room.RoomStatisticsService;
import com.kry.pms.service.room.RoomStatusQuantityService;
import com.kry.pms.service.room.RoomTypeQuantityService;
import com.kry.pms.service.room.RoomTypeService;
import com.kry.pms.service.room.RoomUsageService;
import com.kry.pms.service.sys.AccountService;
import com.kry.pms.service.sys.BusinessSeqService;
import com.kry.pms.service.sys.SystemConfigService;
import com.kry.pms.service.util.UpdateUtil;

@Service
public class CheckInRecordServiceImpl implements CheckInRecordService {
	@Autowired
	CheckInRecordDao checkInRecordDao;
	@Autowired
	RoomRecordService roomRecordService;
	@Autowired
	GuestRoomService guestRoomService;
	@Autowired
	CustomerService customerService;
	@Autowired
	RoomStatisticsService roomStatisticsService;
	@Autowired
	SystemConfigService systemConfigService;
	@Autowired
	RoomUsageService roomUsageService;
	@Autowired
	BusinessSeqService businessSeqService;
	@Autowired
	RoomTypeService roomTypeService;
	@Autowired
	AccountService accountService;
	@Autowired
	RoomLinkDao roomLinkDao;

	@Override
	public CheckInRecord add(CheckInRecord checkInRecord) {
		return checkInRecordDao.saveAndFlush(checkInRecord);
	}

	@Override
	public void delete(String id) {
		CheckInRecord checkInRecord = checkInRecordDao.findById(id).get();
		if (checkInRecord != null) {
			checkInRecord.setDeleted(Constants.DELETED_TRUE);
		}
		modify(checkInRecord);
	}

	@Override
	public CheckInRecord modify(CheckInRecord checkInRecord) {
		CheckInRecord dbCir = findById(checkInRecord.getId());
		if (dbCir != null) {
			checkInRecord.setMainRecord(dbCir.getMainRecord());
			if (checkInRecord.getCustomer() != null) {
				Customer customer = customerService.modify(checkInRecord.getCustomer());
				checkInRecord.setCustomer(customer);
			}
			return checkInRecordDao.saveAndFlush(checkInRecord);
		}
		return null;
	}

	@Override
	@Transactional
	public HttpResponse modifyInfo(CheckInRecord checkInRecord) {
		HttpResponse hr = new HttpResponse();
		CheckInRecord dbCir = checkInRecordDao.getOne(checkInRecord.getId());
		if (dbCir != null) {
			checkInRecord.setMainRecord(dbCir.getMainRecord());
			updateCustomer(dbCir, checkInRecord);
			// 如果是主单操作，判断是不是修改的时间
			if (("G").equals(dbCir.getType())) {
				if (checkInRecord.getIsUpdateTime()) {
					// 修改的离店时间小于之前时间（改小）
					if (checkInRecord.getLeaveTime().isBefore(dbCir.getLeaveTime())) {
						// 释放资源
						// ...释放资源代码
						// ...
					} else {// 改大
							// 查询主单下的成员记录
						List<CheckInRecord> children = checkInRecordDao.findByMainRecordAndDeleted(dbCir,
								Constants.DELETED_FALSE);
						for (int i = 0; i < children.size(); i++) {
							CheckInRecord cir = children.get(i);
							if (cir.getRoomType() != null) {
								// 查询房类资源是否满足
//								boolean isExit = roomStatisticsService.booking(cir.getRoomType(), cir.getArriveTime(),
//										cir.getRoomCount(), cir.getDays());
//								if (!isExit) {
//									TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//									return hr.error("房类资源不够");
//								}
							}
							cir.setArriveTime(checkInRecord.getArriveTime());
							cir.setLeaveTime(checkInRecord.getLeaveTime());
							checkInRecordDao.saveAndFlush(cir);
						}
					}
				}
			}
			hr.addData(checkInRecordDao.saveAndFlush(checkInRecord));
		}
		return hr.ok();
	}

	private void updateCustomer(CheckInRecord dbCir, CheckInRecord cir) {
		if (cir.getGuestRoom() != null && cir.getCustomer() != null) {
			if (dbCir.getCustomer() != null && dbCir.getCustomer().getId().equals(cir.getCustomer().getId())) {
//				guestRoomStatausService.updateSummary(cir.getGuestRoom(), dbCir.getCustomer().getName(),
//						cir.getCustomer().getName());
			}
		}
	}

	@Override
	public CheckInRecord update(CheckInRecord checkInRecord) {
		return checkInRecordDao.save(checkInRecord);
	}

	@Override
	public CheckInRecord updateAll(CheckUpdateItemTestBo checkUpdateItemTestBo) {
		String[] ids = checkUpdateItemTestBo.getIds();
		for (int i = 0; i < ids.length; i++) {
			CheckInRecord cir = findById(ids[i]);
			UpdateUtil.copyNullProperties(checkUpdateItemTestBo, cir);
//			modify(cir);
			checkInRecordDao.save(cir);
		}
		return null;
	}
	@Transactional
	@Override
	public HttpResponse cancelIn(String[] ids) {
		HttpResponse hr = new HttpResponse();
		for (int i = 0; i < ids.length; i++) {
			CheckInRecord cir = findById(ids[i]);
			cir.setStatus("R");
			checkInRecordDao.save(cir);
			roomStatisticsService.cancleCheckIn(new CheckInRecordWrapper(cir));
		}
		return hr;
	}

	@Override
	public CheckInRecord findById(String id) {
		return checkInRecordDao.getOne(id);
	}

	@Override
	public List<CheckInRecord> getAllByHotelCode(String code) {
		return null;// 默认不实现
		// return checkInRecordDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<CheckInRecord> listPage(PageRequest<CheckInRecord> prq) {
		Example<CheckInRecord> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(checkInRecordDao.findAll(ex, req));
	}

	@Override
	public void checkIn(CheckInBo checkInBo, DtoResponse<List<CheckInRecord>> rep) {
		for (CheckInItemBo ciib : checkInBo.getItems()) {
			GuestRoom gr = guestRoomService.findById(ciib.getRoomId());
			LocalDateTime time = LocalDateTime.now();
			ArrayList<CheckInRecord> list = createCheckInRecord(ciib, gr, time);
			// TODO check is group
//			roomStatisticsService.checkIn(gr, time, ciib.getDays(), false);

		}
	}

	private ArrayList<CheckInRecord> createCheckInRecord(CheckInItemBo ciib, GuestRoom gr, LocalDateTime time) {
		ArrayList<CheckInRecord> list = new ArrayList<>();
		CheckInRecord cir = null;
		Customer customer = null;
		LocalDate startDate = time.toLocalDate();
		StringBuilder sb = new StringBuilder();
		LocalTime criticalTime = systemConfigService.getCriticalTime(gr.getHotelCode());
		if (time.toLocalTime().isBefore(criticalTime)) {
			startDate = startDate.plusDays(-1);
		}
		for (int i = 0; i < ciib.getGuests().size(); i++) {
			cir = new CheckInRecord();
			cir.setArriveTime(time);
			ciib.setHotelCode(gr.getHotelCode());
			BeanUtils.copyProperties(ciib, cir);
			customer = customerService.createOrGetCustomer(gr.getHotelCode(), ciib.getGuests().get(i));
			cir.setCustomer(customer);
			cir.setGuestRoom(gr);
			cir.setStartDate(startDate);
			list.add(add(cir));
			roomRecordService.createRoomRecord(cir);
			sb.append(customer.getGuestInfo().getName());
			sb.append(" ");
		}
		return list;
	}

	@Override
	public List<CheckInRecord> checkOut(String roomId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CheckInRecord checkInByTempName(String tempName, String roomId, DtoResponse<String> response) {
		GuestRoom gr = guestRoomService.findById(roomId);
		if (gr != null) {
			Customer customer = customerService.createTempCustomer(gr.getHotelCode(), tempName);

		} else {

		}

		return null;
	}

	@Override
	public List<CheckInRecord> findByBookId(String bookId) {
		return checkInRecordDao.fingByBookId(bookId);
	}

	@Override
	public List<CheckInRecord> findDetailByBookingId(String bookId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CheckInRecord> checkInByTempName(int humanCount, CheckInRecord cir, GuestRoom gr,
			DtoResponse<String> response) {
		LocalTime criticalTime = systemConfigService.getCriticalTime(gr.getHotelCode());
		LocalDate startDate = cir.getArriveTime().toLocalDate();
		if (cir.getArriveTime().toLocalTime().isBefore(criticalTime)) {
			startDate = startDate.plusDays(-1);
		}
		List<CheckInRecord> data = new ArrayList<CheckInRecord>();
		String tempName = gr.getRoomNum();
		List<RoomTag> tags = null;
		if (cir.getDemands() != null && !cir.getDemands().isEmpty()) {
			tags = new ArrayList<RoomTag>();
			tags.addAll(cir.getDemands());
		}
//		DtoResponse<RoomUsage> r = roomUsageService.use(gr, Constants.Status.ROOM_USAGE_BOOK, cir.getArriveTime(),
//				cir.getLeaveTime(), cir.getId(), tempName);
//		roomStatisticsService.assignRoom(new CheckInRecordWrapper(cir));
		List<Arrangement> arrangements = new ArrayList<>();
		for(int i=0; i<cir.getArrangements().size(); i++){
			arrangements.add(cir.getArrangements().get(i));
		}
		for (int i = 1; i <= humanCount; i++) {
			Customer customer = customerService.createTempCustomer(gr.getHotelCode(), tempName + "#" + i);
			CheckInRecord ncir = null;
			try {
				ncir = (CheckInRecord) cir.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			ncir.setDemands(tags);
			ncir.setPersonalPrice(cir.getPurchasePrice());
			ncir.setPersonalPercentage(1.0);
			ncir.setId(null);
			ncir.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION);
			ncir.setCustomer(customer);
			ncir.setName(customer.getName());
			ncir.setType(Constants.Type.CHECK_IN_RECORD_CUSTOMER);
			ncir.setGuestRoom(gr);
			ncir.setSubRecords(null);
			ncir.setStartDate(startDate);
			ncir.setHotelCode(gr.getHotelCode());
			Account account = new Account(0, 0);
			account.setRoomNum(gr.getRoomNum());
			account.setCustomer(customer);
			account.setCode(businessSeqService.fetchNextSeqNum(gr.getHotelCode(),
					Constants.Key.BUSINESS_BUSINESS_CUSTOMER_ACCOUNT_SEQ_KEY));
			ncir.setCheckInCount(1);
			ncir.setRoomCount(1);
			account.setType(Constants.Type.ACCOUNT_CUSTOMER);
			ncir.setAccount(account);
			ncir.setGroupType(cir.getGroupType());// 设置分组类型（团队/散客）
			ncir.setReserveId(cir.getId());// 添加预留记录id
			ncir.setMainRecord(cir.getMainRecord());
			ncir.setArrangements(arrangements);
			ncir = add(ncir);
			data.add(ncir);
			roomRecordService.createRoomRecord(ncir);
			roomStatisticsService.assignRoom(new CheckInRecordWrapper(ncir));
//			guestRoomStatausService.checkIn(ncir);
		}

		return data;
	}

	@Transactional
	@Override
	public CheckInRecord book(CheckInRecord checkInRecord) {
		String orderNum = businessSeqService.fetchNextSeqNum(checkInRecord.getHotelCode(),
				Constants.Key.BUSINESS_ORDER_NUM_SEQ_KEY);
		checkInRecord.setOrderNum(orderNum);
		if (checkInRecord.getSubRecords() != null && !checkInRecord.getSubRecords().isEmpty()) {
//			if (checkInRecord.getGroupType() != null
//					&& checkInRecord.getGroupType().equals(Constants.Type.CHECK_IN_RECORD_GROUP_TYPE_YES)) {
			checkInRecord.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION);
			checkInRecord.setType(Constants.Type.CHECK_IN_RECORD_GROUP);
			initGroup(checkInRecord);
			initGroupAccount(checkInRecord);
			checkInRecord = add(checkInRecord);
			for (CheckInRecord item : checkInRecord.getSubRecords()) {
				if (item.getRoomCount() != null && item.getRoomCount() > 0) {
					item.setOrderNum(orderNum);
					item.setGroupType(checkInRecord.getGroupType());
					item.setHoldTime(checkInRecord.getHoldTime());
					item.setArriveTime(checkInRecord.getArriveTime());
					item.setLeaveTime(checkInRecord.getLeaveTime());
					item.setDays(checkInRecord.getDays());
					item.setContactName(checkInRecord.getContactName());
					item.setMarketEmployee(checkInRecord.getMarketEmployee());
					item.setDistributionChannel(checkInRecord.getDistributionChannel());
					item.setContactMobile(checkInRecord.getContactMobile());
					item.setRoomType(roomTypeService.findById(item.getRoomTypeId()));
					item.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION);
					item.setType(Constants.Type.CHECK_IN_RECORD_RESERVE);
					item.setGroupType(checkInRecord.getGroupType());
					item.setHotelCode(checkInRecord.getHotelCode());
					item.setMainRecord(checkInRecord);
					item.setCheckInCount(0);
					if (item.getRoomType() == null) {
						item.setRoomType(roomTypeService.findById(item.getRoomTypeId()));
					}
					item.setProtocolCorpation(checkInRecord.getProtocolCorpation());
//					boolean bookResult = roomStatisticsService.booking(item.getRoomType(), item.getArriveTime(),
//							item.getRoomCount(), item.getDays());
//					if (!bookResult) {
//						// 房源不足
//					}
					item = add(item);
					roomStatisticsService.reserve(new CheckInRecordWrapper(item));
				}
			}

		} else {
			Customer customer = checkInRecord.getCustomer();
			if (customer != null && customer.getId() == null) {
				customer = customerService.add(customer);
			}
			checkInRecord.setCustomer(customer);
			checkInRecord.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION);
			checkInRecord.setType(Constants.Type.CHECK_IN_RECORD_CUSTOMER);
//			roomStatisticsService.booking(checkInRecord.getRoomType(), checkInRecord.getArriveTime(), 1,
//					checkInRecord.getDays());
			checkInRecord = add(checkInRecord);
			Account account = accountService.createAccount(checkInRecord.getCustomer(), null);
			checkInRecord.setAccount(account);
			modify(checkInRecord);

		}
		return checkInRecord;
	}

	@Override
	public List<CheckInRecord> bookByRoomList(CheckInRecordListBo cirlb) {
		List<CheckInRecord> list = cirlb.getCirs();
		String roomLinkId = null;
		if (cirlb.getIsRoomLink()) {
			RoomLink rl = new RoomLink();
			rl.setDeleted(Constants.DELETED_FALSE);
			roomLinkDao.save(rl);
			roomLinkId = rl.getId();
		}
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setRoomLinkId(roomLinkId);
			bookByRoomTypeTest(list.get(i));
		}
		return list;
	}

	@Override
	@Transactional
	public CheckInRecord bookByRoomTypeTest(CheckInRecord checkInRecord) {
		String orderNum = businessSeqService.fetchNextSeqNum(checkInRecord.getHotelCode(),
				Constants.Key.BUSINESS_ORDER_NUM_SEQ_KEY);
		checkInRecord.setOrderNum(orderNum);
		checkInRecord.setHumanCount(1);
		checkInRecord.setCheckInCount(1);
		checkInRecord.setRoomCount(1);
		checkInRecord.setType(Constants.Type.CHECK_IN_RECORD_CUSTOMER);
		checkInRecord.setGroupType(Constants.Type.CHECK_IN_RECORD_GROUP_TYPE_NO);
		checkInRecord.setSingleRoomCount(1);
		checkInRecord.setStartDate(LocalDate.from(checkInRecord.getArriveTime()));
		GuestRoom gr = guestRoomService.findById(checkInRecord.getGuestRoom().getId());
		checkInRecord.setHotelCode(gr.getHotelCode());
		String tempName = gr.getRoomNum();
		Customer customer = customerService.createTempCustomer(gr.getHotelCode(), tempName + "#" + 1);
		checkInRecord.setCustomer(customer);
		checkInRecord.setName(customer.getName());
		Account account = accountService.createAccount(customer, null);
		checkInRecord.setAccount(account);
		CheckInRecord cir = add(checkInRecord);
		if((Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN).equals(checkInRecord.getStatus())){
			roomStatisticsService.checkIn(new CheckInRecordWrapper(checkInRecord));
		}else if((Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION).equals(checkInRecord.getStatus())){
			roomStatisticsService.booking(new CheckInRecordWrapper(checkInRecord));
		}
		roomRecordService.createRoomRecord(cir);
		return cir;
	}

	@Transactional
	public CheckInRecord bookByRoomType(CheckInRecord checkInRecord) {
		String orderNum = businessSeqService.fetchNextSeqNum(checkInRecord.getHotelCode(),
				Constants.Key.BUSINESS_ORDER_NUM_SEQ_KEY);
		checkInRecord.setOrderNum(orderNum);
		if (checkInRecord.getCustomer() != null) {
			if (checkInRecord.getCustomer().getId() != null && checkInRecord.getCustomer().getId() != "") {

			} else {
				checkInRecord.setCustomer(null);
			}
		}
		int humanCount = checkInRecord.getHumanCount();
		DtoResponse<String> response = new DtoResponse<String>();
		for (int i = 0; i < humanCount; i++) {
			CheckInRecord cir = new CheckInRecord();
			BeanUtils.copyProperties(cir, checkInRecord);
			cir.setMainRecord(checkInRecord);
			cir.setStatus(Constants.Type.CHECK_IN_RECORD_RESERVE);// R
			cir.setCheckInCount(1);
			cir.setRoomCount(1);
			add(checkInRecord);
			GuestRoom gr = guestRoomService.findById(checkInRecord.getGuestRoom().getId());
			checkInByTempName(cir.getSingleRoomCount(), cir, gr, response);
		}

		if (checkInRecord.getSubRecords() != null && !checkInRecord.getSubRecords().isEmpty()) {
			checkInRecord.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION);
			checkInRecord.setType(Constants.Type.CHECK_IN_RECORD_GROUP);
			initGroup(checkInRecord);
			initGroupAccount(checkInRecord);
			checkInRecord = add(checkInRecord);
			for (CheckInRecord item : checkInRecord.getSubRecords()) {
				if (item.getRoomCount() != null && item.getRoomCount() > 0) {
					item.setOrderNum(orderNum);
					item.setGroupType(checkInRecord.getGroupType());
					item.setHoldTime(checkInRecord.getHoldTime());
					item.setArriveTime(checkInRecord.getArriveTime());
					item.setLeaveTime(checkInRecord.getLeaveTime());
					item.setDays(checkInRecord.getDays());
					item.setContactName(checkInRecord.getContactName());
					item.setMarketEmployee(checkInRecord.getMarketEmployee());
					item.setDistributionChannel(checkInRecord.getDistributionChannel());
					item.setContactMobile(checkInRecord.getContactMobile());
					item.setRoomType(roomTypeService.findById(item.getRoomTypeId()));
					item.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION);
					item.setType(Constants.Type.CHECK_IN_RECORD_RESERVE);
					item.setGroupType(checkInRecord.getGroupType());
					item.setHotelCode(checkInRecord.getHotelCode());
					item.setMainRecord(checkInRecord);
					if (item.getRoomType() == null) {
						item.setRoomType(roomTypeService.findById(item.getRoomTypeId()));
					}
					item.setProtocolCorpation(checkInRecord.getProtocolCorpation());
//					boolean bookResult = roomStatisticsService.booking(item.getRoomType(), item.getArriveTime(),
//							item.getRoomCount(), item.getDays());
//					if (!bookResult) {
//						// 房源不足
//						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//					}
					add(item);
				}
			}

		} else {
			Customer customer = checkInRecord.getCustomer();
			if (customer != null && customer.getId() == null) {
				customer = customerService.add(customer);
			}
			checkInRecord.setCustomer(customer);
			checkInRecord.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION);
			checkInRecord.setType(Constants.Type.CHECK_IN_RECORD_CUSTOMER);
//			roomStatisticsService.booking(checkInRecord.getRoomType(), checkInRecord.getArriveTime(), 1,
//					checkInRecord.getDays());
			checkInRecord = add(checkInRecord);
			Account account = accountService.createAccount(checkInRecord.getCustomer(), null);
			checkInRecord.setAccount(account);
			modify(checkInRecord);

		}
		return checkInRecord;
	}

	private void initGroup(CheckInRecord checkInRecord) {

	}

	private void initGroupAccount(CheckInRecord checkInRecord) {
		Account account = new Account(0, 0);
		if (checkInRecord.getGroup() != null) {
			account.setGroup(checkInRecord.getGroup());
		}
		account.setType(Constants.Type.ACCOUNT_GROUP);
		account.setName(checkInRecord.getName());
		checkInRecord.setAccount(account);
	}

	@Override
	public PageResponse<CheckInRecord> notYet(int pageIndex, int pageSize, String status, User user) {
		Pageable page = org.springframework.data.domain.PageRequest.of(pageIndex - 1, pageSize);
		Specification<CheckInRecord> specification = new Specification<CheckInRecord>() {
			@Override
			public Predicate toPredicate(Root<CheckInRecord> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				ArrayList<Predicate> list = new ArrayList<>();
				// 旅馆编码
				if (user != null) {
					Path<Object> hotelCode = root.get("hotelCode");
					Predicate p1 = criteriaBuilder.equal(hotelCode.as(String.class), user.getHotelCode());
					list.add(p1);
				}
				// 状态（R：预订，I：入住，O：退房，D：历史订单，N：未到，S：退房未结账，X：取消）
				if (status != null && status != "") {
					Path<Object> status1 = root.get("status");
					Predicate p1 = criteriaBuilder.equal(status1.as(String.class), status);
					list.add(p1);
				}
				Predicate[] parr = new Predicate[list.size()];
				parr = list.toArray(parr);
				return criteriaBuilder.and(parr);
			}
		};
		Page<CheckInRecord> p = checkInRecordDao.findAll(specification, page);
		return convent(p);
	}

	@Override
	public PageResponse<CheckInRecord> accountEntryList(int pageIndex, int pageSize, User user) {
		Pageable page = org.springframework.data.domain.PageRequest.of(pageIndex - 1, pageSize);
		ParamSpecification<CheckInRecord> psf = new ParamSpecification<CheckInRecord>();
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> emap = new HashMap<>();
		emap.put("hotelCode", user.getHotelCode());
		emap.put("status", "I");
		map.put("equals", emap);
		Specification<CheckInRecord> specification = psf.createSpecification(map);
		Page<CheckInRecord> p = checkInRecordDao.findAll(specification, page);
		return convent(p);
	}

	@Override
	public List<CheckInRecord> accountEntryListAll(String hotelCode) {
		ParamSpecification<CheckInRecord> psf = new ParamSpecification<CheckInRecord>();
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> emap = new HashMap<>();
		emap.put("hotelCode", hotelCode);
		emap.put("status", "I");
		map.put("equals", emap);
		Specification<CheckInRecord> specification = psf.createSpecification(map);
		List<CheckInRecord> list = checkInRecordDao.findAll(specification);
		return list;
	}

	@Override
	public PageResponse<Map<String, Object>> unreturnedGuests(int pageIndex, int pageSize, String status, User user) {
		Pageable page = org.springframework.data.domain.PageRequest.of(pageIndex - 1, pageSize);
		String hotelCode = null;
		if (user != null) {
			hotelCode = user.getHotelCode();
		}
		Page<Map<String, Object>> p = checkInRecordDao.unreturnedGuests(page, status, hotelCode);
		PageResponse<Map<String, Object>> pr = new PageResponse<>();
		pr.setPageSize(p.getNumberOfElements());
		pr.setPageCount(p.getTotalPages());
		pr.setTotal(p.getTotalElements());
		pr.setCurrentPage(p.getNumber());
		pr.setContent(p.getContent());
		return pr;
	}

	@Override
	public List<Map<String, Object>> getStatistics(User user) {
		String hotelCode = null;
		if (user != null) {
			hotelCode = user.getHotelCode();
		}
		List<Map<String, Object>> list = checkInRecordDao.getStatistics(hotelCode);
		return list;
	}

	@Override
	public Collection<AccountSummaryVo> getAccountSummaryByOrderNum(String orderNum, String type) {
		List<CheckInRecord> cirs = checkInRecordDao.findByOrderNumAndTypeAndDeleted(orderNum, type,
				Constants.DELETED_FALSE);
		return checkInRecordToAccountSummaryVo(cirs);
	}

	private Collection<AccountSummaryVo> checkInRecordToAccountSummaryVo(List<CheckInRecord> data){
		Map<String, AccountSummaryVo> asvm = new HashMap<>();
		AccountSummaryVo asv = null;
		for (CheckInRecord cir : data) {
			if (cir.getAccount() != null&&cir.getGuestRoom()!=null) {
				Account acc = cir.getAccount();
				acc.setRoomNum(cir.getGuestRoom().getRoomNum());
				acc.setName(cir.getCustomer().getName());
				if (!asvm.containsKey(cir.getGuestRoom().getRoomNum())) {
					asv = new AccountSummaryVo();
					asv.setType("room");
					asv.setSettleType(Constants.Type.SETTLE_TYPE_ROOM);
					asv.setRoomNum(acc.getRoomNum());
					asv.setId(cir.getGuestRoom().getId());
					asv.setName(acc.getRoomNum());
					asv.setChildren(new ArrayList<AccountSummaryVo>());
					asvm.put(acc.getRoomNum(), asv);
				} else {
					asv = asvm.get(acc.getRoomNum());
				}
				asv.getChildren().add(new AccountSummaryVo(cir));
			}
		}
		return asvm.values();
	}



	@Override
	public PageResponse<CheckInRecordListVo> querySummaryList(PageRequest<CheckInRecord> prq) {
		PageResponse<CheckInRecord> cirp = listPage(prq);
		List<CheckInRecordListVo> data = new ArrayList<CheckInRecordListVo>();
		for (CheckInRecord cir : cirp.getContent()) {
			data.add(new CheckInRecordListVo(cir));
		}
		PageResponse<CheckInRecordListVo> rep = new PageResponse<>();
		BeanUtils.copyProperties(cirp, rep);
		rep.setContent(data);
		return rep;
	}

	@Override
	public List<CheckInRecord> findByOrderNum(String orderNum) {
		return checkInRecordDao.findByOrderNumAndDeleted(orderNum, Constants.DELETED_FALSE);
	}

	@Override
	public CheckInRecord addReserve(CheckInRecord checkInRecord) {
		if (checkInRecord.getMainRecordId() != null) {
			CheckInRecord mainCheckInRecord = findById(checkInRecord.getMainRecordId());
			checkInRecord.setHoldTime(mainCheckInRecord.getHoldTime());
			checkInRecord.setDays(mainCheckInRecord.getDays());
			checkInRecord.setContactName(mainCheckInRecord.getContactName());
			checkInRecord.setMarketEmployee(mainCheckInRecord.getMarketEmployee());
			checkInRecord.setDistributionChannel(mainCheckInRecord.getDistributionChannel());
			checkInRecord.setContactMobile(mainCheckInRecord.getContactMobile());
			checkInRecord.setOrderNum(mainCheckInRecord.getOrderNum());
			checkInRecord.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION);
			checkInRecord.setType(Constants.Type.CHECK_IN_RECORD_RESERVE);
			checkInRecord.setHotelCode(mainCheckInRecord.getHotelCode());
			checkInRecord.setGroupType(mainCheckInRecord.getGroupType());
			checkInRecord.setRoomType(roomTypeService.findById(checkInRecord.getRoomTypeId()));
			checkInRecord.setMainRecord(mainCheckInRecord);
			checkInRecord = add(checkInRecord);
			mainCheckInRecord.setRoomCount(mainCheckInRecord.getRoomCount() + checkInRecord.getRoomCount());
//			mainCheckInRecord.getSubRecords().add(checkInRecord);
			modify(mainCheckInRecord);
		}
		return checkInRecord;
	}

	@Override
	public List<CheckInRecord> addReserve(List<CheckInRecord> checkInRecords) {
		for (CheckInRecord cir : checkInRecords) {
			cir = addReserve(cir);
		}
		return checkInRecords;
	}

	@Transactional
	@Override
	public CheckInRecord addTogether(TogetherBo togetherBo) {
		CheckInRecord cir = findById(togetherBo.getCurrentId());
		CheckInRecord ncir = null;
		if (cir != null) {
			List<RoomTag> tags = null;
			if (cir.getDemands() != null && !cir.getDemands().isEmpty()) {
				tags = new ArrayList<RoomTag>();
				tags.addAll(cir.getDemands());
			}
			try {
				ncir = (CheckInRecord) cir.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			ncir.setDemands(tags);
			ncir.setId(null);
			ncir.setAccount(null);
			ncir.setSubRecords(null);
			ncir.setStatus(togetherBo.getStatus());
			Customer customer = customerService.createOrGetCustomer(cir.getGuestRoom().getHotelCode(),
					togetherBo.getName(), togetherBo.getIdCardNum(), togetherBo.getMobile());
			Account account = accountService.createAccount(customer, cir.getGuestRoom().getRoomNum());
			ncir.setAccount(account);
			ncir.setCustomer(customer);
			ncir = add(ncir);
		}
		return ncir;
	}

	@Override
	public List<CheckInRecord> findRoomTogetherRecord(CheckInRecord cir, String status) {
		CheckInRecord exCir = new CheckInRecord();
		exCir.setGuestRoom(cir.getGuestRoom());
		exCir.setOrderNum(cir.getOrderNum());
		exCir.setHotelCode(cir.getHotelCode());
		if (status != null) {
			exCir.setStatus(status);
		}
		exCir.setType(Constants.Type.CHECK_IN_RECORD_CUSTOMER);
		Example<CheckInRecord> ex = Example.of(exCir);
		return checkInRecordDao.findAll(ex);
	}

	@Override
	public DtoResponse<String> hangUp(String id) {
		CheckInRecord cir = findById(id);
		DtoResponse<String> rep = new DtoResponse<String>();
		cir.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_OUT_UNSETTLED);
		modify(cir);
		return rep;
	}

	@Override
	public DtoResponse<String> hangUpByAccountId(String id) {
		CheckInRecord cir = checkInRecordDao.findByAccountId(id);
		DtoResponse<String> rep = new DtoResponse<String>();
		cir.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_OUT_UNSETTLED);
		modify(cir);
		return rep;
	}

	@Override
	public List<CheckInRecord> findByOrderNumAndGuestRoomAndDeleted(String orderNum, GuestRoom guestRoom, int delete) {
		List<CheckInRecord> list = checkInRecordDao.findByOrderNumAndGuestRoomAndDeleted(orderNum, guestRoom, delete);
		return list;
	}

	// 取消排房
	@Override
	@Transactional
	public HttpResponse callOffAssignRoom(String[] ids) {
		HttpResponse hr = new HttpResponse();
		List<String> reserveIds = new ArrayList<>();
		String mainRecordId = null;
		for (int i = 0; i < ids.length; i++) {
			CheckInRecord cir = findById(ids[i]);
			if (cir.getMainRecord() != null) {
				mainRecordId = cir.getMainRecord().getId();
			}
			if (("G").equals(cir.getType())) {
				return hr.error("主单不能修改");
			}
			cir.setDeleted(Constants.DELETED_TRUE);
			// 修改选中的数据状态，排房记录改为删除
			modify(cir);
			roomStatisticsService.cancleAssign(new CheckInRecordWrapper(cir));
			// 取消排房成功，修改房间状态
			// 修改排放记录状态为删除后，查询此房间是否还有其他人在住
			List<CheckInRecord> list = checkInRecordDao.findByOrderNumAndGuestRoomAndDeleted(cir.getOrderNum(),
					cir.getGuestRoom(), Constants.DELETED_FALSE);
			// 如果该房间没有人在住了，则修改房间状态
			if (list == null || list.isEmpty()) {
				// ...修改房间状态代码
			}
			// 查出所有的预留记录id，放入集合
			if (cir.getReserveId() != null) {
				if (!reserveIds.contains(cir.getReserveId())) {
					reserveIds.add(cir.getReserveId());
				}
			}
		}
		updateCount(reserveIds, mainRecordId);
		return hr;
	}

	// 取消预订
	@Override
	@Transactional
	public HttpResponse callOffReserve(String[] ids) {
		HttpResponse hr = new HttpResponse();
		List<String> reserveIds = new ArrayList<>();
		String mainRecordId = null;
		for (int i = 0; i < ids.length; i++) {
			CheckInRecord cir = checkInRecordDao.getOne(ids[i]);
			if (cir.getMainRecord() != null) {
				mainRecordId = cir.getMainRecord().getId();
			}
			// 如果是主单
			if (("G").equals(cir.getType())) {
				return hr.error("此处不能取消主单");
			}
			// 如果是预留单，取消预留单下的所有排房/预定
			if (("R").equals(cir.getType())) {
				List<CheckInRecord> list = checkInRecordDao.findByReserveIdAndDeleted(cir.getId(),
						Constants.DELETED_FALSE);
				for (int j = 0; j < list.size(); j++) {
					CheckInRecord cird = list.get(j);
					cird.setDeleted(Constants.DELETED_TRUE);
//					String statusR = cird.getStatus();
					if((Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN).equals(cird.getStatus())){
						roomStatisticsService.checkIn(new CheckInRecordWrapper(cird));
					}
					if((Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION).equals(cird.getStatus())){
						roomStatisticsService.booking(new CheckInRecordWrapper(cird));
					}
					cird.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_CANCLE_BOOK);
					modify(cird);
				}
			}
			cir.setDeleted(Constants.DELETED_TRUE);
			String status = cir.getStatus();
			if((Constants.Status.CHECKIN_RECORD_STATUS_CHECK_IN).equals(status)){
				roomStatisticsService.checkIn(new CheckInRecordWrapper(cir));
			}
			if((Constants.Status.CHECKIN_RECORD_STATUS_RESERVATION).equals(status)){
				roomStatisticsService.booking(new CheckInRecordWrapper(cir));
			}
			cir.setStatus(Constants.Status.CHECKIN_RECORD_STATUS_CANCLE_BOOK);
			modify(cir);
			// 查出所有的预留记录id，放入集合
			if (cir.getReserveId() != null) {
				if (!reserveIds.contains(cir.getReserveId())) {
					reserveIds.add(cir.getReserveId());
				}
			}
		}
		updateCount(reserveIds, mainRecordId);
		return hr.ok();
	}

	@Override
	public HttpResponse updateCount(List<String> reserveIds, String mainRecordId) {
		HttpResponse hr = new HttpResponse();
		// 修改预留记录的已排房数量
		for (int i = 0; i < reserveIds.size(); i++) {
			int count = checkInRecordDao.getReserveIdCount(reserveIds.get(i), Constants.DELETED_FALSE);
			CheckInRecord cir = findById(reserveIds.get(i));
			cir.setCheckInCount(count);
			modify(cir);
		}
		// 修改主单已排房数量
		CheckInRecord cir = findById(mainRecordId);
		int count = checkInRecordDao.getMainRecordIdCount(mainRecordId, Constants.DELETED_FALSE);
		cir.setCheckInCount(count);
		modify(cir);
		return hr.ok();
	}

	// 查询可联房的数据
	@Override
	public PageResponse<CheckInRecord> getNRoomLink(int pageIndex, int pageSize, String name, String roomNum,
			String hotelCode, String groupType) {
		Pageable page = org.springframework.data.domain.PageRequest.of(pageIndex - 1, pageSize);
		Page<CheckInRecord> pageList = checkInRecordDao.findAll(new Specification<CheckInRecord>() {
			@Override
			public Predicate toPredicate(Root<CheckInRecord> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> list = new ArrayList<Predicate>();
				if (hotelCode != null) {
					list.add(criteriaBuilder.equal(root.get("hotelCode"), hotelCode));
				}
				if (groupType != null) {
					list.add(criteriaBuilder.equal(root.get("groupType"), groupType));
				}
				if (roomNum != null) {
					list.add(criteriaBuilder.equal(root.get("roomNum"), roomNum));
				}
				if (name != null) {
					// 外键对象的属性，要用join再get
					list.add(criteriaBuilder.like(root.join("customer").get("name"), "%" + name + "%"));
				}
				list.add(criteriaBuilder.isNull(root.get("roomLinkId")));
				list.add(criteriaBuilder.equal(root.get("type"), Constants.Type.CHECK_IN_RECORD_CUSTOMER));
				list.add(criteriaBuilder.equal(root.get("deleted"), Constants.DELETED_FALSE));
				Predicate[] array = new Predicate[list.size()];
				return criteriaBuilder.and(list.toArray(array));
			}
		}, page);
		return convent(pageList);
	}

	// 根据roomlinkId查询已经联房的数据
	@Override
	public List<CheckInRecord> getRoomLinkList(String roomLinkId) {
		List<CheckInRecord> list = new ArrayList<>();
		if (roomLinkId != null) {
			list = checkInRecordDao.findByRoomLinkId(roomLinkId);
		}
		return list;
	}
	// 根据roomlinkId查询已经联房的数据,否则根据订单号和房间查询
	@Override
	public List<CheckInRecord> getRoomLinkListTo(String id, String orderNum) {
		CheckInRecord cir = checkInRecordDao.getOne(id);
		String roomLinkId = cir.getRoomLinkId();
		List<CheckInRecord> list = new ArrayList<>();
		if (roomLinkId != null) {
			list = checkInRecordDao.findByRoomLinkId(roomLinkId);
		}else {
			list = checkInRecordDao.findByOrderNumAndGuestRoomAndDeleted(orderNum, cir.getGuestRoom(), Constants.DELETED_FALSE);
		}
		return list;
	}

	// 删除联房
	@Override
	public void deleteRoomLink(String[] ids) {
		for (int i = 0; i < ids.length; i++) {
			CheckInRecord cir = checkInRecordDao.getOne(ids[i]);
			cir.setRoomLinkId(null);
			update(cir);
		}
	}

	@Override
	public List<CheckInRecord> getByRoomNum(String roomNum, String hotelCode, String groupType) {
		List<CheckInRecord> list = checkInRecordDao.findAll(new Specification<CheckInRecord>() {
			@Override
			public Predicate toPredicate(Root<CheckInRecord> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> list = new ArrayList<Predicate>();
				if (roomNum != null) {
					list.add(criteriaBuilder.equal(root.join("guestRoom").get("roomNum"), roomNum));
				}
				if (hotelCode != null) {
					list.add(criteriaBuilder.equal(root.get("hotelCode"), hotelCode));
				}
				if (groupType != null) {
					list.add(criteriaBuilder.equal(root.get("groupType"), groupType));
				}
				list.add(criteriaBuilder.isNull(root.get("roomLinkId")));
				Predicate[] array = new Predicate[list.size()];
				return criteriaBuilder.and(list.toArray(array));
			}
		});
		return list;
	}

	@Override
	public List<CheckInRecord> checkInTogether(String hotelCode, String orderNum, String guestRoomId) {
		GuestRoom guestRoom = guestRoomService.findById(guestRoomId);
		List<CheckInRecord> list = checkInRecordDao.findAll(new Specification<CheckInRecord>() {
			@Override
			public Predicate toPredicate(Root<CheckInRecord> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> list = new ArrayList<Predicate>();
				if (hotelCode != null) {
					list.add(criteriaBuilder.equal(root.get("hotelCode"), hotelCode));
				}
				if (orderNum != null) {
					list.add(criteriaBuilder.equal(root.get("orderNum"), orderNum));
				}
				if (guestRoom != null) {
					list.add(criteriaBuilder.equal(root.join("guestRoom"), guestRoom));
				}
				list.add(criteriaBuilder.equal(root.get("deleted"), Constants.DELETED_FALSE));
//				list.add(criteriaBuilder.isNotNull(root.get("guestRoom")));
				Predicate[] array = new Predicate[list.size()];
				return criteriaBuilder.and(list.toArray(array));
			}
		});
		return list;
	}

	@Override
	public List<CheckInRecord> findByTogetherCode(String hotelCode, String togetherCod) {
		List<CheckInRecord> list = checkInRecordDao.findAll(new Specification<CheckInRecord>() {
			@Override
			public Predicate toPredicate(Root<CheckInRecord> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> list = new ArrayList<Predicate>();
				if (hotelCode != null) {
					list.add(criteriaBuilder.equal(root.get("hotelCode"), hotelCode));
				}
				if (togetherCod != null) {
					list.add(criteriaBuilder.equal(root.get("togetherCode"), togetherCod));
				}
				list.add(criteriaBuilder.equal(root.get("deleted"), Constants.DELETED_FALSE));
				list.add(criteriaBuilder.isNotNull(root.get("guestRoom")));
				Predicate[] array = new Predicate[list.size()];
				return criteriaBuilder.and(list.toArray(array));
			}
		});
		return list;
	}

	@Override
	@org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
	public void addTogether(String hotelCode, String orderNum, String customerId, String status, String guestRoomId) {
		List<CheckInRecord> list = checkInTogether(hotelCode, orderNum, guestRoomId);
		CheckInRecord cir = list.get(0);
		CheckInRecord checkInRecord = new CheckInRecord();
		BeanUtils.copyProperties(cir, checkInRecord);
		//上面是浅拷贝，集合不允许被多次引用（会报集合共享引用不允许错误）,作出以下处理
		List<Arrangement> arrangements = cir.getArrangements();
		List<Arrangement> arrangements1 = new ArrayList<>();
		for(int i=0; i<arrangements.size(); i++){
			arrangements1.add(arrangements.get(i));
		}
		checkInRecord.setArrangements(arrangements1);
		//浅拷贝处理完毕
		if (null != cir.getDemands() && !cir.getDemands().isEmpty()) {
			List<RoomTag> demands = new ArrayList<>();
			for (int i = 0; i < demands.size(); i++) {
				RoomTag rt = new RoomTag();
				BeanUtils.copyProperties(demands.get(i), rt);
				demands.add(rt);
			}
			checkInRecord.setDemands(demands);
		} else {
			checkInRecord.setDemands(null);
		}
		checkInRecord.setSubRecords(null);
		Customer customer = customerService.findById(customerId);
		checkInRecord.setId(null);
		if (status != null && status != "") {
			checkInRecord.setStatus(status);
		}
		// 新增同住默认房价为零
		checkInRecord.setPersonalPrice(0.0);
		checkInRecord.setPersonalPercentage(0.0);
		checkInRecord.setCustomer(customer);
		if (cir.getTogetherCode() == null) {
			String togetherNum = businessSeqService.fetchNextSeqNum(hotelCode, Constants.Key.TOGETHER_NUM_KEY);
			for (int i = 0; i < list.size(); i++) {
				CheckInRecord cirT = list.get(i);
				cirT.setTogetherCode(togetherNum);
				update(cirT);
			}
			checkInRecord.setTogetherCode(togetherNum);
		} else {
			checkInRecord.setTogetherCode(cir.getTogetherCode());
		}
		checkInRecordDao.save(checkInRecord);
	}

	@Override
	public CheckInRecord queryByAccountId(String id) {
		return checkInRecordDao.findByAccountId(id);
	}

	// 独单房价
	@Override
	@org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
	public void roomPriceAllocation(String hotelCode, String orderNum, String customerId, String guestRoomId) {
		List<CheckInRecord> list = checkInTogether(hotelCode, orderNum, guestRoomId);
		for (int i = 0; i < list.size(); i++) {
			CheckInRecord cir = list.get(i);
			String custId = cir.getCustomer().getId();
			if (!customerId.equals(custId)) {
				cir.setPersonalPrice(0.0);
				cir.setPersonalPercentage(1.0);
				update(cir);
			} else {
				cir.setPersonalPrice(cir.getPurchasePrice());
				cir.setPersonalPercentage(1.0);// 因为是独单房价，占比1
			}

		}
	}

	// 平均房价
	@Override
	@org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
	public void roomPriceAvg(String hotelCode, String orderNum, String guestRoomId) {
		List<CheckInRecord> list = checkInTogether(hotelCode, orderNum, guestRoomId);
		if (list != null && !list.isEmpty()) {
			int peopleCount = list.size();
			Double roomPrice = list.get(0).getPurchasePrice();
			DecimalFormat df = new DecimalFormat("####0.00");
			Double avg = roomPrice / peopleCount;
			Double personalPrice = Double.parseDouble(df.format(avg));
			Double personalPercentage = Double.parseDouble(df.format(1 / peopleCount));
			for (int i = 0; i < list.size(); i++) {
				CheckInRecord cir = list.get(i);
				cir.setPersonalPrice(personalPrice);
				cir.setPersonalPercentage(personalPercentage);
				update(cir);
			}
		}
	}

	@Override
	public List<CheckInRecord> findByGuestRoomAndStatusAndDeleted(GuestRoom guestRoom, String status, int deleted) {
		return checkInRecordDao.findByGuestRoomAndStatusAndDeleted(guestRoom, status, deleted);
	}

	@Override
	public List<CheckInRecord> findTodayCheckInRecord(GuestRoom guestRoom, String status) {
		return checkInRecordDao.findByGuestRoomIdAndStatusAndDeletedAndStartDate(guestRoom.getId(), status,
				Constants.DELETED_FALSE,LocalDate.now());
	}

	@Override
	public List<CheckInRecord> findByLinkId(String id) {
		return checkInRecordDao.findByRoomLinkId(id);
	}

	@Override
	public List<CheckInRecord> findByGuestRoomAndStatusAndDeleted(String guestRoomId, String status, int deleted) {
		return checkInRecordDao.findByGuestRoomIdAndStatusAndDeleted(guestRoomId, status, deleted);
	}

	@Override
	public List<Map<String, Object>> getGroup(String hotelCode, String arriveTime, String leaveTime, String name_,
			String code_) {
		List<Map<String, Object>> list = checkInRecordDao.getGroup(hotelCode, arriveTime, leaveTime, name_, code_);
		return list;
	}

	@Override
	public void inGroup(String[] cId, String gId, Boolean isFollowGroup) {
		CheckInRecord cirG = checkInRecordDao.getOne(gId);
		List<String> roomNums = new ArrayList<>();
		int checkInCount = 0;
		for (int i = 0; i < cId.length; i++) {
			String id = cId[i];
			CheckInRecord cir = checkInRecordDao.getOne(id);
			if (!roomNums.contains(cir.getGuestRoom().getRoomNum())) {
				roomNums.add(cir.getGuestRoom().getRoomNum());
			}
			cir.setMainRecord(cirG);
			cir.setOrderNum(cirG.getOrderNum());
			cir.setGroupType(Constants.Type.CHECK_IN_RECORD_GROUP_TYPE_YES);
			if (isFollowGroup) {
				cir.setMarketingSources(cirG.getMarketingSources());
				cir.setRoomPriceScheme(cirG.getRoomPriceScheme());
			}
			// 如果是入住，主单入住数加1
			if (("I").equals(cir.getStatus())) {
				checkInCount += 1;
			}
			checkInRecordDao.save(cir);
		}
		cirG.setRoomCount(cirG.getRoomCount() + roomNums.size());
//		cirG.setHumanCount(cirG.getHumanCount()+cId.length);
		cirG.setCheckInCount(cirG.getCheckInCount() + checkInCount);
		checkInRecordDao.save(cirG);
	}

	@Override
	public void outGroup(String[] cId, String gId, Boolean isFollowGroup) {
		CheckInRecord cirG = checkInRecordDao.getOne(gId);
		List<String> roomNums = new ArrayList<>();
		int checkInCount = 0;
		for (int i = 0; i < cId.length; i++) {
			String id = cId[i];
			CheckInRecord cir = checkInRecordDao.getOne(id);
			String orderNum = businessSeqService.fetchNextSeqNum(cir.getHotelCode(),
					Constants.Key.BUSINESS_ORDER_NUM_SEQ_KEY);
			if (cir.getGuestRoom() != null) {
				if (!roomNums.contains(cir.getGuestRoom().getRoomNum())) {
					roomNums.add(cir.getGuestRoom().getRoomNum());
				}
			}
			cir.setMainRecord(null);
			cir.setOrderNum(orderNum);
			cir.setGroupType(Constants.Type.CHECK_IN_RECORD_GROUP_TYPE_NO);
			// 如果是入住，主单入住数减1
			if (("I").equals(cir.getStatus())) {
				checkInCount += 1;
			}
			if (isFollowGroup) {
				//
			}
			checkInRecordDao.save(cir);
		}

		cirG.setRoomCount(cirG.getRoomCount() - roomNums.size());
		cirG.setCheckInCount(cirG.getCheckInCount() - checkInCount);
		checkInRecordDao.save(cirG);
	}

	@Override
	public void updateGroup(String[] cId, String gId, String uId, Boolean isFollowGroup) {
		CheckInRecord cirG = checkInRecordDao.getOne(gId);
		CheckInRecord cirU = checkInRecordDao.getOne(uId);
		List<String> roomNums = new ArrayList<>();
		int checkInCount = 0;
		for (int i = 0; i < cId.length; i++) {
			String id = cId[i];
			CheckInRecord cir = checkInRecordDao.getOne(id);
			if (!roomNums.contains(cir.getGuestRoom().getRoomNum())) {
				roomNums.add(cir.getGuestRoom().getRoomNum());
			}
			cir.setMainRecord(cirU);
			cir.setOrderNum(cirU.getOrderNum());
			cir.setGroupType(Constants.Type.CHECK_IN_RECORD_GROUP_TYPE_YES);
			if (isFollowGroup) {
				cir.setMarketingSources(cirU.getMarketingSources());
				cir.setRoomPriceScheme(cirU.getRoomPriceScheme());
			}
			// 如果是入住，主单入住数加1
			if (("I").equals(cir.getStatus())) {
				checkInCount += 1;
			}
			checkInRecordDao.save(cir);
		}
		cirG.setRoomCount(cirG.getRoomCount() - roomNums.size());
		cirG.setHumanCount(cirG.getHumanCount() - cId.length);
		cirG.setCheckInCount(cirG.getCheckInCount() - checkInCount);
		checkInRecordDao.save(cirG);
		cirU.setRoomCount(cirU.getRoomCount() + roomNums.size());
		cirU.setCheckInCount(cirU.getCheckInCount() + checkInCount);
		checkInRecordDao.save(cirU);
	}

	@Override
	public Collection<AccountSummaryVo> getAccountSummaryByLinkNum(String orderNum, String accountCustomer) {
		List<CheckInRecord> data = findByLinkId(orderNum);
		return checkInRecordToAccountSummaryVo(data);
	}
	//修改预留
	@Override
	@Transactional
	public CheckInRecord updateReserve(CheckInRecord cir){
		CheckInRecord oldCir = checkInRecordDao.getOne(cir.getId());
		CheckInRecordWrapper cirw = new CheckInRecordWrapper(oldCir);
		roomStatisticsService.cancleReserve(cirw);//先取消以前预留
		CheckInRecord checkInRecord = new CheckInRecord();
		cir.setId(null);
		BeanUtils.copyProperties(cir, checkInRecord);
		checkInRecord = checkInRecordDao.saveAndFlush(checkInRecord);
		roomStatisticsService.reserve(new CheckInRecordWrapper(checkInRecord));//重新预留
		return checkInRecord;
	}
}
