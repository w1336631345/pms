package com.kry.pms.model.persistence.room;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.kry.pms.model.persistence.PersistenceModel;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "t_guest_room")
public class GuestRoom extends PersistenceModel {
	@OneToOne(fetch = FetchType.LAZY)
	private RoomType roomType;
	@OneToMany(cascade = CascadeType.PERSIST)
	private List<RoomTag> tags;
	@OneToOne(fetch = FetchType.LAZY)
	private Building building;
	@OneToOne(fetch = FetchType.LAZY)
	private Floor floor;
	@Column
	private String roomNum;

}
