package com.kry.pms.model.persistence.busi;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;

import lombok.Getter;
import lombok.Setter;
@Entity
@Getter
@Setter
@Table(name = "t_room_lock_record")
public class RoomLockRecord extends PersistenceModel{

}
