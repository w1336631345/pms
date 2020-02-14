package com.kry.pms.service.marketing;

import java.util.List;
import java.util.Map;

import com.kry.pms.model.http.response.marketing.RoomPriceSchemeVo;
import com.kry.pms.model.persistence.marketing.RoomPriceScheme;
import com.kry.pms.service.BaseService;

public interface RoomPriceSchemeService extends BaseService<RoomPriceScheme>{

	List<RoomPriceSchemeVo> findCorpationScheme(String currentHotleCode, String corpId);

    List<Map<String, Object>> getByHotelCode(String code);

    List<RoomPriceSchemeVo> findDefaultScheme(String hotelCode);

    List<RoomPriceScheme> getByCorpation(String protocolCId);

}