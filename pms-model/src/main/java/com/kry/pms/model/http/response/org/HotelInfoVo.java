package com.kry.pms.model.http.response.org;

import java.util.List;
import java.util.Map;

import com.kry.pms.model.persistence.marketing.DiscountScheme;
import com.kry.pms.model.persistence.marketing.DistributionChannel;
import com.kry.pms.model.persistence.marketing.MarketingSources;
import com.kry.pms.model.persistence.room.RoomTag;
import com.kry.pms.model.persistence.room.RoomType;

import com.kry.pms.model.persistence.sys.StaticResource;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ManyToMany;

@Getter
@Setter
public class HotelInfoVo {
	private String hotelName;
	private String wechatCodeUrl;
	private String address;
	private String mobile;
	private List<RoomType> roomTypes;
	private List<DiscountScheme> discountSchemes;
	private List<MarketingSources> marketingSources;
	private List<DistributionChannel> distributionChannels;
	private List<RoomTag> roomTags;
	private Map<String,String> configs;
	private Map<String,Map<String,String>> dict;
	private List<StaticResource> promotionalPictures;
	private List<StaticResource> environmentPictures;
	private String vrUrl;
}
