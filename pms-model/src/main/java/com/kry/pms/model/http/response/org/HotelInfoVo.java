package com.kry.pms.model.http.response.org;

import java.util.List;
import java.util.Map;

import com.kry.pms.model.persistence.marketing.DiscountScheme;
import com.kry.pms.model.persistence.marketing.DistributionChannel;
import com.kry.pms.model.persistence.marketing.MarketingSources;
import com.kry.pms.model.persistence.room.RoomTag;
import com.kry.pms.model.persistence.room.RoomType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HotelInfoVo {
	private String hotelName;
	private String wechatCodeUrl;
	private List<RoomType> roomTypes;
	private List<DiscountScheme> discountSchemes;
	private List<MarketingSources> marketingSources;
	private List<DistributionChannel> distributionChannels;
	private List<RoomTag> roomTags;
	private Map<String,String> configs;
}
