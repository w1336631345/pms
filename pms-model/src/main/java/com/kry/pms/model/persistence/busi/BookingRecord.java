package com.kry.pms.model.persistence.busi;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.marketing.DistributionChannel;
import com.kry.pms.model.persistence.marketing.MarketingSources;
import com.kry.pms.model.persistence.marketing.ProtocolCorpation;
import com.kry.pms.model.persistence.org.Employee;
import com.kry.pms.model.persistence.room.RoomType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "t_booking_record")
public class BookingRecord extends PersistenceModel {
	@Column
	public Integer roomCount;
	@Column
	private String name;
	@Column(name = "type_")
	private String type;
	@OneToOne
	public RoomType roomType;
	@Column
	private String contactName;
	@Column
	private String contactMobile;
	@OneToOne
	private MarketingSources marketingSources;
	@OneToOne
	private ProtocolCorpation protocolCorpation;
	@OneToOne
	private Employee operationEmployee;
	@OneToOne
	private DistributionChannel distributionChannel;
	@OneToOne
	private Employee marketEmployee;
	@Column
	private LocalDateTime arriveTime;
	@Column
	private LocalDateTime leaveTime;
	@OneToOne
	private Group group;
	@Column
	private Integer days;
	@Column
	private Integer humanCount;
	@Column
	private String holdTime;
	@Column
	private String remark;
	@Column
	private Boolean priceSecret;
	@Column
	private Integer checkInRoomCount;
	@Column
	private Integer checkInHumanCount;
	@OneToMany(targetEntity = CheckInRecord.class,cascade = CascadeType.ALL)
	@JoinColumn(name="booking_record_id")
	private List<CheckInRecord> checkInRecords;
	public Integer getRoomCount() {
		return roomCount;
	}
	public void setRoomCount(Integer roomCount) {
		this.roomCount = roomCount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public RoomType getRoomType() {
		return roomType;
	}
	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactMobile() {
		return contactMobile;
	}
	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}
	public Employee getOperationEmployee() {
		return operationEmployee;
	}
	public void setOperationEmployee(Employee operationEmployee) {
		this.operationEmployee = operationEmployee;
	}
	public DistributionChannel getDistributionChannel() {
		return distributionChannel;
	}
	public void setDistributionChannel(DistributionChannel distributionChannel) {
		this.distributionChannel = distributionChannel;
	}
	public Employee getMarketEmployee() {
		return marketEmployee;
	}
	public void setMarketEmployee(Employee marketEmployee) {
		this.marketEmployee = marketEmployee;
	}
	public LocalDateTime getArriveTime() {
		return arriveTime;
	}
	public void setArriveTime(LocalDateTime arriveTime) {
		this.arriveTime = arriveTime;
	}
	public LocalDateTime getLeaveTime() {
		return leaveTime;
	}
	public void setLeaveTime(LocalDateTime leaveTime) {
		this.leaveTime = leaveTime;
	}
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	public Integer getHumanCount() {
		return humanCount;
	}
	public void setHumanCount(Integer humanCount) {
		this.humanCount = humanCount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Boolean getPriceSecret() {
		return priceSecret;
	}
	public void setPriceSecret(Boolean priceSecret) {
		this.priceSecret = priceSecret;
	}
	public Integer getCheckInRoomCount() {
		return checkInRoomCount;
	}
	public void setCheckInRoomCount(Integer checkInRoomCount) {
		this.checkInRoomCount = checkInRoomCount;
	}
	public Integer getCheckInHumanCount() {
		return checkInHumanCount;
	}
	public void setCheckInHumanCount(Integer checkInHumanCount) {
		this.checkInHumanCount = checkInHumanCount;
	}
	public ProtocolCorpation getProtocolCorpation() {
		return protocolCorpation;
	}
	public void setProtocolCorpation(ProtocolCorpation protocolCorpation) {
		this.protocolCorpation = protocolCorpation;
	}
	public MarketingSources getMarketingSources() {
		return marketingSources;
	}
	public void setMarketingSources(MarketingSources marketingSources) {
		this.marketingSources = marketingSources;
	}
	@JsonIgnore
	public List<CheckInRecord> getCheckInRecords() {
		return checkInRecords;
	}
	public void setCheckInRecords(List<CheckInRecord> checkInRecords) {
		this.checkInRecords = checkInRecords;
	}
	public String getHoldTime() {
		return holdTime;
	}
	public void setHoldTime(String holdTime) {
		this.holdTime = holdTime;
	}
	
	

}
