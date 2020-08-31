package com.kry.pms.manager.entity;

import com.kry.pms.model.persistence.room.RoomType;
import lombok.Data;

import java.util.List;
@Data
public class NewHotelVo {
    String name ;//名字
    String code;//编码
    String srcCode;//原始酒店编码
    String corpCode;//公司编码
}
