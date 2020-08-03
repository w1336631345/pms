package com.kry.pms.manager.entity;

import com.kry.pms.model.persistence.room.RoomType;
import lombok.Data;

import java.util.List;
@Data
public class NewHotelVo {
    String name;
    String code;
    String srcCode;
    String corpCode;
}
