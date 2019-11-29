package com.kry.pms.model.persistence.busi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.guest.Customer;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.room.GuestRoom;

import lombok.Getter;
import lombok.Setter;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;

/**
 * 每人每天一个客房记录
 * 
 * @author Louis
 *
 */
@Entity
@Getter
@Setter
@Table(name = "t_room_record")
public class RoomRecord extends PersistenceModel {
	@OneToOne
	private CheckInRecord checkInRecord;
	@Column
	private LocalDate recordDate;
	@OneToOne
	private Customer customer;
	@OneToOne
	private GuestRoom guestRoom;
	@Column
	private Double cost;
	@Column
	private Double costRatio;
	@OneToOne
	private DailyVerify dailyVerify;
	@Column(columnDefinition = "varchar(32) default 'NO' COMMENT '是否入账（PAY：入账，NO：未入账）'")
	private String isAccountEntry = "NO";//是否入账
}
