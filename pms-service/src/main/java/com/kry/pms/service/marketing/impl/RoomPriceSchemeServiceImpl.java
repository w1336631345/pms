package com.kry.pms.service.marketing.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kry.pms.base.Constants;
import com.kry.pms.base.PageRequest;
import com.kry.pms.base.PageResponse;
import com.kry.pms.dao.marketing.RoomPriceSchemeDao;
import com.kry.pms.model.http.response.marketing.RoomPriceSchemeVo;
import com.kry.pms.model.persistence.marketing.ProtocolCorpation;
import com.kry.pms.model.persistence.marketing.RoomPriceScheme;
import com.kry.pms.service.marketing.ProtocolCorpationService;
import com.kry.pms.service.marketing.RoomPriceSchemeService;

@Service
public class  RoomPriceSchemeServiceImpl implements  RoomPriceSchemeService{
	@Autowired
	 RoomPriceSchemeDao roomPriceSchemeDao;
	@Autowired
	ProtocolCorpationService protocolCorpationService;
	 
	 @Override
	public RoomPriceScheme add(RoomPriceScheme roomPriceScheme) {
		return roomPriceSchemeDao.saveAndFlush(roomPriceScheme);
	}

	@Override
	public void delete(String id) {
		RoomPriceScheme roomPriceScheme = roomPriceSchemeDao.findById(id).get();
		if (roomPriceScheme != null) {
			roomPriceScheme.setDeleted(Constants.DELETED_TRUE);
		}
		roomPriceSchemeDao.saveAndFlush(roomPriceScheme);
	}

	@Override
	public RoomPriceScheme modify(RoomPriceScheme roomPriceScheme) {
		return roomPriceSchemeDao.saveAndFlush(roomPriceScheme);
	}

	@Override
	public RoomPriceScheme findById(String id) {
		return roomPriceSchemeDao.getOne(id);
	}

	@Override
	public List<RoomPriceScheme> getAllByHotelCode(String code) {
		return null;//默认不实现
		//return roomPriceSchemeDao.findByHotelCode(code);
	}

	@Override
	public PageResponse<RoomPriceScheme> listPage(PageRequest<RoomPriceScheme> prq) {
		Example<RoomPriceScheme> ex = Example.of(prq.getExb());
		org.springframework.data.domain.PageRequest req;
		if (prq.getOrderBy() != null) {
			Sort sort = new Sort(prq.isAsc() ? Direction.ASC : Direction.DESC, prq.getOrderBy());
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize(), sort);
		} else {
			req = org.springframework.data.domain.PageRequest.of(prq.getPageNum(), prq.getPageSize());
		}
		return convent(roomPriceSchemeDao.findAll(ex, req));
	}
	@Override
	public List<RoomPriceSchemeVo> findDefaultScheme(String hotelCode){
		RoomPriceScheme rps = new RoomPriceScheme();
		rps.setHotelCode(hotelCode);
		rps.setIsDefault(true);
		rps.setDeleted(Constants.DELETED_FALSE);
		Example<RoomPriceScheme> ex = Example.of(rps);
		List<RoomPriceScheme> rpsc = roomPriceSchemeDao.findAll(ex);
		if(rpsc!=null&&!rpsc.isEmpty()) {
			return convertToVo(rpsc);
		}else {
			return null;
		}
		
	}

	@Override
	public List<RoomPriceSchemeVo> findCorpationScheme(String hotelCode, String corpId) {
		ProtocolCorpation pc = protocolCorpationService.findById(corpId);
		if(pc!=null) {
			List<RoomPriceScheme> rpsc = pc.getRoomPriceSchemes();
			if(rpsc!=null&&!rpsc.isEmpty()) {
				return convertToVo(rpsc);
			}else {
				return findDefaultScheme(hotelCode);
			}
		}
		//TODO
		return null;
	}
	 
	 private List<RoomPriceSchemeVo> convertToVo(List<RoomPriceScheme> rpsc){
			ArrayList<RoomPriceSchemeVo> data = new ArrayList<>();
			for(RoomPriceScheme rps:rpsc) {
				data.add(new RoomPriceSchemeVo(rps));
			}
			return data;
	 }
	 
	 
}
