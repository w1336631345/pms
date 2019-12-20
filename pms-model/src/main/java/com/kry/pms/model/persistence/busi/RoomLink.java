package com.kry.pms.model.persistence.busi;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_room_link")
public class RoomLink {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(columnDefinition = "varchar(64)")
	private String id;
	@Column
	private String linkNum;
	@Column
	private String linkName;
	@Column
	private String hotelCode;
	@Column
	private int deleted;
}
