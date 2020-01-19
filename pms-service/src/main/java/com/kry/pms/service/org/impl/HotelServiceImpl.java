package com.kry.pms.service.org.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.org.HotelDao;
import com.kry.pms.model.http.response.org.HotelInfoVo;
import com.kry.pms.model.persistence.marketing.DiscountScheme;
import com.kry.pms.model.persistence.marketing.MarketingSources;
import com.kry.pms.model.persistence.org.Hotel;
import com.kry.pms.service.marketing.DiscountSchemeService;
import com.kry.pms.service.marketing.DistributionChannelService;
import com.kry.pms.service.marketing.MarketingSourcesService;
import com.kry.pms.service.org.HotelService;
import com.kry.pms.service.room.RoomTagService;
import com.kry.pms.service.room.RoomTypeService;
import com.kry.pms.service.sys.SystemConfigService;

@Service
public class HotelServiceImpl implements HotelService {
	@Autowired
	HotelDao hotelDao;
	@Autowired
	RoomTypeService roomTypeService;
	@Autowired
	DiscountSchemeService discountSchemeService;
	@Autowired
	MarketingSourcesService marketingSourcesService;
	@Autowired
	DistributionChannelService distributionChannelService;
	@Autowired
	RoomTagService roomTagService;
	@Autowired
	SystemConfigService systemConfigService;

	@Override
	public Hotel add(Hotel hotel) {
		return hotelDao.saveAndFlush(hotel);
	}

	@Override
	public void delete(String id) {
		Hotel hotel = hotelDao.findById(id).get();
		if (hotel != null) {
			hotel.setDeleted(Constants.DELETED_TRUE);
		}
		hotelDao.saveAndFlush(hotel);
	}

	@Override
	public Hotel modify(Hotel hotel) {
		return hotelDao.saveAndFlush(hotel);
	}

	@Override
	public Hotel findById(String id) {
		return hotelDao.getOne(id);
	}

	@Override
	public List<Hotel> getAllByHotelCode(String code) {
		return null;// 默认不实现
		// return hotelDao.findByHotelCode(code);
	}

	@Override
	public Hotel getByHotelCode(String code) {
//		return null;//默认不实现
		return hotelDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<Hotel> listPage(PageRequest<Hotel> prq) {
		Example<Hotel> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(hotelDao.findAll(ex, req));
	}

	@Override
	public HotelInfoVo getHotelInfo(String currentHotleCode) {
		HotelInfoVo hv = new HotelInfoVo();
		hv.setRoomTypes(roomTypeService.getAllByHotelCode(currentHotleCode, Constants.DELETED_FALSE));
		hv.setMarketingSources(marketingSourcesService.getAllByHotelCode(currentHotleCode,Constants.DELETED_FALSE));
		hv.setDiscountSchemes(discountSchemeService.getAllByHotelCode(currentHotleCode,Constants.DELETED_FALSE));
		hv.setRoomTags(roomTagService.getAllByHotelCode(currentHotleCode,Constants.DELETED_FALSE));
		hv.setConfigs(systemConfigService.getWebConfig(currentHotleCode));
		hv.setDistributionChannels(distributionChannelService.getAllByHotelCode(currentHotleCode,Constants.DELETED_FALSE));
		return hv;
	}
	
}
