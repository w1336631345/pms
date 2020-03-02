package com.kry.pms.model.http.response.marketing;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.kry.pms.model.http.response.goods.SetMealVo;
import com.kry.pms.model.persistence.goods.SetMeal;
import com.kry.pms.model.persistence.marketing.RoomPriceScheme;
import com.kry.pms.model.persistence.marketing.RoomPriceSchemeItem;

import lombok.Data;

@Data
public class RoomPriceSchemeVo {
	private String id;
	private String name;
	private String code;
	private List<SchemeItemVo> items;
	private SetMeal setMeal;

	public RoomPriceSchemeVo(RoomPriceScheme rps) {
		this.name = rps.getName();
		this.code = rps.getCode();
		this.id = rps.getId();
//		this.setMeal = rps.getSetMeal();
		SchemeItemVo item;
		SetMealVo smv;
		if (rps.getItems() != null) {
			this.items = new ArrayList<SchemeItemVo>();
			for (RoomPriceSchemeItem i : rps.getItems()) {
				item = new SchemeItemVo();
				BeanUtils.copyProperties(i, item);
				item.setRoomTypeId(i.getRoomType().getId());
				item.setRoomTypePrice(i.getRoomType().getPrice());
				if (i.getSetMeal() != null) {
					smv = new SetMealVo();
					BeanUtils.copyProperties(i.getSetMeal(),smv);
					item.setExtrs(smv);
				}
				this.items.add(item);
			}
		}
	}
}
