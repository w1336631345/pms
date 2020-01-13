package com.kry.pms.service.busi.impl;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.base.ParamSpecification;
import com.kry.pms.dao.busi.RoomRecordDao;
import com.kry.pms.model.persistence.busi.CheckInRecord;
import com.kry.pms.model.persistence.busi.DailyVerify;
import com.kry.pms.model.persistence.busi.RoomRecord;
import com.kry.pms.model.persistence.sys.User;
import com.kry.pms.service.busi.BillService;
import com.kry.pms.service.busi.BookingRecordService;
import com.kry.pms.service.busi.CheckInRecordService;
import com.kry.pms.service.busi.RoomRecordService;
import com.kry.pms.service.guest.CustomerService;
import com.kry.pms.service.room.GuestRoomService;
import com.kry.pms.service.room.RoomStatisticsService;
import com.kry.pms.service.sys.BusinessSeqService;
import com.kry.pms.service.sys.SystemConfigService;
import com.kry.pms.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


@Service
public class RoomRecordServiceImpl implements RoomRecordService {
	@Autowired
	RoomRecordDao roomRecordDao;
	@Autowired
	BookingRecordService bookingRecordService;
	@Autowired
	GuestRoomService guestRoomService;
	@Autowired
	SystemConfigService systemConfigService;
	@Autowired
	RoomStatisticsService roomStatisticsService;
	@Autowired
	CustomerService customerService;
	@Autowired
	BillService billService;
	@Autowired
	BusinessSeqService businessSeqService;
	@Autowired
	CheckInRecordService checkInRecordService;

	@Override
	public RoomRecord add(RoomRecord roomRecord) {
		return roomRecordDao.saveAndFlush(roomRecord);
	}

	@Override
	public void delete(String id) {
		RoomRecord roomRecord = roomRecordDao.findById(id).get();
		if (roomRecord != null) {
			roomRecord.setDeleted(Constants.DELETED_TRUE);
		}
		roomRecordDao.saveAndFlush(roomRecord);
	}

	@Override
	public RoomRecord modify(RoomRecord roomRecord) {
		return roomRecordDao.saveAndFlush(roomRecord);
	}

	@Override
	public RoomRecord findById(String id) {
		return roomRecordDao.getOne(id);
	}

	@Override
	public List<RoomRecord> getAllByHotelCode(String code) {
		return null;// 默认不实现
		// return roomRecordDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<RoomRecord> listPage(PageRequest<RoomRecord> prq) {
		Example<RoomRecord> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(roomRecordDao.findAll(ex, req));
	}

	@Override
	public void createRoomRecord(CheckInRecord cir) {
		RoomRecord rr = null;
		for (int i = 0; i < cir.getDays(); i++) {
			rr = new RoomRecord();
			rr.setCheckInRecord(cir);
			rr.setCustomer(cir.getCustomer());
			rr.setGuestRoom(cir.getGuestRoom());
			rr.setRecordDate(cir.getStartDate().plusDays(i));
			add(rr);
		}
	}

	@Override
	public void dailyVerify(String hotelCode, LocalDate recordDate, DailyVerify dv) {
		List<RoomRecord> list = roomRecordDao.findByHotelCodeAndRecordDate(hotelCode, recordDate);
		if (list != null && !list.isEmpty()) {
			for (RoomRecord rr : list) {
				rr.setStatus(Constants.Status.ROOM_RECORD_STATUS_DAILY_VERIFY_PASS);
				rr.setDailyVerify(dv);
				modify(rr);
//				billService.billEntry(rr,recordDate);
			}
		}
	}

	@Override
	public void checkOut(List<CheckInRecord> crs) {
		// TODO Auto-generated method stub

	}

	@Override
	public PageResponse<RoomRecord> accountEntryList(int pageIndex, int pageSize, User user) {
		Pageable page = org.springframework.data.domain.PageRequest.of(pageIndex-1, pageSize);
		ParamSpecification<RoomRecord> psf = new ParamSpecification<RoomRecord>();
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> emap = new HashMap<>();
		emap.put("hotelCode", user.getHotelCode());
		emap.put("isAccountEntry", "NO");
		map.put("equals", emap);
//		Map<String, String[]> omap = new HashMap<>();
//		Map<String, Object> theSameMap = new HashMap<>();
//		theSameMap.put("theSameKey", omap);
//		String[] values = {"R","I"};
//		omap.put("status", values);
//		map.put("or", theSameMap);
		Specification<RoomRecord> specification = psf.createSpecification(map);
		Page<RoomRecord> p = roomRecordDao.findAll(specification,page);
		return convent(p);
	}
	@Override
	public PageResponse<RoomRecord> accountEntryListTest(int pageIndex, int pageSize, User user) {
//		Date startTime = DateTimeUtil.toDayStartDate();
//		Date endTime = DateTimeUtil.toDayEndDate();

		Pageable page = org.springframework.data.domain.PageRequest.of(pageIndex-1, pageSize);
		List<RoomRecord> resultList = null;
		Specification<RoomRecord> querySpecifi = new Specification<RoomRecord>() {
			@Override
			public Predicate toPredicate(Root<RoomRecord> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();
				predicates.add(cb.equal(root.get("recordDate").as(LocalDate.class), LocalDate.now()));
//				//大于或等于传入时间
//				predicates.add(cb.greaterThanOrEqualTo(root.get("recordDate").as(Date.class), startTime));
//				//小于或等于传入时间
//				predicates.add(cb.lessThanOrEqualTo(root.get("recordDate").as(Date.class), endTime));
				if (StringUtils.isNotBlank(user.getHotelCode())) {
					//查询
					predicates.add(cb.equal(root.get("hotelCode").as(String.class), user.getHotelCode()));
				}
				predicates.add(cb.equal(root.get("isAccountEntry").as(String.class), "NO"));
				// and到一起的话所有条件就是且关系，or就是或关系
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
		Page<RoomRecord> p = roomRecordDao.findAll(querySpecifi,page);
		return convent(p);
	}

	@Override
	public List<RoomRecord> accountEntryListAll(String hotelCode, LocalDate businessDate) {
		ParamSpecification<RoomRecord> psf = new ParamSpecification<RoomRecord>();
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> emap = new HashMap<>();
		emap.put("recordDate", businessDate);
		emap.put("hotelCode", hotelCode);
//		emap.put("isAccountEntry", "NO");
		map.put("equals", emap);
		Specification<RoomRecord> specification = psf.createSpecification(map);
		List<RoomRecord> list = roomRecordDao.findAll(specification);
		return list;
	}

	@Override
	public List<RoomRecord> findByHotelCodeAndCheckInRecord(String hotelCode, String checkInRecordId){
		CheckInRecord cir = checkInRecordService.findById(checkInRecordId);
//		List<RoomRecord> list = roomRecordDao.findByHotelCodeAndCheckInRecord(hotelCode, cir);
		List<RoomRecord> list = roomRecordDao.findByCheckInRecord(cir);
		return list;
	}

}
