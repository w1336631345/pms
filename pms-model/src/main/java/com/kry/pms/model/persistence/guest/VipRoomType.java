package com.kry.pms.model.persistence.guest;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.room.RoomType;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * vip类型对应房间类型价格
 */
@Entity
@Data
@Table(name = "t_vip_room_type")
public class VipRoomType extends PersistenceModel {

    @ManyToOne
    private VipType vipType;
    @ManyToOne
    private RoomType roomType;
    @Column
    private Double vipPrice;

}
