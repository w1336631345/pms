package com.kry.pms.service.marketing;

import java.util.List;

import com.kry.pms.model.http.response.marketing.RoomPriceSchemeVo;
import com.kry.pms.model.persistence.marketing.RoomPriceScheme;
import com.kry.pms.service.BaseService;

public interface RoomPriceSchemeService extends BaseService<RoomPriceScheme>{

	List<RoomPriceSchemeVo> findCorpationScheme(String currentHotleCode, String corpId);

	List<RoomPriceSchemeVo> findDefaultScheme(String hotelCode);

}