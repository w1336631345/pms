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
public class RoomPriceSchemeVo{
	private String id;
	private String name;
	private String code;
	private List<SchemeItemVo> items;
	
	public RoomPriceSchemeVo(RoomPriceScheme rps){
		this.name = rps.getName();
		this.code = rps.getCode();
		this.id = rps.getId();
		SchemeItemVo item ;
		SetMealVo smv ;
		List<SetMealVo> smvs ;
		if(rps.getItems()!=null) {
			this.items= new ArrayList<SchemeItemVo>();
			for(RoomPriceSchemeItem i:rps.getItems()) {
				item = new SchemeItemVo();
				BeanUtils.copyProperties(i, item);
				item.setRoomTypeId(i.getRoomType().getId());
				if(i.getExtra()!=null&&!i.getExtra().isEmpty()) {
					smvs = new ArrayList<SetMealVo>();
					for(SetMeal sm : i.getExtra()) {
						smv = new SetMealVo();
						BeanUtils.copyProperties(sm, smv);
					}
					item.setExtrs(smvs);
				}
				this.items.add(item);
			}
			
		}
		
	}
}
