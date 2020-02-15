package com.kry.pms.model.persistence.guest;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.room.RoomType;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "t_member_room_type")
public class MemberRoomType extends PersistenceModel {

    @ManyToOne
    private MemberType memberType;
    @ManyToOne
    private RoomType roomType;
    @Column
    private Double memberPrice;
}